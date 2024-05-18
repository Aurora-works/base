package org.aurora.base.util.reflect;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanInfoUtils {

    /**
     * 获取属性值
     */
    public static Object getPropertyValue(Object obj, String propertyName, Class<?> beanClass) {
        Method method = getReadMethod(propertyName, beanClass);
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入属性值
     */
    public static void setPropertyValue(Object obj, Object value, String propertyName, Class<?> beanClass) {
        Method method = getWriteMethod(propertyName, beanClass);
        try {
            method.invoke(obj, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取读取属性值的方法
     */
    public static Method getReadMethod(String propertyName, Class<?> beanClass) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, beanClass);
            return descriptor.getReadMethod();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取写入属性值的方法
     */
    public static Method getWriteMethod(String propertyName, Class<?> beanClass) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, beanClass);
            return descriptor.getWriteMethod();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
