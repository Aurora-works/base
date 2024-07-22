package org.aurora.base.util.enums;

import lombok.Getter;

/**
 * 系统参数编码 (对应系统参数表)
 */
@Getter
public enum SysParam {
    /**
     * 系统默认密码
     */
    SYS_DEFAULT_PASSWORD("SYS_DEFAULT_PASSWORD"),
    /**
     * 系统默认主题
     */
    SYS_DEFAULT_THEMES("SYS_DEFAULT_THEMES"),
    /**
     * 默认添加字段
     */
    SYS_DEFAULT_TABLE_COLUMN("SYS_DEFAULT_TABLE_COLUMN"),
    /**
     * 代码生成目录
     */
    SYS_GENERATE_PATH("SYS_GENERATE_PATH"),
    /**
     * 项目名称
     */
    SYS_PROJECT_NAME("SYS_PROJECT_NAME");

    private final String code;

    SysParam(String code) {
        this.code = code;
    }
}
