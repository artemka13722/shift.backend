package sifca.shift.repositories;

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
import sifca.shift.services.OrderService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "use.database", havingValue = "true")
public class OrderAndCourierDatabase implements OrderAndCourierRepository{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    List<Courier> couriers = new ArrayList<>();
    private Integer count = -1;

    @Autowired
    OrderService orderInMemory;

    List<ActiveOrders> orders = new ArrayList<>();

    @Autowired
    private CourierExtractor courierExtractor;

    @Autowired
    private OrderExtractor orderExtractor;

    @Autowired
    private MyOrdersExtractor myOrdersExtractor;

    @Autowired
    private ActiveOrdersExtractor activeOrdersExtractor;

    @PostConstruct
    public void initialize(){
        String createTable = "CREATE TABLE Couriers(" +
                "OrderId int NOT NULL," +
                "CourierPhone nvarchar(11) NOT NULL," +
                "Status varchar(1) NOT NULL CHECK(Status IN('D', 'F', 'P', 'W', 'C'))," +
                ");";
        jdbcTemplate.update(createTable, new MapSqlParameterSource());
    }

    @Override
    public boolean existAndActive(Integer OrderId){
        if (orderInMemory.exists(OrderId)){
            Order order = orderInMemory.getOrder(OrderId);
            if (order.getStatus() == 'A'){
                return true;
            }
            return false;
        }
        throw new NotFoundException();
    }

    @Override
    public void create(Integer OrderId, String CourierPhone, char Status){
        if (existAndActive(OrderId)){
            String SqlInsert = "INSERT INTO Couriers VALUES(:OrderId, :CourierPhone,:Status);";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", OrderId)
                    .addValue("CourierPhone", CourierPhone)
                    .addValue("Status", Status);
            jdbcTemplate.update(SqlInsert, param);
        }
    }

    @Override
    public Courier getCourier(Integer OrderId){
        String sql = "SELECT * FROM couriers WHERE OrderId = :OrderId;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("OrderId", OrderId);
        List<Courier> couriers = jdbcTemplate.query(sql, param, courierExtractor);
        return couriers.get(0);
    }

    @Override
    public boolean isCustomer(Integer OrderId, String phone){
        if (orderInMemory.exists(OrderId)){
            String sql = "SELECT * FROM orders " +
                    "JOIN Couriers ON orders.OrderId = Couriers.OrderId" +
                    "WHERE OrderId = :OrderId AND order.orderPhone = :phone;";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", OrderId)
                    .addValue("phone", phone);
            List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
            if (orders.isEmpty())
                return false;
            return true;
        }
        throw new NotFoundException();
    }

    @Override
    public boolean isCourier(Integer OrderId, String phone){
        if (orderInMemory.exists(OrderId)){
            String sql = "SELECT * FROM orders " +
                    "JOIN Couriers ON orders.OrderId = Couriers.OrderId" +
                    "WHERE OrderId = :OrderId AND courier.CourierPhone = :phone;";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("OrderId", OrderId)
                    .addValue("phone", phone);
            List<Order> orders = jdbcTemplate.query(sql, param, orderExtractor);
            if (orders.isEmpty())
                return false;
            return true;
        }
        throw new NotFoundException();
    }

    @Override
    public boolean CourierExists(Integer OrderId){
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
    public void changeStatus(Integer OrderId, char Status, String phone) {
        if ((!isCustomer(OrderId, phone) && !isCourier(OrderId, phone)          // Если это не заказчик, и не курьер или
                || (Status != 'C' && Status != 'D')))                           // запрос не на отмену или закрытие заказа
            throw new NotFoundException();                                      // вернуть ошибку
        else {
            if (Status == 'C' && isCustomer(OrderId, phone)) {                  // Если запрос на отмену, и это заказчик
                if (!CourierExists(OrderId)) {                                  // Если у заказа есть курьер
                    String sql = "UPDATE couriers SET status = :status" +
                            "WHERE OrderId = :OrderId;";
                    MapSqlParameterSource param = new MapSqlParameterSource()
                            .addValue("OrderId", OrderId)
                            .addValue("status", Status);
                    jdbcTemplate.update(sql, param);
                }
                orderInMemory.changeStatus(OrderId, Status);          // Меняем статус заказа на отмененный
            } else {                                            // Если это курьер
                if (Status == 'D') {                            // Если запрос на закрытие
                    orderInMemory.changeStatus(OrderId, Status);      // Закрытие заказа со стороны заказчика
                } else {                                      // Если запрос на отмену
                    orderInMemory.changeStatus(OrderId, 'A');         // Статус заказа меняется на активный
                    String sql = "UPDATE couriers SET status = :status" +
                            "WHERE OrderId = :OrderId;";
                    MapSqlParameterSource param = new MapSqlParameterSource()
                            .addValue("OrderId", OrderId)
                            .addValue("status", Status);
                    jdbcTemplate.update(sql, param);                // Закрытие заказа со стороны курьера
                }
            }
        }
    }

    @Override
    public List<MyOrders> getMyOrders(String phone){
        List<MyOrders> myOrders = new ArrayList<>();
        // ADDING AS A CUSTOMER
        String sql = "SELECT orderPhone, fromAddress, toAddress, price, orderTime," +
                " deliveryTime, status, '0' as \"access\" note, size FROM Orders" +
                "WHERE orderPhone = :phone;";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("phone",phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        // ADDING AS A COURIER
        sql = "SELECT orderPhone, fromAddress, toAddress, price, orderTime," +
                " deliveryTime, status, '0' as \"access\" note, size FROM Orders" +
                "JOIN couriers ON order.OrderId = courier.OrderId" +
                "WHERE CourierPhone = :phone;";
        param = new MapSqlParameterSource()
                .addValue("phone",phone);
        myOrders.addAll(jdbcTemplate.query(sql, param, myOrdersExtractor));
        return myOrders;
    }

    @Override
    public String getPhone(Integer Id){
        if (CourierExists(Id)){
            String Sql = "SELECT CourierPhone FROM couriers" +
                    "WHERE OrderId = :Id";
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("Id", Id);
            List <Courier> couriers = jdbcTemplate.query(Sql, param, courierExtractor);
            return couriers.get(0).getCourierPhone();
        }
        throw new NotFoundException();
    }

    @Override
    public List<ActiveOrders> getActiveOrders(){
        List<ActiveOrders> activeOrders = new ArrayList<>();
        String sql = "SELECT orderPhone, fromAddress, toAddress, price, orderTime," +
                " deliveryTime, note, size FROM Orders" +
                "WHERE status = 'A';";
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
    public char getStatus(Integer OrderId, String phone){
        if (isCustomer(OrderId, phone)){
            return orderInMemory.getOrder(OrderId).getStatus();
        }
        if (isCourier(OrderId, phone)){
            return getCourier(OrderId).getStatus();
        }
        throw new NotFoundException();
    }
}
