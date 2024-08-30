package org.aurora.base.common.dict;

import lombok.Getter;

/**
 * 是否 (对应系统数据字典表)
 */
@Getter
public enum YesOrNo {
    /**
     * 是
     */
    YES("1"),
    /**
     * 否
     */
    NO("0");

    private final String key;

    YesOrNo(String key) {
        this.key = key;
    }
}
