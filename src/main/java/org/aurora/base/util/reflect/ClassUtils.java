package org.aurora.base.util.reflect;

import java.lang.reflect.InvocationTargetException;

public class ClassUtils {

    public static <T> T newInstance(Class<T> entityClass) {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
