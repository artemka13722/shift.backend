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
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Нужен для парсинга стринга в дату
        DateFormat time = new SimpleDateFormat("hh:mm:ss");

        while(rs.next()){
            MyOrders order = new MyOrders();
            order.setId(Integer.parseInt(rs.getString("id")));
            order.setTitle(rs.getString("title"));
            order.setStatus(rs.getString("status"));
            order.setPrice(Integer.parseInt(rs.getString("price")));
            order.setSize(rs.getString("size"));
            try {
                order.setDeliveryDate(time.parse(rs.getString("deliveryDate")));
                order.setDeliveryTime(sdf.parse(rs.getString("deliveryTime")));
            }
            catch (Exception e){
                throw new NotFoundException("Date is incorrect");
            }
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
