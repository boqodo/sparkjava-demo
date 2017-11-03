package com.bqd.sparkjava.utils;

import org.sql2o.Sql2o;

public class Sql2oUtils {

    private static final ThreadLocal<Sql2o> sql2oThreadLocal = new ThreadLocal<>();

    public static Sql2o get() {
        return sql2oThreadLocal.get();
    }

    public static void set(Sql2o sql2o) {
        if (get() == null) {
            sql2oThreadLocal.set(sql2o);
        }
    }

    public static void destory() {
        sql2oThreadLocal.remove();
    }
}
