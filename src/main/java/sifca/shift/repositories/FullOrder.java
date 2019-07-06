package ftc.shift.sample.repositories;
import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.Courier;
import ftc.shift.sample.models.GetActiveOrders;
import ftc.shift.sample.models.Order;
import ftc.shift.sample.models.GetMyOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FullOrder {
    List<Courier> couriers = new ArrayList<>();
    private Integer count = -1;
    @Autowired
    OrderQueries orderQueries = new OrderQueries();
    List<GetActiveOrders> orders = new ArrayList<>();

    @Autowired
    public FullOrder(){
        if (existAndActive(++count))
            couriers.add(count, new Courier(count, "89132222222", 'P'));
    }

    public void create(Integer OrderId, String CourierPhone, char Status){
        if (existAndActive(OrderId)){
            Courier courier = new Courier(OrderId, CourierPhone, Status);
            couriers.add(++count, courier);
            orderQueries.changeStatus(OrderId, 'P');
        }
        else
            throw new NotFoundException();
    }

    public boolean existAndActive(Integer id)
    {
        if (orderQueries.exists(id)){
            Order order = orderQueries.getOrder(id);
            if (order.getStatus() == 'A'){
                return true;
            }
        }
        return false;
    }

    public Courier getCourier(Integer Id){
        if (existAndActive(Id)){
            return couriers.get(Id);
        }
        else
        {
            throw new NotFoundException();
        }
    }

    public boolean isCustomer(Integer Id, String phone){
        if (orderQueries.exists(Id)){
            Order order = orderQueries.getOrder(Id);
            Courier courier = getCourier(Id);
            if (!phone.equals(courier.getCourierPhone()) && phone.equals(order.getOrderPhone())) {
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException();
    }

    public boolean isCourier(Integer Id, String phone){
        if (orderQueries.exists(Id)){
            Order order = orderQueries.getOrder(Id);
            Courier courier = getCourier(Id);
            if (phone.equals(courier.getCourierPhone()) && !phone.equals(order.getOrderPhone())){
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException();
    }

    public boolean existsCourier(Integer id){
        for (Courier courier : couriers){
            if (courier.getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void changeStatus(Integer id, char Status, String phone){
        if (Status == 'C' || Status == 'D') //Если переданный статус на закрытие или отмену
            for (Order order : orderQueries.Orders){
                if (order.getId().equals(id)){ // Если заказ существует
                    if (existsCourier(id)) // Если заказ уже принят курьером
                    {
                        Courier courier = getCourier(id); // Получаем этого курьера
                        courier.Status = Status; // Присваиваем ему переданный статус
                        couriers.set(id, courier); // меняем данные в List
                        if (isCourier(id, phone)) { // если переданный телефон - телефон курьера
                            orderQueries.changeStatus(id, 'A'); // меняем статус у заказа на активный
                        } else
                        if (isCustomer(id, phone)) { // если телефон - телефон заказчика
                            orderQueries.changeStatus(id, Status); // Меняем статус у заказа
                        }
                    }
                    else {
                        orderQueries.changeStatus(id, Status);
                    }
                }
            }
    }

    public List<GetMyOrders> getMyOrders(String phone){
        List<GetMyOrders> getMyOrders = new ArrayList<>();

        Integer index;
        Integer count = -1;
        for(index = 0 ; orderQueries.exists(index); ++index){
            System.out.println("\n\nIS CUSTOMER\n\n");
            if (isCustomer(index, phone)){

                Order order = orderQueries.getOrder(index);
                getMyOrders.add(++count, new GetMyOrders(
                        order.getOrderPhone(), order.getFromAddress(),
                        order.getToAddress(), order.getPrice(), order.getOrderTime(), order.getDeliveryTime(), 0,
                        order.getNote(), order.getSize(), order.getStatus()));
            }
            System.out.println("\n\nIS COURIER\n\n");
            if (isCourier(index, phone)){

                Order order = orderQueries.getOrder(index);
                getMyOrders.add(++count, new GetMyOrders(
                        getPhone(index), order.getFromAddress(),
                        order.getToAddress(), order.getPrice(), order.getOrderTime(), order.getDeliveryTime(), 1,
                        order.getNote(), order.getSize(), order.getStatus()));
            }
        }
        return getMyOrders;
    }

    public String getPhone(Integer Id){
        if (existsCourier(Id)){
            for(Courier courier: couriers){
                if(Id.equals(courier.ID)){
                    return courier.getCourierPhone();
                }
            }
        }
        throw new NotFoundException();
    }

    public List<GetActiveOrders> getActiveOrders(){
        Integer count = -1;
        System.out.println("\n\nIN GET ACTIVE ORDERS\n\n");
        for (Integer index = 0; orderQueries.exists(index); ++index){
            Order order = orderQueries.getOrder(index);
            if (order.getStatus() == 'A'){
                orders.add(++count, new GetActiveOrders(order.getOrderPhone(), order.getFromAddress(), order.getToAddress(),
                        order.getPrice(), order.getOrderTime(), order.getDeliveryTime(),
                        order.getNote(), order.getSize()));
            }
        }
        System.out.println("\n\nNEXT IS THE RETURN ORDERS\n\n");
        return orders;
    }

    public List<Courier> getAll(){
        if(couriers.isEmpty()){
            throw new NotFoundException();
        }
        return couriers;
    }

    public char getStatus(Integer OrderId, String phone){
        if (isCustomer(OrderId, phone)){
            return orderQueries.getOrder(OrderId).getStatus();
        }
        else
            if (isCourier(OrderId, phone)){
                return getCourier(OrderId).getStatus();
            }
        throw new NotFoundException();
    }
}
