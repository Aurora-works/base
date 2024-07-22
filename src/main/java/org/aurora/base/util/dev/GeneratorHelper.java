package org.aurora.base.util.dev;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aurora.base.entity.sys.SysGenerateCode;
import org.aurora.base.entity.sys.SysTableColumn;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GeneratorHelper {

    private final String javaPath; // java文件生成路径
    private final String webPath; // jsp文件生成路径
    private final String projectName; // 项目名称
    private final SysGenerateCode generateCode; // 生成代码使用的功能菜单和数据库表单
    private final List<SysTableColumn> columns; // 生成数据库表单的行信息
    private final String tableDesc; // 表描述
    private final String tableName; // 表名
    private final String menuCode; // 菜单编码
    private final String package1; // 文件一级目录
    private final String package2; // 文件二级目录
    private final String entityName; // 实体类名
    private final String entityName_; // 实体类名 (首字母小写)

    private final String entityPath; // 实体类
    private final String daoPath; // 持久层接口
    private final String daoImplPath; // 持久层实现
    private final String servicePath; // 业务层接口
    private final String serviceImplPath; // 业务层实现
    private final String controllerPath; // 控制层

    private Map<String, Object> data = new HashMap<>();

    public GeneratorHelper(SysGenerateCode generateCode, String generatePath, String projectName) {
        this.javaPath = generatePath + "/" + projectName + "/src/main/java/org/aurora/" + projectName; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base
        this.webPath = generatePath + "/" + projectName + "/src/main/webapp/WEB-INF/jsp"; // D:/SystemGenerateCode/base/src/main/webapp/WEB-INF/jsp
        this.projectName = projectName; // base
        this.generateCode = generateCode;
        this.columns = generateCode.getTable().getColumns().stream().sorted(Comparator.comparing(SysTableColumn::getOrderBy)).toList();
        this.tableDesc = generateCode.getTable().getTableDesc();
        this.tableName = generateCode.getTable().getTableName();
        this.menuCode = generateCode.getMenu().getMenuCode(); // sys_user
        this.package1 = menuCode.substring(0, menuCode.indexOf("_")).toLowerCase(); // sys
        this.package2 = menuCode.substring(menuCode.indexOf("_") + 1).toLowerCase(); // user
        this.entityName = generateCode.getTable().getEntityName(); // SysUser
        this.entityName_ = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1); // sysUser
        this.entityPath = javaPath + "/entity/" + package1 + "/" + entityName + ".java"; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base/entity/sys/SysUser.java
        this.daoPath = javaPath + "/dao/" + package1 + "/" + entityName + "Dao.java"; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base/dao/sys/SysUserDao.java
        this.daoImplPath = javaPath + "/dao/impl/" + package1 + "/" + entityName + "DaoImpl.java"; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base/dao/impl/sys/SysUserDaoImpl.java
        this.servicePath = javaPath + "/service/" + package1 + "/" + entityName + "Service.java"; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base/service/sys/SysUserService.java
        this.serviceImplPath = javaPath + "/service/impl/" + package1 + "/" + entityName + "ServiceImpl.java"; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base/service/impl/sys/SysUserServiceImpl.java
        this.controllerPath = javaPath + "/controller/" + package1 + "/" + entityName + "Controller.java"; // D:/SystemGenerateCode/base/src/main/java/org/aurora/base/controller/sys/SysUserController.java

        data.put("projectName", projectName);
        data.put("columns", columns);
        data.put("tableDesc", tableDesc);
        data.put("tableName", tableName);
        data.put("menuCode", menuCode);
        data.put("package1", package1);
        data.put("package2", package2);
        data.put("entityName", entityName);
        data.put("entityName_", entityName_);
    }
}
