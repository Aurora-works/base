package org.aurora.base.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.aurora.base.common.CommonConstant;
import org.aurora.base.common.Result;
import org.aurora.base.common.dict.SysParam;
import org.aurora.base.common.dto.PersonalData;
import org.aurora.base.entity.sys.SysMenu;
import org.aurora.base.entity.sys.SysUser;
import org.aurora.base.jackson.JSONUtils;
import org.aurora.base.service.sys.SysParamService;
import org.aurora.base.service.sys.SysUserService;
import org.aurora.base.shiro.ShiroUtils;
import org.aurora.base.task.SystemMonitor;
import org.aurora.base.util.OSHIUtils;
import org.aurora.base.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Controller
@RequiresAuthentication
public class IndexController {
    @Autowired
    public IndexController(SysUserService userService, SysParamService paramService, RedisUtils redisUtils) {
        this.userService = userService;
        this.paramService = paramService;
        this.redisUtils = redisUtils;
    }

    private final SysUserService userService;
    private final SysParamService paramService;
    private final RedisUtils redisUtils;

    /**
     * 主页
     */
    @GetMapping(value = "/")
    public String index(Model model) {

        Long currentUserId = ShiroUtils.getCurrentUserId();

        TreeSet<SysMenu> menus = userService.getMenuTree(currentUserId);
        model.addAttribute("westMenuData", JSONUtils.writeValueAsString(menus));

        String defaultThemes = paramService.getValueByCode(SysParam.SYS_DEFAULT_THEMES.getCode());
        model.addAttribute("defaultThemes", defaultThemes);

        model.addAttribute("osStr", OSHIUtils.getOSVersionStr());
        model.addAttribute("systemBootTime", OSHIUtils.getSystemBootTime());
        model.addAttribute("systemUptime", OSHIUtils.getSystemUptime());
        model.addAttribute("cpuStr", OSHIUtils.getCpuStr());
        return "index";
    }

    /**
     * 系统监控
     */
    @GetMapping(value = "/systemMonitor")
    @ResponseBody
    public Map<String, Object> systemMonitor() {
        List<Object> list = redisUtils.lRange(CommonConstant.TASK_REDIS_KEY_SYSTEM_MONITOR, -60, -1);
        int len = list.size();

        Object[][] cpuLoadArr = new Object[len][2];
        Object[][] memoryInUseArr = new Object[len][2];

        String[] fileStoreNameArr = OSHIUtils.getFileStoreNameArr();
        int fsLen = fileStoreNameArr.length;

        Object[][][] spaceInUseArr = new Object[fsLen][len][2];

        SystemMonitor dto;
        String time;
        for (int i = 0; i < len; i++) {
            dto = (SystemMonitor) list.get(i);
            time = dto.getTime();
            cpuLoadArr[i][0] = time;
            cpuLoadArr[i][1] = dto.getCpuLoad();
            memoryInUseArr[i][0] = time;
            memoryInUseArr[i][1] = dto.getMemoryInUse();

            for (int j = 0; j < fsLen && j < dto.getSpaceInUse().length; j++) {
                spaceInUseArr[j][i][0] = time;
                spaceInUseArr[j][i][1] = dto.getSpaceInUse()[j];
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("cpuLoadArr", cpuLoadArr);
        result.put("memoryInUseArr", memoryInUseArr);
        result.put("fileStoreNameArr", fileStoreNameArr);
        result.put("spaceInUseArr", spaceInUseArr);
        return result;
    }

    /**
     * 个人资料
     */
    @GetMapping(value = "/personalData")
    @ResponseBody
    public PersonalData personalData() {
        SysUser user = userService.findById(ShiroUtils.getCurrentUserId());
        return new PersonalData(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getSex(),
                user.getEmail(),
                user.getMobilePhoneNumber(),
                user.getDescription(),
                "", "");
    }

    /**
     * 修改个人资料
     */
    @PostMapping(value = "/personalData")
    @ResponseBody
    public Result<Object> updatePersonalData(@Validated PersonalData data) {
        if (!ShiroUtils.getCurrentUserId().equals(data.id())) {
            throw new IllegalArgumentException();
        }
        userService.updatePersonalData(data);
        return Result.success();
    }
}
