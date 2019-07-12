package sifca.shift.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sifca.shift.models.Courier;
import sifca.shift.models.MyOrders;
import sifca.shift.models.Order;
import sifca.shift.repositories.OrderAndCourierRepository;

import java.util.List;

@Service
public class OrderAndCourierService {

    private final OrderAndCourierRepository orderAndCourierRepository;

    @Autowired
    public  OrderAndCourierService(OrderAndCourierRepository orderAndCourierRepository) {
        this.orderAndCourierRepository = orderAndCourierRepository;
    }

    public void create(Integer orderId, String courierPhone, String status){
        orderAndCourierRepository.create(orderId, courierPhone, status);
    }

    public boolean existAndActive(Integer orderId){
        return orderAndCourierRepository.existAndActive(orderId);
    }

    public Courier getCouier(Integer orderId){
        return orderAndCourierRepository.getCourier(orderId);
    }

    public boolean isCustomer(Integer orderId, String phone){
        return orderAndCourierRepository.isCustomer(orderId, phone);
    }

    public boolean isCourier(Integer orderId, String phone){
        return orderAndCourierRepository.isCourier(orderId, phone);
    }

    public boolean courierExists(Integer orderId){
        return orderAndCourierRepository.courierExists(orderId);
    }

    public void cancel(Integer orderId, String phone){
        orderAndCourierRepository.cancel(orderId, phone);
    }

    public void close(Integer orderId, String phone){
        orderAndCourierRepository.close(orderId, phone);
    }

    public List<MyOrders> getMyOrders(String phone){
        return orderAndCourierRepository.getMyOrders(phone);
    }

    public String getPhone(Integer Id){
        return orderAndCourierRepository.getPhone(Id);
    }

    public List<Order> getActiveOrders(String phone){
        return orderAndCourierRepository.getActiveOrders(phone);
    }

    public String getStatus(Integer orderId, String phone){
        return orderAndCourierRepository.getStatus(orderId, phone);
    }

    public List<Courier> getAll() { return  orderAndCourierRepository.getAll(); }
}
