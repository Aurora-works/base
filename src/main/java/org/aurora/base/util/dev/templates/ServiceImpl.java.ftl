package org.aurora.${projectName}.service.impl.${package1};

import org.aurora.${projectName}.dao.BaseDao;
import org.aurora.${projectName}.dao.${package1}.${entityName}Dao;
import org.aurora.${projectName}.entity.${package1}.${entityName};
import org.aurora.${projectName}.service.impl.BaseServiceImpl;
import org.aurora.${projectName}.service.${package1}.${entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${entityName}ServiceImpl extends BaseServiceImpl<${entityName}> implements ${entityName}Service {
    @Autowired
    public ${entityName}ServiceImpl(${entityName}Dao ${entityName_}Dao) {
        this.${entityName_}Dao = ${entityName_}Dao;
    }

    private final ${entityName}Dao ${entityName_}Dao;

    @Override
    protected BaseDao<${entityName}> getDao() {
        return ${entityName_}Dao;
    }
}
