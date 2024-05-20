package org.aurora.base.dao.impl.sys;

import org.aurora.base.dao.impl.BaseDaoImpl;
import org.aurora.base.dao.sys.SysMenuDao;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.util.dto.TableFormatter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysMenuDaoImpl extends BaseDaoImpl<SysMenu> implements SysMenuDao {

    @Override
    public List<TableFormatter> getFormatters() {
        String hql = "select 'SysMenu', str(id), menuName from SysMenu";
        return getSession().createSelectionQuery(hql, TableFormatter.class)
                .setCacheable(true)
                .list();
    }
}
