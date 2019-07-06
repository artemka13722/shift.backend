package sifca.shift.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import sifca.shift.models.User;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "true")
public class DatabaseUser implements UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private UserExtractor userExtractor;

    @PostConstruct
    public void initialize(){
        String createUserTableSql = "create table if not exists USERS (" +
                "PHONE  VARCHAR(11)," +
                "NAME     VARCHAR(20)," +
                "IMAGE   VARCHAR(50)," +
                ");";

        jdbcTemplate.update(createUserTableSql, new MapSqlParameterSource());
        //create("89515769680", "Tester", "https://pp.userapi.com/");
    }

    @Override
    // http://localhost:8081/api/v001/user/get?phone=89515769680
    public User getOne(String phone) {
        String getUserSql = "select Phone, Name, Image from users where phone=:phone";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("phone", phone);

        List<User> users = jdbcTemplate.query(getUserSql, params, userExtractor);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }


    @Override
    public User update(String oldPhone, String phone, String name) {

        String sqlUpdate = "UPDATE users set name=:name , phone=:phone where phone=:oldPhone";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("phone", phone)
                .addValue("oldPhone", oldPhone)
                .addValue("name", name);

        jdbcTemplate.update(sqlUpdate, params);
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        return user;
    }

    @Override
    public void delete(String phone) {
        String deleteUserSql = "delete from users where phone =:phone";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("phone", phone);
        jdbcTemplate.update(deleteUserSql, params);
    }

    @Override
    public User create(String phone, String name) {
        String createUserSql = "insert into Users (Phone, Name) values (:phone, :name)";

        User user = new User();
        user.setName(name);
        user.setPhone(phone);

        MapSqlParameterSource userParams = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("phone", user.getPhone());

        jdbcTemplate.update(createUserSql, userParams);
        return user;


    }

    @Override

    public List<User> getAll() {

        String sqlAll = "select * from users";
        List<User> users = jdbcTemplate.query(sqlAll, userExtractor);
        return users;
    }
}
