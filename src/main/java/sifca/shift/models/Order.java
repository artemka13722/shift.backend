package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;
import org.apache.tomcat.jni.Time;

import java.util.Date;
import java.util.List;

public class Order {
    @ApiModelProperty(value = "Уникальный идентификатор заказа", required = true)
    private Integer id;

    @ApiModelProperty(value = "Адрес места, где находится вещь", required = true)
    private String fromAddress;

    @ApiModelProperty(value = "Адрес места, куда нужно доставить вещь", required = true)
    private String toAddress;

    @ApiModelProperty(value = "Цена заказа", required = true)
    private Integer price;

    @ApiModelProperty(value = "Время и дата заказа", required = true)
    private Date orderTime;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private Date deliveryTime;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private char status;

    @ApiModelProperty(value = "Примечание", required = true)
    public String note;

    public Order() {
    }

    public Order(Integer id, String fromAddress, String toAddress, Integer price, Date orderTime,
                 Date deliveryTime, char status) {
        this.id = id;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.price = price;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAdress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char Status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
