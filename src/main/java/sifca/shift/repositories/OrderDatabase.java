package sifca.shift.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;

import sifca.shift.exception.NotFoundException;
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
    String OrderIdGenerator = "create sequence ORDERID_GENERATOR";

    @Autowired
    private OrderExtractor orderExtractor;

    @PostConstruct
    public void initialize(){
        String createOrderTable = "CREATE TABLE IF NOT EXISTS orders ("+
                "OrderId int default OrderId_GENERATOR.nextval," +
                "orderPhone varchar(11) NOT NULL," +
                "fromAddress nvarchar(150) NOT NULL," +
                "toAddress nvarchar(150) NOT NULL," +
                "price int NOT NULL," +
                "orderTime DateTime NOT NULL," +
                "deliveryTime DateTime NOT NULL," +
                "status nvarchar(15) NOT NULL CHECK(status IN('Active','Done', 'Failed', " +
                "'Processing', 'Waiting', 'Closed'))," +
                "note nvarchar(255)," +
                "size nvarchar(30) NOT NULL" +
                ");";
        try{
            String stringDate="01/12/1995 17:30:20";
            String stringDate2="01/12/1995 17:50:20";
            date1 = sdf.parse(stringDate);
            date2 = sdf.parse(stringDate2);
        }catch(Exception e){
        }
        jdbcTemplate.update(OrderIdGenerator, new MapSqlParameterSource());
        jdbcTemplate.update(createOrderTable, new MapSqlParameterSource());
        create(null, "89135890000", "dwadaw", "dwadaw", 100, date1, date2, "Active", "dwaaw", "dwad");
    }

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
        String SQLinsert = "INSERT INTO orders(orderPhone, fromAddress, toAddress, price, orderTime, deliveryTime, " +
                "status, note, size) VALUES(:orderPhone, " +
                ":fromAddress, :toAddress, :price, :orderTime, " +
                ":deliveryTime, :status, :note, :size);";
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
        String sql = "SELECT * FROM orders WHERE OrderId = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);

        if(orders.isEmpty()){
            throw new NotFoundException("Error Get Order", 13);
        }
        return orders.get(0);
    }

    @Override
    public boolean exists(Integer OrderId){
        String sql = "SELECT * FROM orders WHERE OrderId = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);
        if (orders.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public void changeStatus(Integer OrderId, String Status){
        String sql = "UPDATE order SET status = :status WHERE OrderId=:OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId)
                .addValue("status", Status);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public Integer getIdOfLast(){
        String Sql = "SELECT COUNT(OrderId) FROM orders;";
        return jdbcTemplate.query(Sql, orderExtractor).get(0).getId();
    }
}
