package sifca.shift.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderExtractor implements ResultSetExtractor<List<Order>>{
    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException{
        Integer count = -1; // счетчик
        List<Order> orders = new ArrayList<>();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); // Нужен для парсинга стринга в дату

        while(rs.next()){
            Order order = new Order();
            order.setId(Integer.parseInt(rs.getString("Id")));
            order.setOrderPhone(rs.getString("orderPhone"));
            order.setFromAddress(rs.getString("fromAddress"));
            order.setToAdress(rs.getString("toAddress"));
            order.setPrice(Integer.parseInt(rs.getString("price")));
            try { // парсинг в дату жалуется, просит обработку экспешенов, без трай-кэтч не будет робить
                order.setOrderTime(sdf.parse(rs.getString("orderTime")));
                order.setDeliveryTime(sdf.parse(rs.getString("deliveryTime")));
            }
            catch (Exception e){
                throw new NotFoundException();
            }
            order.setStatus(rs.getString("status").charAt(0)); //Взять первый символ из строки
            order.setNote(rs.getString("note"));
            order.setSize(rs.getString("size"));
        }
        return new ArrayList<>(orders);
    }
}
