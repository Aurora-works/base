package org.aurora.base.util.reflect;

public class FieldUtils {

    /**
     * 按名称获取字段值
     */
    public static Object readDeclaredField(Object target, String fieldName) {
        try {
            return org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField(target, fieldName, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
