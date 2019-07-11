package sifca.shift.repositories.DataBases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.User;
import sifca.shift.repositories.Extractors.UserExtractor;
import sifca.shift.repositories.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "true")
public class UserDatabase implements UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private UserExtractor userExtractor;

    @PostConstruct
    public void initialize() {
        String createUserTableSql = "create table if not exists USERS (" +
                "phone  VARCHAR(11) UNIQUE NOT NULL," +
                "name   VARCHAR(20) NOT NULL" +
                ");";


        jdbcTemplate.update(createUserTableSql, new MapSqlParameterSource());
    }

    @Override
    public User getOne(String phone) {
        if (exists(phone)) {
            String getUserSql = "SELECT phone, name from users where phone=:phone";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("phone", phone);
            List<User> users = jdbcTemplate.query(getUserSql, params, userExtractor);
            return users.get(0);
        }
        throw new NotFoundException("User does not tableExist"); // user is already exists
    }


    @Override
    public void update(String oldPhone, String phone, String name) {
        if (exists(oldPhone) && !exists(phone)) {
            String sqlUpdate = "UPDATE users set name=:name , phone=:phone where phone=:oldPhone";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("phone", phone)
                    .addValue("oldPhone", oldPhone)
                    .addValue("name", name);
            jdbcTemplate.update(sqlUpdate, params);
        }
        else
            throw new NotFoundException("User does not tableExist"); // user does not tableExist
    }

    @Override
    public void delete(String phone) {
        if (exists(phone)) {
            String deleteUserSql = "delete from users where phone =:phone";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("phone", phone);
            jdbcTemplate.update(deleteUserSql, params);
        }
        else
            throw new NotFoundException("User does not tableExist"); // user does not tableExist
    }

    @Override
    public void create(String phone, String name) {
        if (!exists(phone)) {
            String createUserSql = "insert into Users (Phone, Name) values (:phone, :name)";
            MapSqlParameterSource userParams = new MapSqlParameterSource()
                    .addValue("name", name)
                    .addValue("phone", phone);
            jdbcTemplate.update(createUserSql, userParams);
        }
        else
            throw new NotFoundException("User is already tableExist"); // user does not tableExist
    }

    @Override
    public List<User> getAll() {
        String sqlAll = "select * from users";
        List<User> users = jdbcTemplate.query(sqlAll, userExtractor);
        if (!users.isEmpty()) {
            return users;
        }
        throw new NotFoundException("No users"); // Have no users
    }

    @Override
    public boolean exists(String phone){
        String sql = "SELECT * FROM users WHERE phone=:phone;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone", phone);
        List<User> users = jdbcTemplate.query(sql, param, userExtractor);
        if (!users.isEmpty())
            return true;
        return false;
    }
}
