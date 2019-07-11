package sifca.shift.repositories;

import sifca.shift.models.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository {

    void create(Integer id,
                 String title,
                 String orderPhone,
                 String fromAddress,
                 String toAddress,
                 String contactPhone,
                 Integer price,
                 String deliveryDate,
                 String deliveryTime,
                 String status,
                 String size,
                 String note
    );

    List<Order> getAll();

    Order getOrder(Integer OrderId);

    boolean exists(Integer id);

    void changeStatus(Integer id, String status);

    Integer getIdOfLast();
}
