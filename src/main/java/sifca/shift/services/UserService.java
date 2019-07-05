package ftc.shift.sample.services;

import ftc.shift.sample.models.User;
import ftc.shift.sample.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll(){ return userRepository.getAll();    }

    public User getOne(String phone) { return userRepository.getOne(phone); }

    public User update(String oldPhone, String phone, String name)  { return  userRepository.update(oldPhone, phone, name); }

    public void delete(String phone){ userRepository.delete(phone);}

    public User create(String phone, String name){ return userRepository.create(phone, name); }
}
