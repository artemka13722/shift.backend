package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

public class User {
    @ApiModelProperty(value = "Номер телефона пользователя = уникальный идентификатор", required = true)
    public String phone;

    @ApiModelProperty(value = "Имя пользователя", required = true)
    public String name;

    public User(){
    }
    public User(String phone, String name) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
