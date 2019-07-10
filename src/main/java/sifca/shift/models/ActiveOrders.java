package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveOrders {

    @ApiModelProperty(value = "Имя заказа", required = true)
    private String title;

    @ApiModelProperty(value = "Цена заказа", required = true)
    private Integer price;

    @ApiModelProperty(value = "Размер вещи, которую нужно забрать", required = true)
    private String size;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private Date deliveryDate;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private Date deliveryTime;

    @ApiModelProperty(value = "Адрес места, где находится вещь", required = true)
    private String fromAddress;

    @ApiModelProperty(value = "Адрес места, куда нужно доставить вещь", required = true)
    private String toAddress;

    @ApiModelProperty(value = "Примечание", required = true)
    private String note;
}
