package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysDictDao;
import org.aurora.base.entity.sys.SysDict;
import org.aurora.base.util.dto.TableFormatter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysDictDaoImpl extends BaseDaoImpl<SysDict> implements SysDictDao {

    @Override
    public List<TableFormatter> findFormatterByCodes(String[] codes) {
        String hql = "select dictCode, dictKey, dictValue from SysDict where dictCode in(:codes)";
        return getSession().createSelectionQuery(hql, TableFormatter.class)
                .setParameterList("codes", codes)
                .list();
    }

    @Override
    public List<SysDict> findByCode(String dictCode) {
        String hql = "from SysDict where lower(dictCode) = lower(:dictCode) order by orderBy asc";
        return getSession().createSelectionQuery(hql, SysDict.class)
                .setParameter("dictCode", dictCode)
                .list();
    }
}
