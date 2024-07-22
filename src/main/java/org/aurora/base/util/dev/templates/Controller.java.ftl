package org.aurora.${projectName}.controller.${package1};

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.${projectName}.controller.BaseController;
import org.aurora.${projectName}.entity.${package1}.${entityName};
import org.aurora.${projectName}.service.BaseService;
import org.aurora.${projectName}.service.${package1}.${entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/${package1}/${package2}")
@RequiresAuthentication
public class ${entityName}Controller extends BaseController<${entityName}> {
    @Autowired
    public ${entityName}Controller(${entityName}Service ${entityName_}Service) {
        this.${entityName_}Service = ${entityName_}Service;
    }

    private final ${entityName}Service ${entityName_}Service;

    @Override
    protected String getViewPath() {
        return "/${package1}/${package2}/${package2}_";
    }

    @Override
    protected String getMenuCode() {
        return "${menuCode}";
    }

    @Override
    protected BaseService<${entityName}> getService() {
        return ${entityName_}Service;
    }
}
