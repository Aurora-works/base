package org.aurora.base.service.impl;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.entity.BaseEntity;
import org.aurora.base.service.BaseService;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    protected abstract BaseDao<T> getDao();

    @Override
    public T findById(Long id) {
        return getDao().findByIdWithFetchGraph(id);
    }

    @Override
    public void create(T entity) {
        getDao().create(entity);
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
}
