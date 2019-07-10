package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Time;
import java.util.Date;

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

    public ActiveOrders() {
    }


    public ActiveOrders(String title, Integer price, String size, Date deliveryDate, Date deliveryTime,
                        String fromAddress, String toAddress, String note) {
        this.title = title;
        this.price = price;
        this.size = size;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
