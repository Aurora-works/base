package org.aurora.base.util.dev;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;
import org.aurora.base.common.dict.ColumnType;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.exception.BusinessException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 生成代码
 */
@Log4j2
public class CodeGenerator {

    private static final Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_34);
        cfg.setClassForTemplateLoading(CodeGenerator.class, "/" + CodeGenerator.class.getPackageName().replace(".", "/") + "/templates");
        cfg.setDefaultEncoding("UTF-8");
    }

    public static void code(GeneratorHelper helper) {
        generateRead(helper);
        generateDetail(helper);
        generateImportExcel(helper);
        generateList(helper);
        generateEntity(helper);
        generateDao(helper);
        generateDaoImpl(helper);
        generateService(helper);
        generateServiceImpl(helper);
        generateController(helper);
    }

    private static void generateRead(GeneratorHelper helper) {
        Template template = getTemplate("_read.jsp.ftl");
        generateCode(template, helper.getData(), helper.getReadPath());
    }

    private static void generateDetail(GeneratorHelper helper) {
        Template template = getTemplate("_detail.jsp.ftl");
        generateCode(template, helper.getData(), helper.getDetailPath());
    }

    private static void generateImportExcel(GeneratorHelper helper) {
        Template template = getTemplate("_import_excel.jsp.ftl");
        generateCode(template, helper.getData(), helper.getImportExcelPath());
    }

    private static void generateList(GeneratorHelper helper) {
        Template template = getTemplate("_list.jsp.ftl");
        generateCode(template, helper.getData(), helper.getListPath());
    }

    private static void generateEntity(GeneratorHelper helper) {

        Map<String, Object> data = helper.getData();

        // import
        // boolean hasForeign = false; // 记录是否有外键
        boolean hasLocalDate = false; // 记录是否有LocalDate类型
        boolean hasLocalTime = false; // 记录是否有LocalTime类型
        boolean hasLocalDateTime = false; // 记录是否有LocalDateTime类型
        boolean hasBigDecimal = false; // 记录是否有BigDecimal类型

        for (SysTableColumn column : helper.getColumns()) {
            // if (!hasForeign && column.getForeignTableId() != null) {
            //     hasForeign = true;
            // } else
            if (!hasLocalDate && ColumnType.LOCAL_DATE.getKey().equals(column.getColumnType())) {
                hasLocalDate = true;
            } else if (!hasLocalTime && ColumnType.LOCAL_TIME.getKey().equals(column.getColumnType())) {
                hasLocalTime = true;
            } else if (!hasLocalDateTime && ColumnType.LOCAL_DATE_TIME.getKey().equals(column.getColumnType())) {
                hasLocalDateTime = true;
            } else if (!hasBigDecimal && ColumnType.BIG_DECIMAL.getKey().equals(column.getColumnType())) {
                hasBigDecimal = true;
            }
        }

        // data.put("hasForeign", hasForeign);
        data.put("hasLocalDate", hasLocalDate);
        data.put("hasLocalTime", hasLocalTime);
        data.put("hasLocalDateTime", hasLocalDateTime);
        data.put("hasBigDecimal", hasBigDecimal);

        Template template = getTemplate("Entity.java.ftl");
        generateCode(template, helper.getData(), helper.getEntityPath());
    }

    private static void generateDao(GeneratorHelper helper) {
        Template template = getTemplate("Dao.java.ftl");
        generateCode(template, helper.getData(), helper.getDaoPath());
    }

    private static void generateDaoImpl(GeneratorHelper helper) {
        Template template = getTemplate("DaoImpl.java.ftl");
        generateCode(template, helper.getData(), helper.getDaoImplPath());
    }

    private static void generateService(GeneratorHelper helper) {
        Template template = getTemplate("Service.java.ftl");
        generateCode(template, helper.getData(), helper.getServicePath());
    }

    private static void generateServiceImpl(GeneratorHelper helper) {
        Template template = getTemplate("ServiceImpl.java.ftl");
        generateCode(template, helper.getData(), helper.getServiceImplPath());
    }

    private static void generateController(GeneratorHelper helper) {
        Template template = getTemplate("Controller.java.ftl");
        generateCode(template, helper.getData(), helper.getControllerPath());
    }

    private static Template getTemplate(String templateName) {
        try {
            return cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateCode(Template template, Map<String, Object> data, String fileName) {
        checkFileDir(fileName);
        try (FileWriter out = new FileWriter(fileName)) {
            template.process(data, out);
            out.flush();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkFileDir(String path) {
        File file = new File(path);
        // 判断文件目录是否存在
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                log.info("目录 [{}] 创建成功", dir.getAbsolutePath());
            } else {
                throw new BusinessException("目录 [" + dir.getAbsolutePath() + "] 创建失败");
            }
        }
    }
}
