package sifca.shift.repositories.Extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.MyOrders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyOrdersExtractor implements ResultSetExtractor<List<MyOrders>>{
    @Override
    public List<MyOrders> extractData(ResultSet rs) throws SQLException, DataAccessException{
        List<MyOrders> orders = new ArrayList<>();

        while(rs.next()){
            MyOrders order = new MyOrders();
            order.setId(Integer.parseInt(rs.getString("OrderId")));
            order.setTitle(rs.getString("title"));
            order.setStatus(rs.getString("status"));
            order.setPrice(Integer.parseInt(rs.getString("price")));
            order.setSize(rs.getString("size"));
            order.setDeliveryDate(rs.getString("deliveryDate"));
            order.setDeliveryTime(rs.getString("deliveryTime"));
            order.setFromAddress(rs.getString("fromAddress"));
            order.setToAddress(rs.getString("toAddress"));
            order.setPhone(rs.getString("orderPhone"));
            order.setOutPhone("outPhone");
            order.setNote(rs.getString("note"));
            order.setAccess(Integer.parseInt(rs.getString("access")));
            orders.add(order);
        }
        return orders;
    }
}
