package ftc.shift.sample.api;

import ftc.shift.sample.models.User;
import ftc.shift.sample.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "Запросы для работы с пользователями")
public class UserController {
    private static final String USER_PATH = "api/v001/user";

    @Autowired
    private UserService UserService;

    @GetMapping(USER_PATH + "/getAll")
    @ApiOperation(value = "Получение всех пользователей")
    public ResponseEntity<List<User>> getAll(){
        List<User> users = UserService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(USER_PATH + "/get")
    @ApiOperation(value = "Получение информации о пользователе по номеру телефона")
    public User getOne(@RequestParam(value = "phone", required = true) String phone){
        return UserService.getOne(phone);
    }

    @PostMapping(USER_PATH + "/add")
    @ApiOperation(value = "Добавление нового пользователя")
    public ResponseEntity<User> create(
            @ApiParam(value = "Данные для нового пользователя (Номер телефона, имя, URL-адрес картинки)")
            @RequestBody User user){
        User result = UserService.create(user.phone, user.name);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(USER_PATH + "/update")
    @ApiOperation(value = "Обновление данных о пользователе")
    public ResponseEntity<User> update(
            @ApiParam(value = "Номер телефона пользователя, данные о котором нужно поменять")
            @RequestHeader("phone") String phone,
            @ApiParam(value = "Новые данные для пользователи (Номер телефона, имя, URL-адрес картинки)")
            @RequestBody User user) {
        User updatedUser = UserService.update(phone, user.phone, user.name);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(USER_PATH + "/delete/{phone}")
    @ApiParam(value = "Удаление существующего пользователя")
    public ResponseEntity<?> delete(
            @ApiParam(value = "Номер телефона пользователя, которого нужно удалить")
            @PathVariable String phone) {
        UserService.delete(phone);
        return ResponseEntity.ok().build();
    }
}
