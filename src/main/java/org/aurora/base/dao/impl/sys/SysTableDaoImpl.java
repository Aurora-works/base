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
    public List<TableFormatter> findFormatterByIds(Long[] ids) {
        String hql = "select 'SysTable', str(id), tableDesc from SysTable where id in(:ids)";
        return getSession().createSelectionQuery(hql, TableFormatter.class)
                .setParameterList("ids", ids)
                .setCacheable(true)
                .list();
    }
}
