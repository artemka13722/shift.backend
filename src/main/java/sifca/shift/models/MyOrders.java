package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrders {

    @ApiModelProperty(value = "Идентификатор  заказа", required = true)
    private Integer id;

    @ApiModelProperty(value = "Имя заказа", required = true)
    private String title;

    @ApiModelProperty(value = "Статус заказа", required = true)
    private String status;

    @ApiModelProperty(value = "Цена заказа", required = true)
    private Integer price;

    @ApiModelProperty(value = "Размер вещи, которую нужно забрать", required = true)
    private String size;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private String deliveryDate;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private String deliveryTime;

    @ApiModelProperty(value = "Адрес места, где находится вещь", required = true)
    private String fromAddress;

    @ApiModelProperty(value = "Адрес места, куда нужно доставить вещь", required = true)
    private String toAddress;

    @ApiModelProperty(value = "Номер телефона обратной стороны", required = true)
    private String phone;

    @ApiModelProperty(value = "Номер отдающего")
    private String contactPhone;

    @ApiModelProperty(value = "Примечание", required = true)
    private String note;

    @ApiModelProperty(value = "Код доступа", required = true)
    private Integer access;
}
