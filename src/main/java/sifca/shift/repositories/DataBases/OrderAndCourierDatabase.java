package sifca.shift.repositories.DataBases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Courier;
import sifca.shift.models.Order;
import sifca.shift.models.MyOrders;
import sifca.shift.repositories.Extractors.CourierExtractor;
import sifca.shift.repositories.Extractors.MyOrdersExtractor;
import sifca.shift.repositories.Extractors.OrderExtractor;
import sifca.shift.repositories.OrderAndCourierRepository;
import sifca.shift.services.OrderService;
import sifca.shift.services.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "true")
public class OrderAndCourierDatabase implements OrderAndCourierRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    private CourierExtractor courierExtractor;

    @Autowired
    private OrderExtractor orderExtractor;

    @Autowired
    private MyOrdersExtractor myOrdersExtractor;

    @PostConstruct
    public void initialize() {
        String createTable = "CREATE TABLE IF NOT EXISTS Couriers(" +
                "OrderId int UNIQUE NOT NULL," +
                "CourierPhone nvarchar(11) NOT NULL," +
                "Status varchar(15) NOT NULL" +
                ");";
        jdbcTemplate.update(createTable, new MapSqlParameterSource());
    }

    @Override
    public boolean existAndActive(Integer OrderId){
        if (orderService.exists(OrderId)){
            Order order = orderService.getOrder(OrderId);
            if (order.getStatus().equals("Активный")){
                return true;
            }
            return false;
        }
        throw new NotFoundException("Order does not exist");
    }

    @Override
    public void create(Integer orderId, String courierPhone, String status){
        if (existAndActive(orderId) && !courierExists(orderId) && userService.exists(courierPhone)){
            String SqlInsert = "INSERT INTO Couriers VALUES(:orderId, :courierPhone, 'В процессе');";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("orderId", orderId)
                    .addValue("courierPhone", courierPhone);
            jdbcTemplate.update(SqlInsert, param);
            orderService.changeStatus(orderId, "В процессе");
        }
        else
            throw new NotFoundException("The order is already  exist or courier for this order is already exist " +
                "or user with the orderPhone number does not exist");
    }

    @Override
    public Courier getCourier(Integer id){
        if (courierExists(id)) {
            String sql = "SELECT * FROM couriers WHERE OrderId = :OrderId;";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", id);
            List<Courier> couriers = jdbcTemplate.query(sql, param, courierExtractor);
            return couriers.get(0);
        }
        throw new NotFoundException("Courier with the id does not exist");
    }

    @Override
    public boolean isCustomer(Integer id, String phone){
        if (orderService.exists(id)){
            String sql = "SELECT * FROM orders " +
                    "WHERE orders.OrderId = :OrderId AND orderphone = :phone";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", id)
                    .addValue("phone", phone);
            List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
            if (orders.isEmpty())
                return false;
            return true;
        }
        throw new NotFoundException("Order does not exist");
    }

    @Override
    public boolean isCourier(Integer id, String phone){
        if (orderService.exists(id)){
            String sql = "SELECT * FROM orders " +
                    "JOIN Couriers ON orders.OrderId = Couriers.OrderId " +
                    "WHERE Orders.OrderId = :OrderId AND couriers.CourierPhone = :phone;";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", id)
                    .addValue("phone", phone);
            List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
            if (orders.isEmpty())
                return false;
            return true;
        }
        throw new NotFoundException("Order does not exist");
    }

    @Override
    public boolean courierExists(Integer OrderId){
        String sql = "SELECT * FROM couriers WHERE OrderId = :OrderId;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Courier> couriers = jdbcTemplate.query(sql, param, courierExtractor);
        if (couriers.isEmpty())
            return false;
        return true;
    }

    @Override
    public void cancel(Integer id, String phone) {
        if (isCustomer(id, phone)) {
            if (orderService.getOrder(id).getStatus().equals("Активный")
                    || orderService.getOrder(id).getStatus().equals("В процессе")) {
                if (courierExists(id)) {
                    String sql = "UPDATE couriers SET status = 'Отменен' " +
                            "WHERE orderId = :Id;";
                    MapSqlParameterSource param = new MapSqlParameterSource()
                            .addValue("Id", id);
                    jdbcTemplate.update(sql, param);
                }
                orderService.changeStatus(id, "Отменен");
            }
        } else if (isCourier(id, phone)) {
            String sql = "UPDATE couriers SET status = 'Отменен' " +
                    "WHERE orderId = :Id;";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("Id", id);
            jdbcTemplate.update(sql, param);
            orderService.changeStatus(id, "Активный");
        } else
            throw new NotFoundException("Order does not exist or access  error(wrong orderPhone number) " +
                    "or the order isn't active");
    }

    @Override
    public void close(Integer id, String phone) {
        if (isCourier(id, phone)) {
            if (orderService.getOrder(id).getStatus().equals("Processing")) {
                orderService.changeStatus(id, "Завершен");
                String sql = "UPDATE couriers SET status = 'Звершен' " +
                        "WHERE orderId = :Id;";
                MapSqlParameterSource param = new MapSqlParameterSource()
                        .addValue("Id", id);
                jdbcTemplate.update(sql, param);
            }
        }
        else
            throw new NotFoundException("Order does not exist or access error");
    }

    public boolean courierExist(String phone){
        String sql = "SELECT * FROM orders " +
                "JOIN couriers ON couriers.OrderId = orders.OrderId " +
                "WHERE orders.orderPhone = :phone";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone", phone);
        List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
        if (orders.isEmpty())
            return false;
        return true;
    }

    @Override
    public List<MyOrders> getMyOrders(String phone){
        String sql;
        List<MyOrders> myOrders = new ArrayList<>();
        // ADDING AS A CUSTOMER
        if (courierExist(phone)) {
            sql = "SELECT orders.orderId, title, orders.status, price, size, deliveryDate, deliveryTime, fromAddress," +
                    "toAddress, courierPhone as orderPhone, contactPhone, note, 0 as access FROM Orders " +
                    "JOIN couriers ON orders.OrderId = couriers.OrderId " +
                    "WHERE orderPhone = :phone;";
        }
        else
        {
            sql = "SELECT orders.orderId, title, orders.status, price, size, deliveryDate, deliveryTime, fromAddress," +
                    "toAddress, orderPhone, contactPhone, note, 0 as access FROM Orders " +
                    "WHERE orderPhone = :phone;";
        }
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone", phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        // ADDING AS A COURIER
        sql = "SELECT orders.OrderId, title, couriers.status, price, size, deliveryDate, deliveryTime, fromAddress, " +
                "toAddress, orderPhone, contactPhone, note, 1 as access FROM Orders " +
                "JOIN couriers ON orders.OrderId = couriers.OrderId " +
                "WHERE couriers.courierPhone = :phone;";
        param = new MapSqlParameterSource()
                .addValue("phone", phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        if (myOrders.isEmpty())
            throw new NotFoundException("No my orders");
        return myOrders;
    }

    @Override
    public String getPhone(Integer Id){
        if (courierExists(Id)){
            String Sql = "SELECT CourierPhone FROM couriers " +
                    "WHERE OrderId = :Id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("Id", Id);
            List <Courier> couriers = jdbcTemplate.query(Sql, param, courierExtractor);
            return couriers.get(0).getCourierPhone();
        }
        throw new NotFoundException("Courier does not exist");
    }

    @Override
    public List<Order> getActiveOrders(String phone){
        List<Order> activeOrders = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM Orders WHERE status = 'Active' AND orders.orderPhone <> :phone;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone", phone);
        activeOrders.addAll(jdbcTemplate.query(sql, param, orderExtractor));
        if (activeOrders.isEmpty())
            throw new NotFoundException("No active orders");
        return activeOrders;
    }

    @Override
    public List<Courier> getAll(){
        String sql = "SELECT * FROM couriers;";
        List<Courier> couriers = jdbcTemplate.query(sql, courierExtractor);
        if (couriers.isEmpty())
            throw new NotFoundException("No couriers");
        return couriers;
    }

    @Override
    public String getStatus(Integer orderId, String phone){
        if (isCustomer(orderId, phone)){
            return orderService.getOrder(orderId).getStatus();
        }
        if (isCourier(orderId, phone)){
            return getCourier(orderId).getStatus();
        }
        throw new NotFoundException("Order does not exist or incorrect data");
    }

}
