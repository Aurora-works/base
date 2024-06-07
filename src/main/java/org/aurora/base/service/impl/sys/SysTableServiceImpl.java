package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysTableServiceImpl extends BaseServiceImpl<SysTable> implements SysTableService {
    @Autowired
    public SysTableServiceImpl(SysTableDao tableDao) {
        this.tableDao = tableDao;
    }

    private final SysTableDao tableDao;

    @Override
    protected BaseDao<SysTable> getDao() {
        return tableDao;
    }

    @Override
    public void save(Map<String, List<SysTable>> changes) {
        List<SysTable> createList = changes.get("inserted");
        List<SysTable> updateList = changes.get("updated");
        List<SysTable> deleteList = changes.get("deleted");
        if (createList != null) {
            create(createList);
        }
        if (updateList != null) {
            update(updateList);
        }
        if (deleteList != null) {
            delete(deleteList);
        }
    }

    private void create(List<SysTable> tables) {
        for (SysTable table : tables) {
            if (table.getId() != null) {
                throw new IllegalArgumentException();
            }
            tableDao.create(table);
        }
    }

    private void update(List<SysTable> tables) {
        for (SysTable table : tables) {
            if (table.getId() == null) {
                throw new IllegalArgumentException();
            }
            tableDao.update(table);
        }
    }

    private void delete(List<SysTable> tables) {
        for (SysTable table : tables) {
            if (table.getId() == null) {
                throw new IllegalArgumentException();
            }
            List<SysTable> list = tableDao.findByForeignTableId(table.getId());
            if (!list.isEmpty()) {
                StringBuilder msg = new StringBuilder("请先删除从表：");
                for (SysTable t : list) {
                    msg.append(t.getTableDesc()).append("，");
                }
                throw new IllegalArgumentException(msg.deleteCharAt(msg.length() - 1).toString());
            }
            tableDao.delete(table.getId());
        }
    }
}
