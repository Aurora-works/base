package org.aurora.base.service;

import org.aurora.base.entity.BaseEntity;

public interface BaseService<T extends BaseEntity> {

    T findById(Long id);

    void create(T entity);

    void silentCreate(T entity);

    void update(T entity);

    void delete(Long id);

    void delete(Long[] ids);
}
