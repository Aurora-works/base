package org.aurora.base.service.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aurora.base.dao.BaseDao;
import org.aurora.base.dao.sys.SysDictDao;
import org.aurora.base.dao.sys.SysTableColumnDao;
import org.aurora.base.dao.sys.SysTableDao;
import org.aurora.base.entity.BaseEntity;
import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.service.BaseService;
import org.aurora.base.util.dto.TableFormatter;
import org.aurora.base.util.poi.ExcelUtils;
import org.aurora.base.util.reflect.BeanInfoUtils;
import org.aurora.base.util.view.FilterRuleHelper;
import org.aurora.base.util.view.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    @SuppressWarnings("unchecked")
    protected BaseServiceImpl() {
        entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        var superClass = entityClass.getSuperclass();
        entityName = entityClass.getSimpleName();
        entityFields = ArrayUtils.addAll(entityClass.getDeclaredFields(), superClass.getDeclaredFields());
        otherFields = new String[]{"createUser.nickname"};
    }

    private final Class<T> entityClass;
    private final String entityName;
    private final Field[] entityFields;
    private final String[] otherFields;

    protected abstract BaseDao<T> getDao();

    @Autowired
    private SysDictDao dictDao;
    @Autowired
    private SysTableDao tableDao;
    @Autowired
    private SysTableColumnDao tableColumnDao;

    @Override
    public T findById(Long id) {
        return getDao().findByIdWithFetchGraph(id);
        // return getDao().findById(id);
    }

    @Override
    public void create(T entity) {
        getDao().create(entity);
    }

    @Override
    public void silentCreate(T entity) {
        getDao().silentCreate(entity);
    }

    @Override
    public void update(T entity) {
        getDao().update(entity);
    }

    @Override
    public void delete(Long id) {
        getDao().delete(id);
    }

    @Override
    public void delete(Long[] ids) {
        getDao().delete(ids);
    }

    @Override
    public List<T> findAll(String sort, String order) {
        checkFields(sort, order, null);
        return getDao().findAll(sort, order, addBusinessFilterRules(null));
    }

    @Override
    public PageHelper<T> findAllInPage(String sort, String order) {
        checkFields(sort, order, null);
        return new PageHelper<>(
                getDao().getTotal(addBusinessFilterRules(null)),
                getDao().findAll(sort, order, addBusinessFilterRules(null)),
                getFormatters());
    }

    @Override
    public PageHelper<T> findAllInPage(int page, int size, String sort, String order, List<FilterRuleHelper> filterRules) {
        checkFields(sort, order, filterRules);
        filterRules = addBusinessFilterRules(filterRules);
        return new PageHelper<>(
                getDao().getTotal(filterRules),
                getDao().findAll(page, size, sort, order, filterRules),
                getFormatters());
    }

    @Override
    public List<TableFormatter> getFormatters() {
        List<SysTableColumn> columns = tableColumnDao.findByTableEntityName(entityName);
        List<TableFormatter> formatters = new ArrayList<>();
        Set<String> dictCodeSet = new HashSet<>();
        for (SysTableColumn column : columns) {
            if (StringUtils.isNotBlank(column.getDictCode())) {
                dictCodeSet.add(column.getDictCode());
            }
        }
        if (!dictCodeSet.isEmpty()) {
            formatters.addAll(dictDao.findFormatterByCodes(dictCodeSet.toArray(new String[0])));
        }
        return formatters;
    }

    @Override
    public boolean uniqueValidate(String columnName, String value) {
        checkField(columnName);
        return getDao().columnValueCount(columnName, value) < 1;
    }

    @Override
    public String exportExcel(ByteArrayOutputStream stream, String sort, String order, List<FilterRuleHelper> filterRules) {
        checkFields(sort, order, filterRules);
        filterRules = addBusinessFilterRules(filterRules);
        // 数据列表
        List<T> list = getDao().findAll(sort, order, filterRules);
        // 数据表结构
        List<SysTableColumn> columns = tableColumnDao.findByTableEntityName(entityName);
        checkExportColumns(columns);
        SysTable table = tableDao.findByEntityName(entityName);
        String safeName = WorkbookUtil.createSafeSheetName(table.getTableDesc());
        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 创建工作表
        XSSFSheet sheet = workbook.createSheet(safeName);
        // 创建标题行
        XSSFRow titleRow = sheet.createRow(0);
        // 默认列宽
        sheet.setDefaultColumnWidth(20);
        // 标题行样式
        XSSFCellStyle titleStyle = ExcelUtils.getTitleStyle(workbook);
        // 设置筛选
        Cell cell;
        for (int i = 0, len = columns.size(); i < len; i++) {
            cell = titleRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(columns.get(i).getDisplayName());
        }
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, columns.size() - 1));
        // 冻结首行
        sheet.createFreezePane(0, 1);
        // 内容样式
        XSSFCellStyle rowStyle = ExcelUtils.getRowStyle(workbook);
        // 数据字典
        List<TableFormatter> formatters = getFormatters();
        // 内容
        XSSFRow row;
        for (int i = 0, len = list.size(); i < len; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0, clen = columns.size(); j < clen; j++) {
                cell = row.createCell(j);
                cell.setCellStyle(rowStyle);
                Object value = BeanInfoUtils.getPropertyValue(list.get(i), columns.get(j).getEntityName(), entityClass);
                if (value == null) {
                    continue;
                }
                // 数据字典处理
                if (StringUtils.isNotBlank(columns.get(j).getDictCode())) {
                    for (TableFormatter formatter : formatters) {
                        if (columns.get(j).getDictCode().equals(formatter.dictCode()) && value.toString().equals(formatter.dictKey())) {
                            value = formatter.dictValue();
                        }
                    }
                }
                cell.setCellValue(value.toString());
            }
        }

        ExcelUtils.write(workbook, stream);

        return safeName;
    }

    /**
     * 校验单个字段名称的合法性
     */
    protected void checkField(String fieldName) {
        for (Field field : entityFields) {
            if (field.getName().equals(fieldName)) return;
        }
        throw new IllegalArgumentException();
    }

    /**
     * 校验过滤条件中所有字段名称的合法性
     */
    protected void checkFields(String sort, String order, List<FilterRuleHelper> filterRules) {
        if (filterRules != null) {
            outer:
            for (FilterRuleHelper filterRule : filterRules) {
                for (Field field : entityFields) {
                    if (field.getName().equals(filterRule.getField())) {
                        filterRule.setFieldType(field.getType());
                        continue outer;
                    }
                }
                for (String otherField : otherFields) {
                    if (otherField.equals(filterRule.getField())) {
                        continue outer;
                    }
                }
                throw new IllegalArgumentException();
            }
        }
        if (sort != null) {
            boolean flag = true;
            for (Field field : entityFields) {
                if (field.getName().equals(sort)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                for (String otherField : otherFields) {
                    if (otherField.equals(sort)) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                throw new IllegalArgumentException();
            }
        }
        if (order != null) {
            if (!"asc".equals(order) && !"desc".equals(order)) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * 子类可重写此方法追加业务过滤条件
     */
    protected List<FilterRuleHelper> addBusinessFilterRules(List<FilterRuleHelper> filterRules) {
        return filterRules;
    }

    /**
     * 子类可重写此方法控制Excel导出的列
     */
    protected void checkExportColumns(List<SysTableColumn> columns) {
    }
}
