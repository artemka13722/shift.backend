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

    List<ActiveOrders> orders = new ArrayList<>();

    List<Courier> couriers = new ArrayList<>();

    @PostConstruct
    public void initialize(){
        String createTable = "CREATE TABLE IF NOT EXISTS Couriers(" +
                "OrderId int NOT NULL," +
                "CourierPhone nvarchar(11) NOT NULL," +
                "Status varchar(15) NOT NULL CHECK(Status IN('Done', 'Processing', 'Closed'))" +
                ");";
        jdbcTemplate.update(createTable, new MapSqlParameterSource());
        //create(1, "89135895600", "Processing");
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
        throw new NotFoundException("Error get Order", 3);
    }

    @Override
    public void create(Integer orderId, String courierPhone, String Status){
        if (existAndActive(orderId) && !courierExists(orderId) && userService.exists(courierPhone)){
            String SqlInsert = "INSERT INTO Couriers VALUES(:orderId, :courierPhone,:Status);";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("orderId", orderId)
                    .addValue("courierPhone", courierPhone)
                    .addValue("Status", Status);
            jdbcTemplate.update(SqlInsert, param);
        }
    }

    @Override
    public Courier getCourier(Integer id){
        String sql = "SELECT * FROM couriers WHERE OrderId = :OrderId;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("OrderId", id);
        List<Courier> couriers = jdbcTemplate.query(sql, param, courierExtractor);
        return couriers.get(0);
    }

    @Override
    public boolean isCustomer(Integer id, String phone){
        if (orderService.exists(id)){
            String sql = "SELECT * FROM orders " +
                    "JOIN Couriers ON orders.OrderId = Couriers.OrderId " +
                    "WHERE orders.OrderId = :OrderId AND orders.orderPhone = :phone;";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", id)
                    .addValue("phone", phone);
            List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
            if (orders.isEmpty())
                return false;
            return true;
        }
        throw new NotFoundException();
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
        throw new NotFoundException();
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
                }
            }
            else
            {
                if (getCourier(orderId).getStatus().equals("Processing")){
                    orderService.changeStatus(orderId, "Active");
                    String sql = "UPDATE couriers SET status = :status;";
                    MapSqlParameterSource param = new MapSqlParameterSource()
                            .addValue("status", "Closed");
                    jdbcTemplate.update(sql, param);
                }
            }
        }
        else
            throw new NotFoundException();
    }

    @Override
    public List<MyOrders> getMyOrders(String phone){
        List<MyOrders> myOrders = new ArrayList<>();
        // ADDING AS A CUSTOMER
        String sql = "SELECT orderPhone, fromAddress, toAddress, price, orderTime," +
                " deliveryTime, status, 0 as access, note, size FROM Orders " +
                "WHERE orderPhone = :phone;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone",phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        // ADDING AS A COURIER
        sql = "SELECT orderPhone, fromAddress, toAddress, price, orderTime," +
                " deliveryTime, couriers.status, 0 as access, note, size FROM Orders " +
                "JOIN couriers ON Orders.OrderId = Couriers.OrderId " +
                "WHERE couriers.courierPhone = :phone;";
        param = new MapSqlParameterSource()
                .addValue("phone",phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        return myOrders;
    }

    @Override
    public String getPhone(Integer Id){
        if (courierExists(Id)){
            String Sql = "SELECT CourierPhone FROM couriers" +
                    "WHERE OrderId = :Id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("Id", Id);
            List <Courier> couriers = jdbcTemplate.query(Sql, param, courierExtractor);
            return couriers.get(0).getCourierPhone();
        }
        throw new NotFoundException("Error getPhone courier", 7);
    }

    @Override
    public List<ActiveOrders> getActiveOrders(){
        List<ActiveOrders> activeOrders = new ArrayList<>();
        String sql = "SELECT orderPhone, fromAddress, toAddress, price, orderTime," +
                " deliveryTime, note, size FROM Orders " +
                "WHERE status = 'Active';";
        activeOrders.addAll(jdbcTemplate.query(sql, activeOrdersExtractor));
        return activeOrders;
    }

    @Override
    public List<Courier> getAll(){
        String sql = "SELECT * FROM couriers;";
        List<Courier> couriers = jdbcTemplate.query(sql, courierExtractor);
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
        throw new NotFoundException("Error getStatus Ordder or Courier", 8);
    }
}
