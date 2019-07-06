package ftc.shift.sample.repositories;

import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class OrderQueries implements OrderRepository{
    private Integer count = -1;
    Date date1, date2;
    public  List<Order> Orders = new ArrayList<>();
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    @Autowired
    public OrderQueries(){
        try{
            String stringDate="01/12/1995 17:30:20";
            String stringDate2="01/12/1995 17:50:20";
            date1 = sdf.parse(stringDate);
            date2 = sdf.parse(stringDate2);
        }catch(Exception e){
        }
        Orders.add(++count, new Order(count, "89130000000", "from", "to", 200, date1, date2, 'P', "lala", "small"));
        Orders.add(++count, new Order(count, "89131111111", "from", "to", 300, date1, date2, 'A', "lala", "small"));
    }

    @Override
    public List<Order> getAll(){
        if(Orders.isEmpty()){
            throw new NotFoundException();
        }
        return Orders;
    }

    @Override
    public Order create(Integer Id,
                        String orderPhone,
                        String fromAddress,
                        String toAddress,
                        Integer price,
                        Date orderTime,
                        Date deliveryTime,
                        char status,
                        String note,
                        String size){
        Order order = new Order(count, orderPhone, fromAddress, toAddress, price, orderTime,
                deliveryTime, status, note, size);
        order.setStatus('A');
        Orders.add(++count, order);
        return  Orders.get(count);
    }

    @Override
    public Integer getIdOfLast(){
        return count;
    }

    public Order getOrder(Integer OrderId){
        for (Order order : Orders){
            if (OrderId.equals(order.getId())){
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
    public void changeStatus(Integer id, char Status)
    {
        Order order = getOrder(id);
        order.setStatus(Status);
        Orders.set(id,order);
    }
}
