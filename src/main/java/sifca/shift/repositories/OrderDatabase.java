package sifca.shift.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;

import sifca.shift.exception.modelsException.DatabaseException;
import sifca.shift.models.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "true")
public class OrderDatabase implements OrderRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private Integer count = -1;
    Date date1, date2;
    public List<Order> Orders = new ArrayList<>();
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    String OrderIdGenerator = "create sequence ORDER_ID_GENERATOR";

    @Autowired
    private OrderExtractor orderExtractor;

    @PostConstruct
    public void initialize(){
        String createOrderTable = "CREATE TABLE IF NOT EXISTS  orders ("+
                "OrderId int NOT NULL," +
                "orderPhone varchar(11) NOT NULL," +
                "fromAddress nvarchar(150) NOT NULL," +
                "toAddress nvarchar(150) NOT NULL," +
                "price int NOT NULL," +
                "orderTime DateTime NOT NULL," +
                "deliveryTime DateTime NOT NULL," +
                "status varchar(1) NOT NULL CHECK(status IN('A', 'D', 'F', 'P', 'W', 'C'))," +
                "note nvarchar(255)," +
                "size nvarchar(30) NOT NULL" +
                ");";
        jdbcTemplate.update(OrderIdGenerator, new MapSqlParameterSource());
        jdbcTemplate.update(createOrderTable, new MapSqlParameterSource());
    }

    public void create(Integer Id,
                       String orderPhone,
                       String fromAddress,
                       String toAddress,
                       Integer price,
                       Date orderTime,
                       Date deliveryTime,
                       char status,
                       String note,
                       String size){
        String SQLinsert = "INSERT INTO orders VALUES(:orderPhone," +
                ":fromAddress, :toAddress, :price, :orderTime, " +
                ":deliveryTime, :status, :note, :size";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("orderPhone", orderPhone)
                .addValue("fromAddress", fromAddress)
                .addValue("toAddress", toAddress)
                .addValue("price", price)
                .addValue("orderTime", orderTime)
                .addValue("deliveryTime", deliveryTime)
                .addValue("status", status)
                .addValue("note", note)
                .addValue("size", size);

        jdbcTemplate.update(SQLinsert, params);
    }

    @Override
    public List<Order> getAll(){
        String sqlGetAll = "SELECT * FROM orders;";
        return jdbcTemplate.query(sqlGetAll, orderExtractor);
    }

    @Override
    public Order getOrder(Integer OrderId){
        String sql = "SELECT * FROM orders WHERE Id = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);

        if(orders.isEmpty()){
            throw new DatabaseException();
        }
        return orders.get(0);
    }

    @Override
    public boolean exists(Integer OrderId){
        String sql = "SELECT * FROM orders WHERE Id = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);
        if (orders.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void changeStatus(Integer OrderId, char Status){
        String sql = "UPDATE order SET status = :status WHERE Id=:OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId)
                .addValue("status", Status);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public Integer getIdOfLast(){
        String Sql = "SELECT COUNT(Id) FROM orders;";
        return jdbcTemplate.query(Sql, orderExtractor).get(0).getId();
    }
}
