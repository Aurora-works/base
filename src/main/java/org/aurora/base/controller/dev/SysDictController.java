package org.aurora.base.controller.dev;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.controller.BaseController;
import org.aurora.base.entity.sys.SysDict;
import org.aurora.base.service.BaseService;
import org.aurora.base.service.sys.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
     * 获取下拉菜单数据列表
     */
    @PostMapping(value = "/combo/{dictCode}")
    @ResponseBody
    public List<SysDict> findByCode(@PathVariable String dictCode) {
        return dictService.findByCode(dictCode);
    }
}
