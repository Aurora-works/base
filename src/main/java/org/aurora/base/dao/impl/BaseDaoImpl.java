package org.aurora.base.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.BaseEntity;
import org.aurora.base.util.view.FilterRuleHelper;
import org.hibernate.Session;
import org.hibernate.query.SelectionQuery;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

public abstract class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {
    @SuppressWarnings("unchecked")
    protected BaseDaoImpl() {
        entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        entityName = entityClass.getSimpleName();
        entityClassName = entityClass.getName();
    }

    private final Class<T> entityClass;
    private final String entityName;
    private final String entityClassName;

    @PersistenceContext
    private EntityManager entityManager;

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    protected String getEntityName() {
        return entityName;
    }

    @Override
    public T findById(Long id) {
        return getSession().find(entityClass, id);
    }

    @Override
    public T findByIdWithFetchGraph(Long id) {
        var graph = getSession().createEntityGraph(entityClass);
        graph.addSubgraph("createUser");
        graph.addSubgraph("lastUser");
        return getSession().byId(entityClass).withFetchGraph(graph).load(id);
    }

    @Override
    public List<T> findByIds(Long[] ids) {
        String hql = "from " + entityClassName + " e where e.id in(:ids)";
        return getSession().createSelectionQuery(hql, entityClass)
                .setParameterList("ids", ids)
                .list();
    }

    @Override
    public void create(T entity) {
        getSession().persist(entity);
    }

    @Override
    public void silentCreate(T entity) {
        getSession().persist(entity);
    }

    @Override
    public void update(T entity) {
        getSession().merge(entity);
    }

    @Override
    public void delete(Long id) {
        getSession().remove(findById(id));
    }

    @Override
    public void delete(Long[] ids) {
        Arrays.stream(ids).forEach(this::delete);
    }

    @Override
    public List<T> findAll(String sort, String order, List<FilterRuleHelper> filterRules) {
        return findAll(0, 0, sort, order, filterRules);
    }

    @Override
    public List<T> findAll(int page, int size, String sort, String order, List<FilterRuleHelper> filterRules) {
        StringBuilder hql = new StringBuilder("from " + entityClassName + " e join fetch e.createUser");
        if (filterRules != null) {
            hql.append(" where 1=1");
            addFilterRules(hql, filterRules);
        }
        hql.append(" order by e.").append(sort).append(" ").append(order);
        var query = getSession().createSelectionQuery(hql.toString(), entityClass);
        if (filterRules != null) {
            setParameters(query, filterRules);
        }
        if (page == 0 && size == 0) {
            return query.list();
        }
        return query.setFirstResult((page - 1) * size).setMaxResults(size).list();
    }

    @Override
    public List<T> findAllWithCache(String sort, String order) {
        String hql = "from " + entityClassName + " order by " + sort + " " + order;
        return getSession().createSelectionQuery(hql, entityClass)
                .setCacheable(true)
                .list();
    }

    @Override
    public long getTotal(List<FilterRuleHelper> filterRules) {
        StringBuilder hql = new StringBuilder("select count(*) from " + entityClassName + " e");
        if (filterRules != null) {
            hql.append(" where 1=1");
            addFilterRules(hql, filterRules);
        }
        var query = getSession().createSelectionQuery(hql.toString(), Long.class);
        if (filterRules != null) {
            setParameters(query, filterRules);
        }
        return query.uniqueResult();
    }

    @Override
    public long columnValueCount(String columnName, Object value) {
        String hql = "select count(*) from " + entityClassName + " where lower(" + columnName + ") = lower(:value)";
        return getSession().createSelectionQuery(hql, Long.class)
                .setParameter("value", value)
                .uniqueResult();
    }

    private void addFilterRules(StringBuilder hql, List<FilterRuleHelper> filterRules) {
        for (FilterRuleHelper filterRule : filterRules) {
            if (FilterRuleHelper.DATE_BOX.equals(filterRule.getType())) {
                hql.append(" and extract(date from e.").append(filterRule.getField()).append(") ").append(filterRule.getOp()).append(" extract(date from :").append(filterRule.getField().replace(".", "_")).append(")");
                continue;
            }
            hql.append(" and e.").append(filterRule.getField()).append(" ").append(filterRule.getOp()).append(" :").append(filterRule.getField().replace(".", "_"));
        }
    }

    private void setParameters(SelectionQuery<?> query, List<FilterRuleHelper> filterRules) {
        for (FilterRuleHelper filterRule : filterRules) {
            query.setParameter(filterRule.getField().replace(".", "_"), filterRule.getValue());
        }
    }
}
