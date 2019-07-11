package sifca.shift.repositories.DataBases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Courier;
import sifca.shift.models.Order;
import sifca.shift.models.ActiveOrders;
import sifca.shift.models.MyOrders;
import sifca.shift.repositories.Extractors.ActiveOrdersExtractor;
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

    @Autowired
    private ActiveOrdersExtractor activeOrdersExtractor;

    @PostConstruct
    public void initialize() {
        String createTable = "CREATE TABLE IF NOT EXISTS Couriers(" +
                "OrderId int NOT NULL," +
                "CourierPhone nvarchar(11) NOT NULL," +
                "Status varchar(15) NOT NULL CHECK(Status IN('Done', 'Processing', 'Closed'))" +
                ");";
        jdbcTemplate.update(createTable, new MapSqlParameterSource());
    }

    @Override
    public boolean existAndActive(Integer OrderId){
        if (orderService.exists(OrderId)){
            Order order = orderService.getOrder(OrderId);
            if (order.getStatus().equals("Active")){
                return true;
            }
            return false;
        }
        throw new NotFoundException("Order does not tableExist");
    }

    @Override
    public void create(Integer orderId, String courierPhone, String status){
        if (existAndActive(orderId) && !courierExists(orderId) && userService.exists(courierPhone)){
            String SqlInsert = "INSERT INTO Couriers VALUES(:orderId, :courierPhone, 'Processing');";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("orderId", orderId)
                    .addValue("courierPhone", courierPhone);
            jdbcTemplate.update(SqlInsert, param);
        }
        throw new NotFoundException("The order is already tableExist or courier for this order is already tableExist " +
                "or user with the phone number does not tableExist");
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
        throw new NotFoundException("Courier with the id does not tableExist");
    }

    @Override
    public boolean isCustomer(Integer id, String phone){
        if (orderService.exists(id)){
            String sql = "SELECT * FROM orders " +
                    "WHERE orders.OrderId = :OrderId";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", id)
                    .addValue("phone", phone);
            List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
            if (orders.isEmpty())
                return false;
            return true;
        }
        throw new NotFoundException("Order does not tableExist");
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
        throw new NotFoundException("Order does not tableExist");
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

    // CHANGE, try to make this more easy and clearer
    @Override
    public void changeStatus(Integer orderId, String phone) {
        if (orderService.exists(orderId)){
            if (isCustomer(orderId, phone)){
                if (orderService.getOrder(orderId).getStatus().equals("Active")
                        || orderService.getOrder(orderId).getStatus().equals("Processing")){
                    if (courierExists(orderId)){
                        String sql = "UPDATE couriers SET status = :status;";
                        MapSqlParameterSource param = new MapSqlParameterSource()
                                .addValue("status", "Closed");
                        jdbcTemplate.update(sql, param);
                    }
                    orderService.changeStatus(orderId, "Closed");
                }
            }
            else
            {
                if (getCourier(orderId).getStatus().equals("Processing")){
                    orderService.changeStatus(orderId, "Active");
                    String sql = "UPDATE couriers SET status = :status WHERE orderId = :id;";
                    MapSqlParameterSource param = new MapSqlParameterSource()
                            .addValue("status", "Closed")
                            .addValue("id", orderId);
                    jdbcTemplate.update(sql, param);
                }
            }
        }
        else
            throw new NotFoundException("Order does not tableExist");
    }

    @Override
    public List<MyOrders> getMyOrders(String phone){
        List<MyOrders> myOrders = new ArrayList<>();
        // ADDING AS A CUSTOMER
        String sql = "SELECT id, title, status, price, size, deliveryDate, deliveryTime, fromAddress," +
                "toAddress, orderPhone, contactPhone, note, 0 as access FROM Orders " +
                "WHERE orderPhone = :phone;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone",phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        // ADDING AS A COURIER
        sql = "SELECT id, title, status, price, size, deliveryDate, deliveryTime, fromAddress, " +
                "toAddress, orderPhone, contactPhone, note, 0 as access FROM Orders " +
                "JOIN couriers ON orders.OrderId = couriers.OrderId " +
                "WHERE couriers.courierPhone = :phone;";
        param = new MapSqlParameterSource()
                .addValue("phone",phone);
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
        throw new NotFoundException("Courier does not tableExist");
    }

    @Override
    public List<ActiveOrders> getActiveOrders(String phone){
        List<ActiveOrders> activeOrders = new ArrayList<>();
        String sql = "SELECT title, price, size, deliveryDate, deliveryTime, fromAddress, toAddress, note " +
                "FROM Orders WHERE status = 'Active' AND orders.orderPhone <> :phone;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone", phone);
        activeOrders.addAll(jdbcTemplate.query(sql, param, activeOrdersExtractor));
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
        throw new NotFoundException("Order does not tableExist or the phone number is incorrect");
    }
}
