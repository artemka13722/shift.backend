package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courier {
    @ApiModelProperty(value = "orderId взятого заказа")
    public Integer orderId;

    @ApiModelProperty(value = "Номер заказчика")
    public String courierPhone;

    @ApiModelProperty(value = "Статус заказа")
    public String status;
}
