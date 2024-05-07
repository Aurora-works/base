package org.aurora.base.dao;

import org.aurora.base.entity.BaseEntity;
import org.aurora.base.util.view.FilterRuleHelper;

import java.util.List;

public interface BaseDao<T extends BaseEntity> {

    T findById(Long id);

    T findByIdWithFetchGraph(Long id);

    List<T> findByIds(Long[] ids);

    void create(T entity);

    void silentCreate(T entity);

    void update(T entity);

    void delete(Long id);

    void delete(Long[] ids);

    List<T> findAll(int page, int size, String sort, String order);

    List<T> findAll(int page, int size, String sort, String order, List<FilterRuleHelper> filterRules);

    long getTotal();

    long getTotal(List<FilterRuleHelper> filterRules);

    long columnValueCount(String columnName, Object value);
}
