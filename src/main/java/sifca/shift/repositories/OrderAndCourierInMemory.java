package sifca.shift.repositories;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.ActiveOrders;
import sifca.shift.models.Courier;
import sifca.shift.models.MyOrders;
import sifca.shift.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class OrderAndCourierInMemory implements OrderAndCourierRepository {
    List<Courier> couriers = new ArrayList<>();
    private Integer count = -1;
    @Autowired
    OrderInMemory orderInMemory = new OrderInMemory();
    List<ActiveOrders> orders = new ArrayList<>();

    @Autowired
    public OrderAndCourierInMemory(){
        if (existAndActive(++count))
            couriers.add(count, new Courier(count, "89132222222", "Processing"));
    }

    @Override
    public void create(Integer orderId, String courierPhone, String Status){
        if (existAndActive(orderId)){
            Courier courier = new Courier(orderId, courierPhone, Status);
            couriers.add(++count, courier);
            orderInMemory.changeStatus(orderId, "Processing");
        }
        else
            throw new NotFoundException("Error create Order", 9);
    }

    @Override
    public boolean existAndActive(Integer id)
    {
        if (orderInMemory.exists(id)){
            Order order = orderInMemory.getOrder(id);
            if (order.getStatus().equals("Active")){
                return true;
            }
            return false;
        }
        throw new NotFoundException("Error existAndActive", 10);
    }

    @Override
    public Courier getCourier(Integer id){
        if (existAndActive(id)){
            return couriers.get(id);
        }
        else
        {
            throw new NotFoundException("Error getCourier", 11);
        }
    }

    @Override
    public boolean isCustomer(Integer id, String phone){
        if (orderInMemory.exists(id)){
            Order order = orderInMemory.getOrder(id);
            Courier courier = getCourier(id);
            if (!phone.equals(courier.getCourierPhone()) && phone.equals(order.getOrderPhone())) {
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException("Error isCustomer", 4);
    }

    @Override
    public boolean isCourier(Integer id, String phone){
        if (orderInMemory.exists(id)){
            Order order = orderInMemory.getOrder(id);
            Courier courier = getCourier(id);
            if (phone.equals(courier.getCourierPhone()) && !phone.equals(order.getOrderPhone())){
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException("Error isCourier", 5);
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
    public void changeStatus(Integer id, String Status, String phone) {
        if ((!isCustomer(id, phone) && !isCourier(id, phone)    // Если это не заказчик, и не курьер или
                || (!Status.equals("Closed") &&
                !Status.equals("Done"))))                       // запрос не на отмену или закрытие заказа
            throw new NotFoundException("Error changeStatus", 6);                      // вернуть ошибку
        else {
            if (Status.equals("Closed") &&
                    isCustomer(id, phone)) {                    // Если запрос на отмену, и это заказчик
                if (!courierExists(id)) {                       // Если у заказа есть курьер
                    Courier courier = getCourier(id);           // Получаем этого курьера
                    courier.status = Status;                    // Присваиваем ему переданный статус
                    couriers.set(id, courier);                  // меняем данные в List
                }
                orderInMemory.changeStatus(id, Status);          // Меняем статус заказа на отмененный
            } else {                                            // Если это курьер
                if (Status.equals("Done")) {                            // Если запрос на закрытие
                    orderInMemory.changeStatus(id, Status);      // Закрытие заказа со стороны заказчика
                }
                else {                                          // Если запрос на отмену
                    orderInMemory.changeStatus(id, "Active");         // Статус заказа меняется на активный
                    // changeStatus(orderId, status, phone);            // Закрытие заказа со стороны курьера
                    Courier courier = getCourier(id);           // Получаем этого курьера
                    courier.status = Status;                    // Присваиваем ему переданный статус
                    couriers.set(id, courier);
                }
            }
        }
    }


    @Override
    public List<MyOrders> getMyOrders(String phone){
        List<MyOrders> myOrders = new ArrayList<>();

        Integer index;
        Integer count = -1;
        for(index = 0 ; orderInMemory.exists(index); ++index){
            if (isCustomer(index, phone)){

                Order order = orderInMemory.getOrder(index);
                myOrders.add(++count, new MyOrders(
                        order.getOrderPhone(), order.getFromAddress(),
                        order.getToAddress(), order.getPrice(), order.getOrderTime(), order.getDeliveryTime(), 0,
                        order.getNote(), order.getSize(), order.getStatus()));
            }
            if (isCourier(index, phone)){

                Order order = orderInMemory.getOrder(index);
                myOrders.add(++count, new MyOrders(
                        getPhone(index), order.getFromAddress(),
                        order.getToAddress(), order.getPrice(), order.getOrderTime(), order.getDeliveryTime(), 1,
                        order.getNote(), order.getSize(), order.getStatus()));
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
        throw new NotFoundException("Error getPhone courier", 7);
    }

    @Override
    public List<ActiveOrders> getActiveOrders(){
        Integer count = -1;
        for (Integer index = 0; orderInMemory.exists(index); ++index){
            Order order = orderInMemory.getOrder(index);
            if (order.getStatus().equals("Active")){
                orders.add(++count, new ActiveOrders(order.getOrderPhone(), order.getFromAddress(), order.getToAddress(),
                        order.getPrice(), order.getOrderTime(), order.getDeliveryTime(),
                        order.getNote(), order.getSize()));
            }
        }
        return orders;
    }

    @Override
    public List<Courier> getAll(){
        if(couriers.isEmpty()){
            throw new NotFoundException("Error getAll couriers", 12);
        }
        return couriers;
    }

    @Override
    public String getStatus(Integer orderId, String phone){
        if (isCustomer(orderId, phone)){
            return orderInMemory.getOrder(orderId).getStatus();
        }
        if (isCourier(orderId, phone)){
            return getCourier(orderId).getStatus();
        }
        throw new NotFoundException("Error getStatus Ordder or Courier", 8);
    }
}
