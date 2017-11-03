package com.bqd.sparkjava.utils;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class InstanceFactory {

    private static final Map<String, Object> store = Collections.synchronizedMap(new WeakHashMap<>());

    public static void addInstance(Object instance) {
        assert instance != null;
        Class<?> clazz = instance.getClass();
        String key = clazz.getName();
        if (store.containsKey(key)) {
            throw new RuntimeException(String.format("已经存在%s的实例", key));
        } else {
            store.put(key, instance);
        }
    }

    public static <T> T getInstance(Class<T> clazz) {
        String key = clazz.getName();
        if (store.containsKey(key)) {
            Object instance = store.get(key);
            return (T) instance;
        } else {
            try {
                T t = clazz.newInstance();
                store.put(key, t);
                return t;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException(String.format("获取%s的实例失败！", key));
    }
}
