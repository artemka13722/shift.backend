package ftc.shift.sample.api;


import ftc.shift.sample.models.Courier;
import ftc.shift.sample.models.Order;
import ftc.shift.sample.repositories.FullOrder;
import ftc.shift.sample.services.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@Api(description = "Запросы на работу с заказами")
public class OrderController {
    @Autowired
    private OrderService orderService;
    private FullOrder fullOrder;


    @PostMapping("api/v001/addOrder")
    @ApiOperation(value = "Оформление заказа")
    public ResponseEntity<Integer> createOrder(
        @ApiParam(value = "Данные для добавления нового заказа")
        @RequestBody Order order) {
        orderService.create(orderService.getIdofLast()+1, order.getOrderPhone(), order.getFromAddress(), order.getToAddress(),
                order.getPrice(), order.getOrderTime(),order.getDeliveryTime(),
                order.getStatus(), order.getNote(), order.getSize());
        return ResponseEntity.ok(orderService.getIdofLast());
    }

    @PostMapping("api/v001/addCourier")
    @ApiOperation(value = "Принятие заказа")
    public ResponseEntity<?> createCourier(
            @ApiParam(value = "Данные для добавления принятого оформленного заказа")
            @RequestBody Courier courier) {
            fullOrder.create(courier.ID, courier.CourierPhone, courier.Status);
            return ResponseEntity.ok().build();
    }


    @GetMapping("api/v001/getAllOrders")
    @ApiOperation(value = "Получение всех существующих заказов")
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.getAll();
        //Map<Order, String> query = new HashMap<Order, String>();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("api/v001/ChangeStatus")
    @ApiOperation(value = "Изменение статута заказа от заказчика")
    public  ResponseEntity<?> ChangeStatus(
            @ApiParam(value = "Данные для изменения статуса")
            @RequestBody Courier courier) {
        fullOrder.changeStatus(courier.ID, courier.Status, courier.getCourierPhone());
        return ResponseEntity.ok().build();
    }
}
