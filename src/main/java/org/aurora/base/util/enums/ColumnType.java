package org.aurora.base.util.enums;

import lombok.Getter;

/**
 * 数据类型 (对应系统数据字典表)
 */
@Getter
public enum ColumnType {

    INTEGER("Integer"),
    LONG("Long"),
    STRING("String"),
    LOCAL_DATE("LocalDate"),
    LOCAL_TIME("LocalTime"),
    LOCAL_DATE_TIME("LocalDateTime"),
    BOOLEAN("Boolean"),
    DOUBLE("Double"),
    BIG_DECIMAL("BigDecimal");

    private final String key;

    ColumnType(String key) {
        this.key = key;
    }
}
