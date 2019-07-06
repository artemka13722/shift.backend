package ftc.shift.sample.models;

import io.swagger.annotations.ApiModelProperty;

public class Courier {
    @ApiModelProperty(value = "ID взятого заказа")
    public Integer ID;

    @ApiModelProperty(value = "Номер заказчика")
    public String CourierPhone;

    @ApiModelProperty(value = "Статус заказа")
    public char Status;

    public Courier(){
    }
    public Courier(Integer ID, String CourierPhone, char Status){
        this.ID = ID;
        this.CourierPhone = CourierPhone;
        this.Status = Status;
    }

    public Integer getID() { return ID; }

    public void setID(Integer ID) { this.ID = ID; }

    public String getCourierPhone() { return CourierPhone; }

    public void setCourierPhone(String CourierPhone) { this.CourierPhone = CourierPhone; }

    public char getStatus() { return Status; }

    public void setStatus(char Status) { this.Status = Status; }
}
