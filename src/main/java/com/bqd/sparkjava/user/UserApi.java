package com.bqd.sparkjava.user;

import com.bqd.sparkjava.utils.InstanceFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserApi {

    private static UserDao userDao = InstanceFactory.getInstance(UserDao.class);

    public static Route listUser = (req, res) -> userDao.list();

    public static Route getUser = (req, res) -> userDao.get(Long.valueOf(req.params("id")));

    public static Object delUser(Request req, Response res) {
        return userDao.delete(Long.valueOf(req.params("id")));
    }
}
