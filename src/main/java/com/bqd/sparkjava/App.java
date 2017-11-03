package com.bqd.sparkjava;

import com.bqd.sparkjava.user.UserApi;
import com.bqd.sparkjava.user.UserDao;
import com.bqd.sparkjava.utils.InstanceFactory;
import com.bqd.sparkjava.utils.Sql2oUtils;
import com.p6spy.engine.spy.P6DataSource;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:mem:test";
        String userName = "sa";
        String password = "";
        DataSource dataSource = JdbcConnectionPool.create(url, userName, password);
        dataSource = new P6DataSource(dataSource);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();


        Sql2o sql2o = new Sql2o(dataSource);
        UserDao userDao = new UserDao(sql2o);
        InstanceFactory.addInstance(userDao);

        TemplateEngine templateEngine = new ThymeleafTemplateEngine();

        int maxThreads = 8;
        int minThreads = 2;
        int timeOutMillis = 30000;
        threadPool(maxThreads, minThreads, timeOutMillis);

        port(8080);
        staticFileLocation("/public");

        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        before((req, res) ->
                System.out.println(req.pathInfo()));
        path("/api", () -> {
            get("/users", UserApi.listUser);
            get("/users/:id", UserApi.getUser);
            delete("/users/:id", UserApi::delUser);
        });

        get("/login", (req, res) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("version", "1.0.0");
            return new ModelAndView(map, "login");
        }, templateEngine);

        notFound((req, res) -> req.pathInfo() + "页面不存在！");
        enableDebugScreen();
        awaitInitialization();
        Server.startWebServer(dataSource.getConnection());
    }
}
