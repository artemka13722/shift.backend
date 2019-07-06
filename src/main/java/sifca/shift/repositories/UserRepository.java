package ftc.shift.sample.repositories;

import ftc.shift.sample.models.User;

import java.util.List;

/**
 * Интерфейс для получения данных юзеров
 */
public interface UserRepository {

    User getOne(String phone);

    User update(String oldPhone, String phone, String name);

    void delete(String phone);

    User create(String phone, String name);

    List<User> getAll();
}
