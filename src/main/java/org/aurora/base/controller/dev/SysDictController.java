package org.aurora.base.controller.dev;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysDict;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysDictService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.util.Result;
import org.aurora.base.util.dto.TableFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/sys/dict", "/dev/dict"})
@RequiresAuthentication
public class SysDictController extends BaseController<SysDict> {
    @Autowired
    public SysDictController(SysDictService dictService) {
        this.dictService = dictService;
    }

    private final SysDictService dictService;

    @Override
    protected String getViewPath() {
        return "/dev/dict/dict_";
    }

    @Override
    protected String getMenuCode() {
        return "sys_dict";
    }

    @Override
    protected BaseService<SysDict> getService() {
        return dictService;
    }

    /**
     * 返回根据 dictCode, description 分组后的数据列表
     */
    @PostMapping(value = "/grid")
    @RequiresPermissions(value = "sys_table:read")
    @ResponseBody
    public List<TableFormatter> findAllGroupByCode() {
        return dictService.findAllGroupByCode();
    }

    /**
     * 获取下拉菜单数据列表
     */
    @PostMapping(value = "/combo/{dictCode}")
    @ResponseBody
    public List<SysDict> findByCode(@PathVariable String dictCode) {
        return dictService.findByCode(dictCode);
    }

    /**
     * 保存修改
     */
    @PostMapping(value = "/save")
    @RequiresPermissions(value = {"sys_dict:create", "sys_dict:update", "sys_dict:delete"}, logical = Logical.OR)
    @ResponseBody
    public Result<Object> save(@RequestBody Map<String, List<SysDict>> changes) {
        if (!ShiroUtils.isPermitted("sys_dict:create")) {
            changes.put("inserted", null);
        }
        if (!ShiroUtils.isPermitted("sys_dict:update")) {
            changes.put("updated", null);
        }
        if (!ShiroUtils.isPermitted("sys_dict:delete")) {
            changes.put("deleted", null);
        }
        dictService.save(changes);
        return Result.success();
    }
}
