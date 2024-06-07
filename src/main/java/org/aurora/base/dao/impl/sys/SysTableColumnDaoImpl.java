package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysTableColumnDao;
import org.aurora.base.entity.sys.SysTableColumn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysTableColumnDaoImpl extends BaseDaoImpl<SysTableColumn> implements SysTableColumnDao {

    @Override
    public List<SysTableColumn> findByTableEntityName(String tableEntityName) {
        String hql = "from SysTableColumn where table.entityName = :tableEntityName";
        return getSession().createSelectionQuery(hql, SysTableColumn.class)
                .setParameter("tableEntityName", tableEntityName)
                .setCacheable(true)
                .list();
    }

    @Override
    public List<SysTableColumn> findByTableId(int page, int size, String sort, String order, Long tableId, String defaultTableName) {
        String hql = "from SysTableColumn where tableId = :tableId or table.tableName = :defaultTableName order by " + sort + " " + order;
        return getSession().createSelectionQuery(hql, SysTableColumn.class)
                .setParameter("tableId", tableId)
                .setParameter("defaultTableName", defaultTableName)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .list();
    }

    @Override
    public Long getTotalByTableId(Long tableId, String defaultTableName) {
        String hql = "select count(*) from SysTableColumn where tableId = :tableId or table.tableName = :defaultTableName";
        return getSession().createSelectionQuery(hql, Long.class)
                .setParameter("tableId", tableId)
                .setParameter("defaultTableName", defaultTableName)
                .uniqueResult();
    }
}
