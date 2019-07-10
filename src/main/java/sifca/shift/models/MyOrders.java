package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Time;
import java.util.Date;

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
    private Date deliveryDate;

    @ApiModelProperty(value = "Время и дата доставки заказа", required = true)
    private Date deliveryTime;

    @ApiModelProperty(value = "Адрес места, где находится вещь", required = true)
    private String fromAddress;

    @ApiModelProperty(value = "Адрес места, куда нужно доставить вещь", required = true)
    private String toAddress;

    @ApiModelProperty(value = "Номер телефона обратной стороны", required = true)
    private String phone;

    @ApiModelProperty(value = "Номер отдающего")
    private String outPhone;

    @ApiModelProperty(value = "Примечание", required = true)
    private String note;

    @ApiModelProperty(value = "Код доступа", required = true)
    private Integer access;


    public MyOrders(){
    }

    public MyOrders(Integer id, String title, String status, Integer price, String size, Date deliveryDate,
                    Date deliveryTime, String fromAddress, String toAddress, String phone, String outPhone, String note, Integer access) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.price = price;
        this.size = size;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.phone = phone;
        this.outPhone = outPhone;
        this.note = note;
        this.access = access;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOutPhone() {
        return outPhone;
    }

    public void setOutPhone(String outPhone) {
        this.outPhone = outPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }
}
