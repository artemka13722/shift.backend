package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @ApiModelProperty(value = "Уникальный идентификатор заказа", required = true)
    private Integer id;

    @ApiModelProperty(value = "Имя заказа", required = true)
    private String title;

    @ApiModelProperty(value = "Номер телефона заказчика")
    private String orderPhone;

    @ApiModelProperty(value = "Адрес места, где находится вещь", required = true)
    private String fromAddress;

    @ApiModelProperty(value = "Адрес места, куда нужно доставить вещь", required = true)
    private String toAddress;

    @ApiModelProperty(value = "Контактный номер")
    private String contactPhone;

    @ApiModelProperty(value = "Цена заказа", required = true)
    private Integer price;

    @ApiModelProperty(value = "Дата доставки заказа", required = true)
    private Date deliveryDate;

    @ApiModelProperty(value = "Время доставки заказа", required = true)
    private Date deliveryTime;

    @ApiModelProperty(value = "Статус заказа", required = true)
    private String status;

    @ApiModelProperty(value = "Примечание", required = true)
    private String note;

    @ApiModelProperty(value = "Размер вещи, которую нужно забрать", required = true)
    private String size;
}
