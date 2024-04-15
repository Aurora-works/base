package org.aurora.base.util.enums;

import lombok.Getter;

/**
 * 用户类型 - 系统留用 (对应系统用户表)
 */
@Getter
public enum TodoUser {
    /**
     * 未登录用户标识
     */
    USER_NO_LOGIN(2L);

    private final Long userId;

    TodoUser(Long userId) {
        this.userId = userId;
    }
}
