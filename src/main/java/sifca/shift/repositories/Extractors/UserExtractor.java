package sifca.shift.repositories.Extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import sifca.shift.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();

        // считывание из каждого поля пока есть строки в таблице
        while (rs.next()) {
            User user = new User();
            user.setName(rs.getString("name"));
            user.setPhone(rs.getString("orderPhone"));
            users.add(user);
        }
        return users;
    }
}
