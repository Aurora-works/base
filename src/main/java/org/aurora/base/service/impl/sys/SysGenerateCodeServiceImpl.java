package org.aurora.base.service.impl.sys;

import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysGenerateCodeDao;
import org.aurora.base.dao.sys.SysMenuDao;
import org.aurora.base.dao.sys.SysParamDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.service.impl.BaseServiceImpl;
import org.aurora.base.service.sys.SysGenerateCodeService;
import org.aurora.base.util.dev.CodeGenerator;
import org.aurora.base.util.dev.GeneratorHelper;
import org.aurora.base.util.dto.TableFormatter;
import org.aurora.base.util.enums.SysParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysGenerateCodeServiceImpl extends BaseServiceImpl<SysGenerateCode> implements SysGenerateCodeService {
    @Autowired
    public SysGenerateCodeServiceImpl(SysGenerateCodeDao generateCodeDao, SysParamDao paramDao, SysTableDao tableDao, SysMenuDao menuDao) {
        this.generateCodeDao = generateCodeDao;
        this.paramDao = paramDao;
        this.tableDao = tableDao;
        this.menuDao = menuDao;
    }

    private final SysGenerateCodeDao generateCodeDao;
    private final SysParamDao paramDao;
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
        SysGenerateCode generateCode = generateCodeDao.findByIdWithFetchGraph2(id);
        String generatePath = paramDao.findByCode(SysParam.SYS_GENERATE_PATH.getCode()).getParamValue();
        String projectName = paramDao.findByCode(SysParam.SYS_PROJECT_NAME.getCode()).getParamValue();
        CodeGenerator.code(new GeneratorHelper(generateCode, generatePath, projectName));
    }
}
