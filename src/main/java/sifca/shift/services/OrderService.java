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
                        char status,
                        String note,
                        String size) {
        orderRepository.create(Id, phone, fromAddress, toAddress, price, orderTime,
                deliveryTime, status, note, size);
    }

    public Order getOrder(Integer OrderId) { return orderRepository.getOrder(OrderId); }

    public void changeStatus(Integer Id, char Status) { orderRepository.changeStatus(Id, Status);}

    public Integer getIdOfLast() { return orderRepository.getIdOfLast(); }

    public boolean exists(Integer Id) { return  orderRepository.exists(Id);}
}
