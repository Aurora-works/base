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
                .list();
    }
}
