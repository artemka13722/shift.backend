package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class GetMyOrders {

    @ApiModelProperty(value = "Номер телефона заказчика/курьера", required = true)
    private String Phone;

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

    @ApiModelProperty(value = "Код доступа", required = true)
    private Integer access;

    @ApiModelProperty(value = "Примечание", required = true)
    private String note;

    @ApiModelProperty(value = "Размер вещи, которую нужно забрать", required = true)
    private String size;

    public GetMyOrders() {
    }

    public GetMyOrders(String Phone, String fromAddress, String toAddress, Integer price, Date orderTime,
                       Date deliveryTime, Integer access, String note, String size, char Status) {
        this.Phone = Phone;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.price = price;
        this.orderTime = orderTime;
        this.deliveryTime = deliveryTime;
        this.access = access;
        this.note = note;
        this.size = size;
        this.status = Status;
    }


    public char getStatus() {
        return status;
    }

    public void setStatus(char Status) {
        this.status = status;
    }

    public String getOrderPhone() { return Phone; }

    public void setOrderPhone(String Phone) { this.Phone = Phone; }

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

    public int getAccess() {
        return access;
    }

    public void setAccess(int Status) {
        this.access = access;
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
