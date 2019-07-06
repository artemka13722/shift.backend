package ftc.shift.sample.repositories;

import ftc.shift.sample.models.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository {

    Order create(Integer Id,
                 String orderPhone,
                 String fromAddress,
                 String toAddress,
                 Integer price,
                 Date orderTime,
                 Date deliveryTime,
                 char status,
                 String note,
                 String size
    );

    List<Order> getAll();

    Integer getIdOfLast();

    boolean exists(Integer id);

    void changeStatus(Integer id, char Status);
}
