package sifca.shift.services;

import sifca.shift.repositories.UserRepository;
import sifca.shift.models.User;
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

    public void update(String oldPhone, String phone, String name)  { userRepository.update(oldPhone, phone, name); }

    public void delete(String phone){ userRepository.delete(phone);}

    public void create(String phone, String name){ userRepository.create(phone, name); }

    public boolean exists(String phone){ return userRepository.exists(phone); }
}
