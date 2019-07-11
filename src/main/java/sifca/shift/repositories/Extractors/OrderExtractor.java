package sifca.shift.repositories.Extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderExtractor implements ResultSetExtractor<List<Order>>{
    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException{
        List<Order> orders = new ArrayList<>();

        while(rs.next()){
            Order order = new Order();
            order.setId(Integer.parseInt(rs.getString("OrderId")));
            order.setTitle(rs.getString("Title"));
            order.setOrderPhone(rs.getString("orderPhone"));
            order.setFromAddress(rs.getString("fromAddress"));
            order.setToAddress(rs.getString("toAddress"));
            order.setContactPhone(rs.getString("contactPhone"));
            order.setPrice(Integer.parseInt(rs.getString("price")));
            order.setDeliveryDate(rs.getString("deliveryDate"));
            order.setDeliveryTime(rs.getString("deliveryTime"));
            order.setStatus(rs.getString("status")); //Взять первый символ из строки
            order.setNote(rs.getString("note"));
            order.setSize(rs.getString("size"));
            orders.add(order);
        }
        return orders;
    }
}
