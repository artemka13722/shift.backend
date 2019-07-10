package sifca.shift.repositories.Extractors;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.ActiveOrders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActiveOrdersExtractor implements ResultSetExtractor<List<ActiveOrders>>{
    @Override
    public List<ActiveOrders> extractData(ResultSet rs) throws SQLException, DataAccessException{
        List<ActiveOrders> orders = new ArrayList<>();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd"); // Нужен для парсинга стринга в дату
        DateFormat time = new SimpleDateFormat("hh:mm:ss"); // Нужен для парсинга стринга в time
        while(rs.next()){
            ActiveOrders order = new ActiveOrders();
            order.setTitle(rs.getString("title"));
            order.setPrice(Integer.parseInt(rs.getString("price")));
            order.setSize(rs.getString("size"));
            try { // парсинг в дату жалуется, просит обработку экспешенов, без трай-кэтч не будет робить
                order.setDeliveryDate(date.parse(rs.getString("deliveryDate")));
                order.setDeliveryTime(time.parse(rs.getString("deliveryTime")));
            }
            catch (Exception e){
                throw new NotFoundException();
            }
            order.setFromAddress(rs.getString("fromAddress"));
            order.setToAddress(rs.getString("toAddress"));
            order.setNote(rs.getString("note"));
            orders.add(order);
        }
        return orders;
    }
}
