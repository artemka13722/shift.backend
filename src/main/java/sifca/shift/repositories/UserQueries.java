package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализиция, хранящая все данные в памяти приложения
 */
@Repository
public class UserQueries implements UserRepository{

    private Integer count = -1;
    private List<User> Users = new ArrayList<>();

    @Autowired
    public UserQueries(){
        Users.add(++count, new User("89130000000", "Валя"));
        Users.add(++count, new User("89131111111", "Катя"));
        Users.add(++count, new User("89132222222", "Настя"));
        Users.add(++count, new User("89133333333", "Настя"));
        Users.add(++count, new User("89134444444", "Настя"));
    }

    @Override
    public List<User> getAll(){
        if (Users.isEmpty()){
            throw new NotFoundException();
        }
        return Users;
    }

    @Override
    public User getOne(String phone){
        for (User user : Users){
            if (user.phone.equals(phone))
                return user;
        }
        throw new NotFoundException();
    }

    @Override
    public User update(String oldPhone, String phone, String name){
        for (User user : Users){
            if (user.phone.equals(oldPhone))
            {
                user.phone = phone;
                user.name = name;
                return user;
            }
        }
        throw new NotFoundException();
    }

    @Override
    public void delete(String phone){
        boolean key = false;
        User deletingUser = null;
        for (User user : Users){
            if (user.phone.equals(phone))
            {
                key = true;
                deletingUser = user;
                break;
            }
        }

        if (!key)
            throw new NotFoundException();
        else
        {
            --count;
            Users.remove(deletingUser);
        }
    }

    @Override
    public User create(String phone, String name){
        Users.add(++count, new User(phone, name));
        return Users.get(count);
    }
}