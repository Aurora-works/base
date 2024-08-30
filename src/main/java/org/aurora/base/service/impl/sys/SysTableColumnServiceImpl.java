package org.aurora.base.service.impl.sys;

import org.aurora.base.common.dto.TableFormatter;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysDictDao;
import org.aurora.base.dao.sys.SysParamDao;
import org.aurora.base.dao.sys.SysTableColumnDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysParam;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysTableColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysTableColumnServiceImpl extends BaseServiceImpl<SysTableColumn> implements SysTableColumnService {
    @Autowired
    public SysTableColumnServiceImpl(SysTableColumnDao columnDao, SysParamDao paramDao, SysDictDao dictDao, SysTableDao tableDao) {
        this.columnDao = columnDao;
        this.paramDao = paramDao;
        this.dictDao = dictDao;
        this.tableDao = tableDao;
    }

    private final SysTableColumnDao columnDao;
    private final SysParamDao paramDao;
    private final SysDictDao dictDao;
    private final SysTableDao tableDao;

    @Override
    protected BaseDao<SysTableColumn> getDao() {
        return columnDao;
    }

    @Override
    public void save(Map<String, List<SysTableColumn>> changes) {
        List<SysTableColumn> createList = changes.get("inserted");
        List<SysTableColumn> updateList = changes.get("updated");
        List<SysTableColumn> deleteList = changes.get("deleted");
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

    private void create(List<SysTableColumn> columns) {
        for (SysTableColumn column : columns) {
            if (column.getId() != null) {
                throw new IllegalArgumentException();
            }
            columnDao.create(column);
        }
    }

    private void update(List<SysTableColumn> columns) {
        for (SysTableColumn column : columns) {
            if (column.getId() == null) {
                throw new IllegalArgumentException();
            }
            columnDao.update(column);
        }
    }

    private void delete(List<SysTableColumn> columns) {
        for (SysTableColumn column : columns) {
            if (column.getId() == null) {
                throw new IllegalArgumentException();
            }
            columnDao.delete(column.getId());
        }
    }

    @Override
    public PageHelper<SysTableColumn> findByTableId(int page, int size, String sort, String order, Long tableId) {
        checkFields(sort, order, null);
        SysParam param = paramDao.findByCode(org.aurora.base.common.dict.SysParam.SYS_DEFAULT_TABLE_COLUMN.getCode());
        return new PageHelper<>(
                columnDao.getTotalByTableId(tableId, param.getParamValue()),
                columnDao.findByTableId(page, size, sort, order, tableId, param.getParamValue()),
                getFormatters());
    }

    @Override
    public List<TableFormatter> getFormatters() {
        List<TableFormatter> formatters = super.getFormatters();
        formatters.addAll(dictDao.findAllGroupByCode());
        formatters.addAll(tableDao.getFormatters());
        return formatters;
    }
}
