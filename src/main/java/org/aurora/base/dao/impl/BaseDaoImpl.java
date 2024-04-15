package org.aurora.base.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.BaseEntity;
import org.hibernate.Session;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public abstract class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

    private final Class<T> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    protected BaseDaoImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return entityManager.unwrap(Session.class);
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
    public void create(T entity) {
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
}
