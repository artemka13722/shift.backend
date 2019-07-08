package sifca.shift.repositories;

import sifca.shift.models.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository {

    void create(Integer Id,
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

    Order getOrder(Integer OrderId);

    boolean exists(Integer id);

    void changeStatus(Integer id, char Status);

    Integer getIdOfLast();
}
