package com.bqd.sparkjava.user;

import com.bqd.sparkjava.utils.Sql2oUtils;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class UserDao {

    private Sql2o sql2o;

    @Deprecated
    public UserDao(){
        this(Sql2oUtils.get());
    }

    public UserDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<User> list() {
        try (Connection conn = sql2o.beginTransaction()) {
            List<User> users = conn.createQuery("select * from User")
                    .executeAndFetch(User.class);
            return users;
        }
    }

    public User get(Long id) {
        try (Connection conn = sql2o.beginTransaction()) {
            User user = conn.createQuery("select * from User u where u.id=:id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(User.class);
            return user;
        }
    }

    public boolean delete(Long id) {
        try (Connection conn = sql2o.beginTransaction()) {
            int result = conn.createQuery("delete from User u where u.id=:id")
                    .addParameter("id", id)
                    .executeUpdate().getResult();
            conn.commit();
            return result>0;
        }
    }
}
