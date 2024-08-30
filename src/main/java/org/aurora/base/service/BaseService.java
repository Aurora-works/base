package org.aurora.base.service;

import org.aurora.base.common.dto.TableFormatter;
import org.aurora.base.common.view.FilterRuleHelper;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.entity.BaseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface BaseService<T extends BaseEntity> {

    T findById(Long id);

    void create(T entity);

    void silentCreate(T entity);

    void update(T entity);

    void delete(Long id);

    void delete(Long[] ids);

    List<T> findAll(String sort, String order);

    PageHelper<T> findAllInPage(String sort, String order);

    PageHelper<T> findAllInPage(int page, int size, String sort, String order, List<FilterRuleHelper> filterRules);

    List<TableFormatter> getFormatters();

    boolean uniqueValidate(String columnName, String value);

    String exportExcel(ByteArrayOutputStream stream, String sort, String order, List<FilterRuleHelper> filterRules);

    void importExcel(MultipartFile excelFile);
}
