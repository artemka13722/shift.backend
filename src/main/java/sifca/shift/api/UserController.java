package sifca.shift.api;

import sifca.shift.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "Запросы для работы с пользователями/Queries for work with users")
public class UserController {
    private static final String USER_PATH = "api/v001/user";

    @Autowired
    private sifca.shift.services.UserService UserService;

    @GetMapping(USER_PATH + "/getAll")
    @ApiOperation(value = "Получение всех пользователей/Adding all users")
    public ResponseEntity<List<User>> getAll(){
        List<User> users = UserService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(USER_PATH + "/get")
    @ApiOperation(value = "Получение информации о пользователе по номеру телефона" +
            "/Getting all information(only name, yes) about the user having the phone number")
    public User getOne(@RequestParam(value = "phone", required = true) String phone){
        return UserService.getOne(phone);
    }

    @PostMapping(USER_PATH + "/add")
    @ApiOperation(value = "Добавление нового пользователя/Adding a new user")
    public ResponseEntity<?> create(
            @ApiParam(value = "Данные для нового пользователя (Номер телефона, имя)/" +
                    "Some data for a new user(phone number, name")
            @RequestBody User user){
        UserService.create(user.getPhone(), user.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(USER_PATH + "/update")
    @ApiOperation(value = "Обновление данных о пользователе/Updating data about a user")
    public ResponseEntity<?> update(
            @ApiParam(value = "Номер телефона пользователя, данные о котором нужно поменять/" +
                    "User's phone number which data needs to change")
            @RequestHeader("phone") String phone,
            @ApiParam(value = "Новые данные для пользователи (Номер телефона, имя)/" +
                    "New data for the user(phone number, name")
            @RequestBody User user) {
        UserService.update(phone, user.getPhone(), user.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(USER_PATH + "/delete/{phone}")
    @ApiParam(value = "Удаление существующего пользователя/" +
            "Deleting existing user")
    public ResponseEntity<?> delete(
            @ApiParam(value = "Номер телефона пользователя, которого нужно удалить/" +
                    "User's phone number which needs to delete")
            @PathVariable String phone) {
        UserService.delete(phone);
        return ResponseEntity.ok().build();
    }
}
