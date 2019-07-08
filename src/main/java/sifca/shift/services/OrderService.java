package sifca.shift.services;

import sifca.shift.repositories.OrderRepository;
import sifca.shift.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) { this.orderRepository = orderRepository; }

    public List<Order> getAll() { return orderRepository.getAll(); }

    public void create(Integer Id,
                        String fromAddress,
                        String phone,
                        String toAddress,
                        Integer price,
                        Date orderTime,
                        Date deliveryTime,
                        String status,
                        String note,
                        String size) {
        orderRepository.create(Id, phone, fromAddress, toAddress, price, orderTime,
                deliveryTime, status, note, size);
    }

    public Order getOrder(Integer orderId) { return orderRepository.getOrder(orderId); }

    public void changeStatus(Integer id, String status) { orderRepository.changeStatus(id, status);}

    public Integer getIdOfLast() { return orderRepository.getIdOfLast(); }

    public boolean exists(Integer id) { return  orderRepository.exists(id);}
}
