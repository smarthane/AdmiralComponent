package com.smarthane.admiral.component.common.sdk.util;

import com.smarthane.admiral.core.util.LogUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author smarthane
 * @time 2019/11/10 14:28
 * @describe 对象操作工具
 */
public class BeanUtils {

    private BeanUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static Map<String, Object> describe(final Object obj) {
        return describe(obj, null);
    }

    public static Map<String, Object> describe(final Object obj, final List<String> excludes) {
        final Map<String, Object> description = new HashMap<>();
        if (obj == null) {
            return description;
        }
        try {
            Class<?> clazz = obj.getClass();
            while (clazz != null) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (excludes != null
                            && !excludes.isEmpty()
                            && excludes.contains(field.getName())) {
                        continue;
                    }
                    field.setAccessible(true);
                    description.put(field.getName(), field.get(obj));
                }
                clazz = clazz.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            LogUtils.errorInfo(e.getMessage());
        }
        return description;
    }
}
