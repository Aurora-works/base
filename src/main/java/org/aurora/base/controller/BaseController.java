package org.aurora.base.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.aurora.base.entity.BaseEntity;
import org.aurora.base.jackson.JSONUtils;
import org.aurora.base.service.BaseService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.Result;
import org.aurora.base.util.constant.CommonConstant;
import org.aurora.base.util.view.FilterRuleHelper;
import org.aurora.base.util.view.PageHelper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class BaseController<T extends BaseEntity> {

    protected abstract String getViewPath();

    protected abstract String getMenuCode();

    protected abstract BaseService<T> getService();

    /**
     * 跳转页面
     */
    @GetMapping(value = "/{view}")
    public String initView(@PathVariable String view) {
        return getViewPath() + view;
    }

    /**
     * 唯一验证
     */
    @PostMapping(value = "/validate/{columnName}")
    @ResponseBody
    public boolean uniqueValidate(@PathVariable String columnName, String value) {
        return getService().uniqueValidate(columnName, value);
    }

    /**
     * 数据列表
     */
    @PostMapping(value = "/list")
    @ResponseBody
    public PageHelper<T> getList(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "50") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "filterRules", required = false) String filterRules) {
        ShiroUtils.checkPermission(getMenuCode() + ":read");
        List<FilterRuleHelper> filterRuleList = parseFilterRules(filterRules);
        return getService().findAllInPage(page, size, sort, order, filterRuleList);
    }

    /**
     * 新增
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public Result<Object> create(T entity) {
        ShiroUtils.checkPermission(getMenuCode() + ":create");
        if (entity.getId() != null) {
            throw new IllegalArgumentException();
        }
        getService().create(entity);
        return Result.success();
    }

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public Result<Object> update(T entity) {
        ShiroUtils.checkPermission(getMenuCode() + ":update");
        if (entity.getId() == null) {
            throw new IllegalArgumentException();
        }
        getService().update(entity);
        return Result.success();
    }

    /**
     * 删除
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Result<Object> delete(
            @RequestParam(value = "ids[]", required = false) Long[] ids,
            @RequestParam(value = "id", required = false) Long id) {
        ShiroUtils.checkPermission(getMenuCode() + ":delete");
        if (ids != null) getService().delete(ids);
        else if (id != null) getService().delete(id);
        return Result.success();
    }

    /**
     * Excel导出
     */
    @GetMapping(value = "/excel/out")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "filterRules", required = false) String filterRules) {
        ShiroUtils.checkPermission(getMenuCode() + ":read");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String excelName = getService().exportExcel(stream, sort, order, parseFilterRules(filterRules));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(excelName, StandardCharsets.UTF_8) + ".xlsx")
                .contentType(MediaType.parseMediaType(CommonConstant.MIME_TYPE_XLSX))
                .contentLength(stream.size())
                .body(new ByteArrayResource(stream.toByteArray()));
    }

    /**
     * Excel导入
     */
    @PostMapping(value = "/excel/in")
    @ResponseBody
    public Result<Object> importExcel(@RequestParam MultipartFile excelFile) {
        ShiroUtils.checkPermission(getMenuCode() + ":create");
        getService().importExcel(excelFile);
        return Result.success();
    }

    /**
     * 将JSON格式的过滤条件转换为Java对象格式
     */
    protected List<FilterRuleHelper> parseFilterRules(String filterRules) {
        if (StringUtils.isBlank(filterRules) || "[]".equals(filterRules)) {
            return null;
        }
        return JSONUtils.readValue(filterRules, new TypeReference<>() {
        });
    }
}
