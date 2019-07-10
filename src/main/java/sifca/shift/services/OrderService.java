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
                        String title,
                        String orderPhone,
                        String fromAddress,
                        String toAddress,
                        String contactPhone,
                        Integer price,
                        Date deliveryDate,
                        Date deliveryTime,
                        String note,
                        String size) {
        orderRepository.create(Id, title, orderPhone, fromAddress, toAddress, contactPhone, price,
                deliveryDate, deliveryTime, note, size);
    }

    public Order getOrder(Integer orderId) { return orderRepository.getOrder(orderId); }

    public Integer getIdOfLast() { return orderRepository.getIdOfLast(); }

    public boolean exists(Integer id) { return  orderRepository.exists(id);}

    public void changeStatus(Integer orderId, String status) {
        orderRepository.changeStatus(orderId, status); }
}
