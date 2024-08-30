package org.aurora.base.controller.dev;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.common.Result;
import org.aurora.base.common.view.PageHelper;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysTable;
import org.aurora.base.entity.sys.SysTableColumn;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysTableColumnService;
import org.aurora.base.service.sys.SysTableService;
import org.aurora.base.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/sys/table", "/dev/table"})
@RequiresAuthentication
public class SysTableController extends BaseController<SysTable> {
    @Autowired
    public SysTableController(SysTableService tableService, SysTableColumnService columnService) {
        this.tableService = tableService;
        this.columnService = columnService;
    }

    private final SysTableService tableService;
    private final SysTableColumnService columnService;

    @Override
    protected String getViewPath() {
        return "/dev/table/table_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_table";
    }

    @Override
    protected BaseService<SysTable> getService() {
        return tableService;
    }

    /**
     * 窗口数据列表
     */
    @PostMapping(value = "/grid")
    @RequiresPermissions(value = {"sys_table:read", "sys_generate:read"}, logical = Logical.OR)
    @ResponseBody
    public List<SysTable> findAllInGrid() {
        return tableService.findAll("id", "asc");
    }

    /**
     * 保存修改
     */
    @PostMapping(value = "/save")
    @RequiresPermissions(value = {"sys_table:create", "sys_table:update", "sys_table:delete"}, logical = Logical.OR)
    @ResponseBody
    public Result<Object> save(@RequestBody Map<String, List<SysTable>> changes) {
        if (!ShiroUtils.isPermitted("sys_table:create")) {
            changes.put("inserted", null);
        }
        if (!ShiroUtils.isPermitted("sys_table:update")) {
            changes.put("updated", null);
        }
        if (!ShiroUtils.isPermitted("sys_table:delete")) {
            changes.put("deleted", null);
        }
        tableService.save(changes);
        return Result.success();
    }

    /**
     * 跳转到查看表单字段页面
     */
    @GetMapping(value = "/column/list")
    @RequiresPermissions("sys_table:read")
    public String tableColumns(Model model, @RequestParam(value = "tid") Long tableId) {
        model.addAttribute("tableId", tableId);
        return "/dev/table/table_column_list";
    }

    /**
     * 表单字段数据列表
     */
    @PostMapping(value = "/column/list")
    @RequiresPermissions("sys_table:read")
    @ResponseBody
    public PageHelper<SysTableColumn> getList(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "50") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "tid") Long tableId) {
        return columnService.findByTableId(page, size, sort, order, tableId);
    }

    /**
     * 保存修改 (表单字段)
     */
    @PostMapping(value = "/column/save")
    @RequiresPermissions("sys_table:update")
    @ResponseBody
    public Result<Object> saveColumns(@RequestBody Map<String, List<SysTableColumn>> changes) {
        columnService.save(changes);
        return Result.success();
    }
}
