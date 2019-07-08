package sifca.shift.repositories;

import sifca.shift.models.Courier;
import sifca.shift.models.ActiveOrders;
import sifca.shift.models.MyOrders;

import java.util.List;

public interface OrderAndCourierRepository {

    void create(Integer OrderId, String CourierPhone, char Status);

    boolean existAndActive(Integer id);

    Courier getCourier(Integer Id);

    boolean isCustomer(Integer Id, String phone);

    boolean isCourier(Integer Id, String phone);

    boolean CourierExists(Integer id);

    void changeStatus(Integer id, char Status, String phone);

    List<MyOrders> getMyOrders(String phone);

    String getPhone(Integer Id);

    List<ActiveOrders> getActiveOrders();

    List<Courier> getAll();

    char getStatus(Integer OrderId, String phone);
}
