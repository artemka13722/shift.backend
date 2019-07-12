package sifca.shift.repositories;

import sifca.shift.models.Courier;
import sifca.shift.models.MyOrders;
import sifca.shift.models.Order;

import java.util.List;

public interface OrderAndCourierRepository {

    void create(Integer orderId, String courierPhone, String status);

    boolean existAndActive(Integer id);

    Courier getCourier(Integer id);

    boolean isCustomer(Integer id, String phone);

    boolean isCourier(Integer id, String phone);

    boolean courierExists(Integer id);

    void close(Integer id, String phone);

    void cancel(Integer id, String phone);

    List<MyOrders> getMyOrders(String phone);

    String getPhone(Integer Id);

    List<Order> getActiveOrders(String phone);

    List<Courier> getAll();

    String getStatus(Integer orderId, String phone);
}
