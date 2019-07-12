package sifca.shift.repositories.DataBases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;
import sifca.shift.repositories.Extractors.OrderExtractor;
import sifca.shift.repositories.OrderRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "true")
public class OrderDatabase implements OrderRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    String OrderIdGenerator = "create sequence IF NOT EXISTS ORDERID_GENERATOR";

    @Autowired
    private OrderExtractor orderExtractor;

    @PostConstruct
    public void initialize() {
        String createOrderTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "OrderId int default OrderId_GENERATOR.nextval," +
                "Title nvarchar(120) NOT NULL," +
                "orderPhone varchar(11) NOT NULL," +
                "fromAddress nvarchar(150) NOT NULL," +
                "toAddress nvarchar(150) NOT NULL," +
                "contactPhone varchar(11) NOT NULL," +
                "price int NOT NULL," +
                "deliveryDate varchar(20) NOT NULL," +
                "deliveryTime varchar(20) NOT NULL," +
                "status nvarchar(15) NOT NULL," +
                "note nvarchar(255)," +
                "size nvarchar(30) NOT NULL" +
                ");";
        jdbcTemplate.update(OrderIdGenerator, new MapSqlParameterSource());
        jdbcTemplate.update(createOrderTable, new MapSqlParameterSource());
    }

    @Override
    public void create(Integer id,
                       String title,
                       String orderPhone,
                       String fromAddress,
                       String toAddress,
                       String contactPhone,
                       Integer price,
                       String deliveryDate,
                       String deliveryTime,
                       String status,
                       String note,
                       String size) {
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
    public List<Order> getAll() {
        String sqlGetAll = "SELECT * FROM orders;";
        List<Order> orders = jdbcTemplate.query(sqlGetAll, orderExtractor);
        if (orders.isEmpty())
            throw new NotFoundException("No orders");
        return orders;
    }

    @Override
    public Order getOrder(Integer OrderId) {
        String sql = "SELECT * FROM orders WHERE OrderId = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);

        if (orders.isEmpty()) {
            throw new NotFoundException("Error Get Order");
        }
        return orders.get(0);
    }

    @Override
    public boolean exists(Integer OrderId) {
        String sql = "SELECT * FROM orders WHERE OrderId = :OrderId;";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Order> orders = jdbcTemplate.query(sql, params, orderExtractor);
        if (orders.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void changeStatus(Integer orderId, String Status) {
        if (exists(orderId)) {
            String sql = "UPDATE orders SET status = :status WHERE OrderId=:OrderId;";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("OrderId", orderId)
                    .addValue("status", Status);
            jdbcTemplate.update(sql, params);
        } else
            throw new NotFoundException("Order does not tableExist");
    }

    @Override
    public Integer getIdOfLast() {
        String Sql = "SELECT * FROM ORDERS";
        List<Order> orders = jdbcTemplate.query(Sql, orderExtractor);
        return orders.size();
    }
}
