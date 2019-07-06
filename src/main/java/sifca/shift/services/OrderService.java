package ftc.shift.sample.services;

import ftc.shift.sample.models.Order;
import ftc.shift.sample.repositories.OrderRepository;
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

    public Order create(Integer Id,
                        String fromAddress,
                        String phone,
                        String toAddress,
                        Integer price,
                        Date orderTime,
                        Date deliveryTime,
                        char status,
                        String note,
                        String size) {
        return orderRepository.create(Id, phone, fromAddress, toAddress, price, orderTime,
                deliveryTime, status, note, size);
    }

    public Integer getIdofLast() { return orderRepository.getIdOfLast(); }

    public void changeStatus(Integer Id, char Status) { orderRepository.changeStatus(Id, Status);}
}
