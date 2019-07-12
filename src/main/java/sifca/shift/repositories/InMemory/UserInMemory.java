package sifca.shift.repositories.InMemory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sifca.shift.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализиция, хранящая все данные в памяти приложения
 */

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class UserInMemory implements UserRepository {

    private Integer count = -1;
    private List<User> Users = new ArrayList<>();

    @Autowired
    public UserInMemory(){
        Users.add(++count, new User("89130000000", "Валя"));
        Users.add(++count, new User("89131111111", "Катя"));
        Users.add(++count, new User("89132222222", "Настя"));
        Users.add(++count, new User("89133333333", "Настя"));
        Users.add(++count, new User("89134444444", "Настя"));
    }

    @Override
    public List<User> getAll(){
        if (Users.isEmpty()){
            throw new NotFoundException("No users");
        }
        return Users;
    }

    @Override
    public User getOne(String phone){
        if (exists(phone)) {
            for (User user : Users) {
                if (user.getPhone().equals(phone))
                    return user;
            }
        }
        throw new NotFoundException("No user with the orderPhone number");
    }

    @Override
    public void update(String oldPhone, String phone, String name){
        if (!exists(phone) && exists(oldPhone)) {
            for (User user : Users) {
                if (user.getPhone().equals(oldPhone)) {
                    user.setPhone(phone);
                    user.setName(name);
                    break;
                }
            }
        }
        throw new NotFoundException("No user with the orderPhone number or user is already exists");
    }

    @Override
    public void delete(String phone){
        if (exists(phone)) {
            User delUser = new User();
            for (User user : Users) {
                if (user.getPhone().equals(phone)) {
                    delUser = user;
                    break;
                }
            }
            Users.remove(delUser);
        }
        throw new NotFoundException("User does not exists");
    }

    @Override
    public void create(String phone, String name){
        if (!exists(phone)) {
            Users.add(++count, new User(phone, name));
        }
        throw new NotFoundException("User exitsts");
    }

    @Override
    public boolean exists(String phone){
        if (!Users.isEmpty()){
            for (User user: Users){
                if (user.getPhone().equals(phone))
                    return true;
            }
            return false;
        }
        throw new NotFoundException("No users");
    }
}
