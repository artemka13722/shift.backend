package sifca.shift.repositories.DataBases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;

import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;
import sifca.shift.repositories.Extractors.OrderExtractor;
import sifca.shift.repositories.OrderRepository;

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

    Date date1, time1;
    DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat time = new SimpleDateFormat("hh:mm:ss");
    String OrderIdGenerator = "create sequence ORDERID_GENERATOR";

    @Autowired
    private OrderExtractor orderExtractor;

    @PostConstruct
    public void initialize(){
        String createOrderTable = "CREATE TABLE IF NOT EXISTS orders ("+
                "OrderId int default OrderId_GENERATOR.nextval," +
                "Title nvarchar(20) NOT NULL," +
                "orderPhone varchar(11) NOT NULL," +
                "fromAddress nvarchar(150) NOT NULL," +
                "toAddress nvarchar(150) NOT NULL," +
                "contactPhone varchar(11) NOT NULL," +
                "price int NOT NULL," +
                "deliveryDate Date NOT NULL," +
                "deliveryTime Time NOT NULL," +
                "status nvarchar(15) NOT NULL CHECK(status IN('Active', 'Done', " +
                "'Processing', 'Closed'))," +
                "note nvarchar(255)," +
                "size nvarchar(30) NOT NULL" +
                ");";
        try{
            String stringDate = "2018-07-10";
            String stringTime = "17:30:20";
            date1 = date.parse(stringDate);
            time1 = time.parse(stringTime);
        }catch(Exception e){
            throw new NotFoundException("Date is incorrect");
        }
        jdbcTemplate.update(OrderIdGenerator, new MapSqlParameterSource());
        jdbcTemplate.update(createOrderTable, new MapSqlParameterSource());
        //create(null, "BULKA", "89135890000", "dwadaw", "dwadaw", "89135890000", 100, date1, time1, "Processing", "dwaaw", "dwad");
    }

    @Override
    public void create(Integer id,
                       String title,
                       String orderPhone,
                       String fromAddress,
                       String toAddress,
                       String contactPhone,
                       Integer price,
                       Date deliveryDate,
                       Date deliveryTime,
                       String status,
                       String note,
                       String size){
        String SQLinsert = "INSERT INTO orders(title, orderphone, fromaddress, toaddress, contactphone," +
                "price, deliverydate, deliverytime, status, note, size) VALUES(:title, :orderPhone, " +
                ":fromAddress, :toAddress, :contactPhone, :price, " +
                ":deliveryDate, :deliveryTime, :status, :note, :size);";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("orderPhone", orderPhone)
                .addValue("fromAddress", fromAddress)
                .addValue("toAddress", toAddress)
                .addValue("contactPhone", contactPhone)
                .addValue("price", price)
                .addValue("deliveryDate", deliveryDate)
                .addValue("deliveryTime", deliveryTime)
                .addValue("status", "Active")
                .addValue("note", note)
                .addValue("size", size);

        jdbcTemplate.update(SQLinsert, params);
    }

    @Override
    public List<Order> getAll(){
        String sqlGetAll = "SELECT * FROM orders;";
        List<Order> orders = jdbcTemplate.query(sqlGetAll, orderExtractor);
        if (orders.isEmpty())
            throw new NotFoundException("No orders");
        return orders;
    }

    @Override
    public Order getOrder(Integer OrderId){
        String sql = "SELECT * FROM orders WHERE OrderId = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);

        if(orders.isEmpty()){
            throw new NotFoundException("Error Get Order");
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
    public void changeStatus(Integer orderId, String Status){
        if (exists(orderId)) {
            String sql = "UPDATE order SET status = :status WHERE OrderId=:OrderId;";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("OrderId", orderId)
                    .addValue("status", Status);
            jdbcTemplate.update(sql, params);
        }
        else
            throw new NotFoundException("Order does not exist");
    }

    @Override
    public Integer getIdOfLast(){
        String Sql = "SELECT * FROM ORDERS";
        List<Order> orders = jdbcTemplate.query(Sql, orderExtractor);
        return orders.size()-1;
    }
}
