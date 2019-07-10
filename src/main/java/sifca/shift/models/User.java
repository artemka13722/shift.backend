package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @ApiModelProperty(value = "Номер телефона пользователя = уникальный идентификатор", required = true)
    private String phone;

    @ApiModelProperty(value = "Имя пользователя", required = true)
    private String name;
}
