package sifca.shift.services;

import sifca.shift.models.Order;
import sifca.shift.repositories.OrderRepository;
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

    public Order getOne(Integer ID) { return orderRepository.getOne(ID); }

    public Order create(String fromAddress,
                        String toAddress,
                        Integer price,
                        Date orderTime,
                        Date deliveryTime,
                        char Status,
                        String Note) {
        return orderRepository.create(fromAddress, toAddress, price, orderTime,
                deliveryTime, Status, Note);
    }
}
