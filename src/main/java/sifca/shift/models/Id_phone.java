package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Id_phone {

    @ApiModelProperty(value = " Идентификатор заказа", required = true)
    private Integer id;

    @ApiModelProperty(value = "Номер телефона", required = true)
    private String phone;
}
