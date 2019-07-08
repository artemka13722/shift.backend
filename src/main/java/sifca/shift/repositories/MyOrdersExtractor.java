package sifca.shift.repositories;

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
        Integer count = -1; // счетчик
        List<MyOrders> orders = new ArrayList<>();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); // Нужен для парсинга стринга в дату

        while(rs.next()){
            MyOrders order = new MyOrders();
            order.setPhone(rs.getString("orderPhone"));
            order.setFromAddress(rs.getString("fromAddress"));
            order.setToAdress(rs.getString("toAddress"));
            order.setPrice(Integer.parseInt(rs.getString("price")));
            try { // парсинг в дату жалуется, просит обработку экспешенов, без трай-кэтч не будет робить
                order.setOrderTime(sdf.parse(rs.getString("orderTime")));
                order.setDeliveryTime(sdf.parse(rs.getString("deliveryTime")));
            }
            catch (Exception e){
                throw new NotFoundException("Error MyOrder Extractor", 2);
            }
            order.setStatus(rs.getString("status")); //Взять первый символ из строки
            order.setAccess(Integer.parseInt(rs.getString("access")));
            order.setNote(rs.getString("note"));
            order.setSize(rs.getString("size"));
            orders.add(++count, order);
        }
        return new ArrayList<>(orders);
    }
}
