package sifca.shift.repositories.InMemory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.ActiveOrders;
import sifca.shift.models.Courier;
import sifca.shift.models.MyOrders;
import sifca.shift.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sifca.shift.repositories.OrderAndCourierRepository;
import sifca.shift.services.OrderService;
import sifca.shift.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class OrderAndCourierInMemory implements OrderAndCourierRepository {
    List<Courier> couriers = new ArrayList<>();
    private Integer count = -1;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    List<ActiveOrders> orders = new ArrayList<>();

    @Autowired
    public OrderAndCourierInMemory(){
        if (existAndActive(++count))
            couriers.add(count, new Courier(count, "89132222222", "Processing"));
    }

    @Override
    public void create(Integer orderId, String courierPhone, String Status){
        if (existAndActive(orderId) && !courierExists(orderId) && userService.exists(courierPhone)){
            Courier courier = new Courier(orderId, courierPhone, Status);
            couriers.add(++count, courier);
            orderService.changeStatus(orderId, "Processing");
        }
        else
            throw new NotFoundException("Order isn't active or the order does not exists or courier is already exist");
    }

    @Override
    public boolean existAndActive(Integer id)
    {
        if (orderService.exists(id)){
            Order order = orderService.getOrder(id);
            if (order.getStatus().equals("Active")){
                return true;
            }
            return false;
        }
        throw new NotFoundException("Order does not exist");
    }

    @Override
    public Courier getCourier(Integer id){
        if (courierExists(id)){
            return couriers.get(id);
        }
        throw new NotFoundException("Courier does not exist");
    }

    @Override
    public boolean isCustomer(Integer id, String phone){
        if (orderService.exists(id)){
            Order order = orderService.getOrder(id);
            Courier courier = getCourier(id);
            if (!phone.equals(courier.getCourierPhone()) && phone.equals(order.getOrderPhone())) {
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException("Order does not exist");
    }

    @Override
    public boolean isCourier(Integer id, String phone){
        if (orderService.exists(id)){
            Order order = orderService.getOrder(id);
            Courier courier = getCourier(id);
            if (phone.equals(courier.getCourierPhone()) && !phone.equals(order.getOrderPhone())){
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException("Order does not exist");
    }

    @Override
    public boolean courierExists(Integer id){
        for (Courier courier : couriers){
            if (courier.getOrderId().equals(id)){
                return true;
            }
        }
        return false;
    }

    /// CHANGE, try to make this more easy and clearer
    @Override
    public void changeStatus(Integer id, String phone) {
        if (orderService.exists(id)) {
            if (isCustomer(id, phone)) {
                if (orderService.getOrder(id).getStatus().equals("Active")
                        || orderService.getOrder(id).getStatus().equals("Processing")) {
                    if (courierExists(id)) {
                        Courier courier = getCourier(id);
                        courier.setStatus("Closed");
                        couriers.set(id, courier);
                    }
                    orderService.changeStatus(id, "Closed");
                }
            }
            else
            {
                if (getCourier(id).getStatus().equals("Processing")){
                    orderService.changeStatus(id, "Active");
                    Courier courier = getCourier(id);
                    courier.setStatus("Closed");
                    couriers.set(id, courier);
                }
            }
        }
        else
            throw new NotFoundException("Order does not exist or the phone number is incorrect");
    }

    @Override
    public List<MyOrders> getMyOrders(String phone){
        List<MyOrders> myOrders = new ArrayList<>();

        Integer index;
        Integer count = -1;
        for(index = 0 ; orderService.exists(index); ++index){
            if (isCustomer(index, phone)){

                Order order = orderService.getOrder(index);
                myOrders.add(++count, new MyOrders(
                        order.getId(), order.getTitle(), order.getStatus(), order.getPrice(),order.getSize(),
                        order.getDeliveryDate(), order.getDeliveryTime(), order.getFromAddress(), order.getToAddress(),
                        order.getOrderPhone(), order.getContactPhone(), order.getNote(), 0));
            }
            if (isCourier(index, phone)){

                Order order = orderService.getOrder(index);
                myOrders.add(++count, new MyOrders(
                        order.getId(), order.getTitle(), order.getStatus(), order.getPrice(),order.getSize(),
                        order.getDeliveryDate(), order.getDeliveryTime(), order.getFromAddress(), order.getToAddress(),
                        order.getOrderPhone(), order.getContactPhone(), order.getNote(), 1));
            }
        }
        return myOrders;
    }

    @Override
    public String getPhone(Integer Id){
        if (courierExists(Id)){
            for(Courier courier: couriers){
                if(Id.equals(courier.orderId)){
                    return courier.getCourierPhone();
                }
            }
        }
        throw new NotFoundException("Courier does not exist");
    }

    @Override
    public List<ActiveOrders> getActiveOrders(){
        Integer count = -1;
        for (Integer index = 0; orderService.exists(index); ++index){
            Order order = orderService.getOrder(index);
            if (order.getStatus().equals("Active")){
                orders.add(++count, new ActiveOrders(order.getTitle(), order.getPrice(), order.getSize(),
                        order.getDeliveryDate(), order.getDeliveryTime(), order.getFromAddress(),
                        order.getToAddress(), order.getNote()));
            }
        }
        return orders;
    }

    @Override
    public List<Courier> getAll(){
        if(couriers.isEmpty()){
            throw new NotFoundException("No couriers");
        }
        return couriers;
    }

    @Override
    public String getStatus(Integer orderId, String phone){
        if (isCustomer(orderId, phone)){
            return orderService.getOrder(orderId).getStatus();
        }
        if (isCourier(orderId, phone)){
            return getCourier(orderId).getStatus();
        }
        throw new NotFoundException("Order does not exist or the phone number is incorrect");
    }
}
