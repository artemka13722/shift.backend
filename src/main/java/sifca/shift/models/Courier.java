package sifca.shift.models;

import io.swagger.annotations.ApiModelProperty;

public class Courier {
    @ApiModelProperty(value = "orderId взятого заказа")
    public Integer orderId;

    @ApiModelProperty(value = "Номер заказчика")
    public String courierPhone;

    @ApiModelProperty(value = "Статус заказа")
    public String status;

    public Courier() {
    }

    public Courier(Integer orderId, String courierPhone, String status) {
        this.orderId = orderId;
        this.courierPhone = courierPhone;
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
