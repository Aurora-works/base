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
     * 默认添加字段
     */
    SYS_DEFAULT_TABLE_COLUMN("SYS_DEFAULT_TABLE_COLUMN");

    private final String code;

    SysParam(String code) {
        this.code = code;
    }
}
