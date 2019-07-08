package sifca.shift.repositories;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "false")
public class OrderInMemory implements OrderRepository{
    private Integer count = -1;
    Date date1, date2;
    public  List<Order> Orders = new ArrayList<>();
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Autowired
    public OrderInMemory(){
        try{
            String stringDate="01/12/1995 17:30:20";
            String stringDate2="01/12/1995 17:50:20";
            date1 = sdf.parse(stringDate);
            date2 = sdf.parse(stringDate2);
        }catch(Exception e){
        }
        Orders.add(++count, new Order(count, "89130000000", "from", "to", 200, date1, date2, "Processing", "lala", "small"));
        Orders.add(++count, new Order(count, "89131111111", "from", "to", 300, date1, date2, "Active", "lala", "small"));
    }

    // HAVE
    @Override
    public List<Order> getAll(){
        if(Orders.isEmpty()){
            throw new NotFoundException("Error getAll Order", 15);
        }
        return Orders;
    }

    // HAVE
    @Override
    public void create(Integer Id,
                        String orderPhone,
                        String fromAddress,
                        String toAddress,
                        Integer price,
                        Date orderTime,
                        Date deliveryTime,
                        String status,
                        String note,
                        String size){
        Order order = new Order(count, orderPhone, fromAddress, toAddress, price, orderTime,
                deliveryTime, status, note, size);
        order.setStatus("Active");
        Orders.add(++count, order);
    }

    @Override
    public Integer getIdOfLast(){
        return count;
    }

    @Override
    public Order getOrder(Integer orderId){
        for (Order order : Orders){
            if (orderId.equals(order.getId())){
                return order;
            }
        }
        return null;
    }

    @Override
    public boolean exists(Integer id){
        for (Order order : Orders) {
            if (order.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeStatus(Integer id, String status)
    {
        Order order = getOrder(id);
        order.setStatus(status);
        Orders.set(id, order);
    }
}
