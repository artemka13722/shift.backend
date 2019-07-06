package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class GetActiveOrders {

    @ApiModelProperty(value = "Номер телефона заказчика")
    private String orderPhone;

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

    @ApiModelProperty(value = "Примечание", required = true)
    private String note;

    @ApiModelProperty(value = "Размер вещи, которую нужно забрать", required = true)
    private String size;

    public GetActiveOrders() {
    }

    public GetActiveOrders(String orderPhone, String fromAddress, String toAddress, Integer price, Date orderTime,
                 Date deliveryTime,String note, String size) {
        this.orderPhone = orderPhone;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.price = price;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.note = note;
        this.size = size;
    }
    public String getOrderPhone() { return orderPhone; }

    public void setOrderPhone(String orderPhone) { this.orderPhone = orderPhone; }

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSize() {return size; }

    public void setSize(String size) { this.size = size; }
}
