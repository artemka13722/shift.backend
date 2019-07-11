package sifca.shift.repositories.InMemory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sifca.shift.repositories.OrderRepository;
import sifca.shift.services.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class OrderInMemory implements OrderRepository {
    private Integer count = -1;

    public  List<Order> Orders = new ArrayList<>();

    @Autowired
    UserService userService;

    @Autowired
    public OrderInMemory(){
    }

    @Override
    public List<Order> getAll(){
        if (Orders.isEmpty()){
            throw new NotFoundException("No orders");
        }
        return Orders;
    }

    @Override
    public void create(Integer id,
                       String title,
                       String orderPhone,
                       String fromAddress,
                       String toAddress,
                       String contactPhone,
                       Integer price,
                       String deliveryDate,
                       String deliveryTime,
                       String status,
                       String note,
                       String size){
        if (!exists(id) || !userService.exists(orderPhone)) {
            Order order = new Order(count, title, orderPhone, fromAddress, toAddress, contactPhone,price,
                    deliveryDate, deliveryTime, status, note, size);
            Orders.add(++count, order);
        }
        throw new NotFoundException("User does not tableExist or order is already tableExist");
    }

    @Override
    public Integer getIdOfLast(){
        return count;
    }

    @Override
    public Order getOrder(Integer orderId){
        for (Order order : Orders){
            if (orderId.equals(order.getId())){
                return order;
            }
        }
        throw new NotFoundException("Order with the id does not tableExist");
    }

    @Override
    public boolean exists(Integer id){
        for (Order order : Orders) {
            if (order.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeStatus(Integer id, String status)
    {
        if (exists(id)) {
            Order order = getOrder(id);
            order.setStatus(status);
            Orders.set(id, order);
        }
        else
            throw new NotFoundException("Order does not tableExist");
    }
}
