package ftc.shift.sample.repositories;
import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.Courier;
import ftc.shift.sample.models.Order;
import ftc.shift.sample.repositories.OrderQueries;
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

    @Autowired
    public FullOrder(){
        if (existAndActive(++count))
            couriers.add(count, new Courier(count, "89132222222", 'P'));
        if (existAndActive(++count))
            couriers.add(count, new Courier(count, "89133333333", 'P'));
    }

    public Courier create(Integer OrderId, String CourierPhone, char Status){
        if (existAndActive(OrderId)){
            Courier courier = new Courier(OrderId, CourierPhone, Status);
            couriers.add(++count, courier);
            return couriers.get(count);
        }
        else
            throw new NotFoundException();
    }

    public boolean existAndActive(Integer id)
    {
        System.out.println("\n\nSTARTING EXIST&ACTIVE\n\n");
        if (orderQueries.exists(id)){
            Order order = orderQueries.getOrder(id);
            if (order.getStatus() == 'A'){
                return true;
            }
        }
        System.out.println("\n\nENDING EXIST&ACTIVE\n\n");
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
            if (order.getStatus() == 'P'){
                Courier courier = getCourier(Id);
                if (!phone.equals(courier.getCourierPhone()) && phone.equals(order.getOrderPhone())) {
                    return true;
                }
                else
                    return false;
            }
        }
        throw new NotFoundException();
    }

    public boolean isCourier(Integer Id, String phone){
        if (orderQueries.exists(Id)){
            Order order = orderQueries.getOrder(Id);
            if (order.getStatus() == 'P'){
                Courier courier = getCourier(Id);
                if (phone.equals(courier.getCourierPhone()) && !phone.equals(order.getOrderPhone())){
                    return true;
                }
                else
                    return false;
            }
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
}
