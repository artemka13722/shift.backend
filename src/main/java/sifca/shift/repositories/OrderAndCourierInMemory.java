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
            couriers.add(count, new Courier(count, "89132222222", 'P'));
    }

    @Override
    public void create(Integer OrderId, String CourierPhone, char Status){
        if (existAndActive(OrderId)){
            Courier courier = new Courier(OrderId, CourierPhone, Status);
            couriers.add(++count, courier);
            orderInMemory.changeStatus(OrderId, 'P');
        }
        else
            throw new NotFoundException();
    }

    @Override
    public boolean existAndActive(Integer id)
    {
        if (orderInMemory.exists(id)){
            Order order = orderInMemory.getOrder(id);
            if (order.getStatus() == 'A'){
                return true;
            }
            return false;
        }
        throw new NotFoundException();
    }

    @Override
    public Courier getCourier(Integer Id){
        if (existAndActive(Id)){
            return couriers.get(Id);
        }
        else
        {
            throw new NotFoundException();
        }
    }

    @Override
    public boolean isCustomer(Integer Id, String phone){
        if (orderInMemory.exists(Id)){
            Order order = orderInMemory.getOrder(Id);
            Courier courier = getCourier(Id);
            if (!phone.equals(courier.getCourierPhone()) && phone.equals(order.getOrderPhone())) {
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException();
    }

    @Override
    public boolean isCourier(Integer Id, String phone){
        if (orderInMemory.exists(Id)){
            Order order = orderInMemory.getOrder(Id);
            Courier courier = getCourier(Id);
            if (phone.equals(courier.getCourierPhone()) && !phone.equals(order.getOrderPhone())){
                return true;
            }
            else
                return false;
        }
        throw new NotFoundException();
    }

    @Override
    public boolean CourierExists(Integer id){
        for (Courier courier : couriers){
            if (courier.getID().equals(id)){
                return true;
            }
        }
        return false;
    }

    /// CHANGE, try to make this more easy and clearer
    @Override
    public void changeStatus(Integer id, char Status, String phone) {
        if ((!isCustomer(id, phone) && !isCourier(id, phone)    // Если это не заказчик, и не курьер или
                || (Status != 'C' && Status != 'D')))           // запрос не на отмену или закрытие заказа
            throw new NotFoundException();                      // вернуть ошибку
        else {
            if (Status == 'C' && isCustomer(id, phone)) {       // Если запрос на отмену, и это заказчик
                if (!CourierExists(id)) {                       // Если у заказа есть курьер
                    Courier courier = getCourier(id);           // Получаем этого курьера
                    courier.Status = Status;                    // Присваиваем ему переданный статус
                    couriers.set(id, courier);                  // меняем данные в List
                }
                orderInMemory.changeStatus(id, Status);          // Меняем статус заказа на отмененный
            } else {                                            // Если это курьер
                if (Status == 'D') {                            // Если запрос на закрытие
                    orderInMemory.changeStatus(id, Status);      // Закрытие заказа со стороны заказчика
                }
                else {                                          // Если запрос на отмену
                    orderInMemory.changeStatus(id, 'A');         // Статус заказа меняется на активный
                   // changeStatus(id, Status, phone);            // Закрытие заказа со стороны курьера
                    Courier courier = getCourier(id);           // Получаем этого курьера
                    courier.Status = Status;                    // Присваиваем ему переданный статус
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
        if (CourierExists(Id)){
            for(Courier courier: couriers){
                if(Id.equals(courier.ID)){
                    return courier.getCourierPhone();
                }
            }
        }
        throw new NotFoundException();
    }

    @Override
    public List<ActiveOrders> getActiveOrders(){
        Integer count = -1;
        for (Integer index = 0; orderInMemory.exists(index); ++index){
            Order order = orderInMemory.getOrder(index);
            if (order.getStatus() == 'A'){
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
            throw new NotFoundException();
        }
        return couriers;
    }

    @Override
    public char getStatus(Integer OrderId, String phone){
        if (isCustomer(OrderId, phone)){
            return orderInMemory.getOrder(OrderId).getStatus();
        }
        if (isCourier(OrderId, phone)){
            return getCourier(OrderId).getStatus();
        }
        throw new NotFoundException();
    }
}
