package sifca.shift.repositories;

import sifca.shift.models.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository {

    void create(Integer Id,
                 String title,
                 String orderPhone,
                 String fromAddress,
                 String toAddress,
                 String contactPhone,
                 Integer price,
                 Date deliveryDate,
                 Date deliveryTime,
                 String size,
                 String note
    );

    List<Order> getAll();

    Order getOrder(Integer OrderId);

    boolean exists(Integer id);

    void changeStatus(Integer id, String status);

    Integer getIdOfLast();
}
