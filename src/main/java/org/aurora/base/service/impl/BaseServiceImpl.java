package org.aurora.base.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysDictDao;
import org.aurora.base.dao.sys.SysTableColumnDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.BaseEntity;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.service.BaseService;
import org.aurora.base.util.dto.TableFormatter;
import org.aurora.base.util.view.FilterRuleHelper;
import org.aurora.base.util.view.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    @SuppressWarnings("unchecked")
    protected BaseServiceImpl() {
        var entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        var superClass = entityClass.getSuperclass();
        entityName = entityClass.getSimpleName();
        entityFields = ArrayUtils.addAll(entityClass.getDeclaredFields(), superClass.getDeclaredFields());
        otherFields = new String[]{"createUser.nickname"};
    }

    private final String entityName;
    private final Field[] entityFields;
    private final String[] otherFields;

    protected abstract BaseDao<T> getDao();

    @Autowired
    private SysDictDao dictDao;
    @Autowired
    private SysTableDao tableDao;
    @Autowired
    private SysTableColumnDao tableColumnDao;

    @Override
    public T findById(Long id) {
        return getDao().findByIdWithFetchGraph(id);
        // return getDao().findById(id);
    }

    @Override
    public void create(T entity) {
        getDao().create(entity);
    }

    @Override
    public void silentCreate(T entity) {
        getDao().silentCreate(entity);
    }

    @Override
    public void update(T entity) {
        getDao().update(entity);
    }

    @Override
    public void delete(Long id) {
        getDao().delete(id);
    }

    @Override
    public void delete(Long[] ids) {
        getDao().delete(ids);
    }

    @Override
    public PageHelper<T> findAll(int page, int size, String sort, String order, List<FilterRuleHelper> filterRules) {
        checkFields(filterRules);
        return new PageHelper<>(
                getDao().getTotal(filterRules),
                getDao().findAll(page, size, sort, order, filterRules),
                getFormatters());
    }

    @Override
    public List<TableFormatter> getFormatters() {
        List<SysTableColumn> columns = tableColumnDao.findByTableEntityName(entityName);
        List<TableFormatter> formatters = new ArrayList<>();
        Set<Long> foreignTableIdSet = new HashSet<>();
        Set<String> dictCodeSet = new HashSet<>();
        for (SysTableColumn column : columns) {
            if (column.getForeignTableId() != null) {
                foreignTableIdSet.add(column.getForeignTableId());
            } else if (StringUtils.isNotBlank(column.getDictCode())) {
                dictCodeSet.add(column.getDictCode());
            }
        }
        if (!foreignTableIdSet.isEmpty()) {
            formatters.addAll(tableDao.findFormatterByIds(foreignTableIdSet.toArray(new Long[0])));
        }
        if (!dictCodeSet.isEmpty()) {
            formatters.addAll(dictDao.findFormatterByCodes(dictCodeSet.toArray(new String[0])));
        }
        return formatters;
    }

    @Override
    public boolean uniqueValidate(String columnName, String value) {
        checkField(columnName);
        return getDao().columnValueCount(columnName, value) < 1;
    }

    protected void checkField(String fieldName) {
        for (Field field : entityFields) {
            if (field.getName().equals(fieldName)) return;
        }
        throw new IllegalArgumentException();
    }

    protected void checkFields(List<FilterRuleHelper> filterRules) {
        if (filterRules == null) return;
        outer_for:
        for (FilterRuleHelper filterRule : filterRules) {
            for (Field field : entityFields) {
                if (field.getName().equals(filterRule.getField())) {
                    filterRule.setFieldType(field.getType());
                    continue outer_for;
                }
            }
            for (String otherField : otherFields) {
                if (otherField.equals(filterRule.getField())) {
                    continue outer_for;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
