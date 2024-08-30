package org.aurora.base.common.dict;

import lombok.Getter;

/**
 * 系统数据状态 (对应系统数据字典表)
 */
@Getter
public enum Status {
    /**
     * 启用
     */
    ENABLED("1"),
    /**
     * 停用
     */
    DISABLED("0");

    private final String key;

    Status(String key) {
        this.key = key;
    }
}
