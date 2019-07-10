package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Time;
import java.util.Date;

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

    public Order() {
    }

    public Order(Integer id, String title, String orderPhone, String fromAddress, String toAddress,
                 String contactPhone, Integer price, Date deliveryDate, Date deliveryTime, String note, String size) {
        this.id = id;
        this.title = title;
        this.orderPhone = orderPhone;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.contactPhone = contactPhone;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
        this.status = "Active";
        this.note = note;
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
