package org.aurora.base.util.enums;

import lombok.Getter;

/**
 * 用户类型 (对应系统数据字典表)
 */
@Getter
public enum UserType {
    /**
     * 系统管理员
     */
    ADMIN("ADMIN"),
    /**
     * 普通用户
     */
    USER("USER"),
    /**
     * 系统留用
     */
    TODO("TODO");

    private final String key;

    UserType(String key) {
        this.key = key;
    }
}
