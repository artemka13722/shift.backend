package sifca.shift.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sifca.shift.models.Courier;
import sifca.shift.models.ActiveOrders;
import sifca.shift.models.MyOrders;
import sifca.shift.repositories.OrderAndCourierRepository;

import java.util.List;

@Service
public class OrderAndCourierService {

    private final OrderAndCourierRepository OACR;

    @Autowired
    public  OrderAndCourierService(OrderAndCourierRepository OACR) {
        this.OACR = OACR;
    }

    public void create(Integer OrderId, String CourierPhone, char Status){
        OACR.create(OrderId, CourierPhone, Status);
    }

    public boolean existAndActive(Integer OrderId){
        return OACR.existAndActive(OrderId);
    }

    public Courier getCouier(Integer OrderId){
        return OACR.getCourier(OrderId);
    }

    public boolean isCustomer(Integer OrderId, String phone){
        return OACR.isCustomer(OrderId, phone);
    }

    public boolean isCourier(Integer OrderId, String phone){
        return OACR.isCourier(OrderId, phone);
    }

    public boolean CourierExists(Integer OrderId){
        return OACR.CourierExists(OrderId);
    }

    public void changeStatus(Integer OrderId, char Status, String phone){
        OACR.changeStatus(OrderId, Status, phone);
    }

    public List<MyOrders> getMyOrders(String phone){
        return OACR.getMyOrders(phone);
    }

    public String getPhone(Integer Id){
        return OACR.getPhone(Id);
    }

    public List<ActiveOrders> getActiveOrders(){
        return OACR.getActiveOrders();
    }

    public char getStatus(Integer OrderId, String Phone){
        return OACR.getStatus(OrderId, Phone);
    }

    public List<Courier> getAll() { return  OACR.getAll(); }
}
