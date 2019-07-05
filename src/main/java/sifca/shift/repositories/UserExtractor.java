package sifca.shift.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import sifca.shift.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String, User> users = new HashMap<>();

        while (rs.next()) {
            String phone = rs.getString("phone");

            User user;
            if (users.containsKey(phone)) {
                user = users.get(phone);
            } else {

                user = new User();

                user.setName(rs.getString("name"));
                user.setPhone(rs.getString("phone"));
                user.setImage(rs.getString("image"));

                users.put(phone, user);
            }
        }
        return new ArrayList<>(users.values());
    }
}
