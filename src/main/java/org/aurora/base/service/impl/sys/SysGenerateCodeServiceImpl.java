package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysGenerateCodeDao;
import org.aurora.base.dao.sys.SysMenuDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysGenerateCodeService;
import org.aurora.base.util.dto.TableFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysGenerateCodeServiceImpl extends BaseServiceImpl<SysGenerateCode> implements SysGenerateCodeService {
    @Autowired
    public SysGenerateCodeServiceImpl(SysGenerateCodeDao generateCodeDao, SysTableDao tableDao, SysMenuDao menuDao) {
        this.generateCodeDao = generateCodeDao;
        this.tableDao = tableDao;
        this.menuDao = menuDao;
    }

    private final SysGenerateCodeDao generateCodeDao;
    private final SysTableDao tableDao;
    private final SysMenuDao menuDao;

    @Override
    protected BaseDao<SysGenerateCode> getDao() {
        return generateCodeDao;
    }

    @Override
    public List<TableFormatter> getFormatters() {
        List<TableFormatter> formatters = super.getFormatters();
        formatters.addAll(tableDao.getFormatters());
        formatters.addAll(menuDao.getFormatters());
        return formatters;
    }

    @Override
    public void save(Map<String, List<SysGenerateCode>> changes) {
        List<SysGenerateCode> createList = changes.get("inserted");
        List<SysGenerateCode> updateList = changes.get("updated");
        List<SysGenerateCode> deleteList = changes.get("deleted");
        if (createList != null) {
            for (SysGenerateCode gen : createList) {
                if (gen.getId() != null) {
                    throw new IllegalArgumentException();
                }
                generateCodeDao.create(gen);
            }
        }
        if (updateList != null) {
            for (SysGenerateCode gen : updateList) {
                if (gen.getId() == null) {
                    throw new IllegalArgumentException();
                }
                generateCodeDao.update(gen);
            }
        }
        if (deleteList != null) {
            for (SysGenerateCode gen : deleteList) {
                if (gen.getId() == null) {
                    throw new IllegalArgumentException();
                }
                generateCodeDao.delete(gen.getId());
            }
        }
    }

    @Override
    public void code(Long id) {

    }
}
