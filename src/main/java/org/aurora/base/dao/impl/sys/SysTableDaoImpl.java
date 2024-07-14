package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.util.dto.TableFormatter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysTableDaoImpl extends BaseDaoImpl<SysTable> implements SysTableDao {

    @Override
    public List<TableFormatter> getFormatters() {
        String hql = "select '" + getEntityName() + "', str(id), tableDesc from SysTable";
        return getSession().createSelectionQuery(hql, TableFormatter.class)
                .setCacheable(true)
                .list();
    }

    @Override
    public List<SysTable> findByForeignTableId(Long tableId) {
        String hql = "from SysTable t join t.columns c where c.tableId <> :tableId and c.foreignTableId = :tableId";
        return getSession().createSelectionQuery(hql, SysTable.class)
                .setParameter("tableId", tableId)
                .list();
    }

    @Override
    public SysTable findByEntityName(String entityName) {
        String hql = "from SysTable where entityName = :entityName";
        return getSession().createSelectionQuery(hql, SysTable.class)
                .setParameter("entityName", entityName)
                .uniqueResult();
    }
}
