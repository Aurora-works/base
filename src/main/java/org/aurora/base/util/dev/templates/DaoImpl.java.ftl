package org.aurora.${projectName}.dao.impl.${package1};

import org.aurora.${projectName}.dao.impl.BaseDaoImpl;
import org.aurora.${projectName}.dao.${package1}.${entityName}Dao;
import org.aurora.${projectName}.entity.${package1}.${entityName};
import org.springframework.stereotype.Repository;

@Repository
public class ${entityName}DaoImpl extends BaseDaoImpl<${entityName}> implements ${entityName}Dao {
}
