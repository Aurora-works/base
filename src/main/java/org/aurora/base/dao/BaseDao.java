package org.aurora.base.dao;

import org.aurora.base.entity.BaseEntity;

public interface BaseDao<T extends BaseEntity> {

    T findById(Long id);

    T findByIdWithFetchGraph(Long id);

    void create(T entity);

    void silentCreate(T entity);

    void update(T entity);

    void delete(Long id);

    void delete(Long[] ids);
}
