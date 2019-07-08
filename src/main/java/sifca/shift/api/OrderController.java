package sifca.shift.api;


import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Order;
import sifca.shift.services.OrderService;
import sifca.shift.models.Courier;
import sifca.shift.models.ActiveOrders;
import sifca.shift.models.MyOrders;
import sifca.shift.services.OrderAndCourierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "Запросы на работу с заказами")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderAndCourierService orderAndCourierService;
    private static final String _PATH = "api/v001/";


    @PostMapping(_PATH + "add/order")
    @ApiOperation(value = "Оформление заказа")
    public ResponseEntity<Integer> createOrder(
            @ApiParam(value = "Данные для добавления нового заказа")
            @RequestBody Order order) {
        orderService.create(orderService.getIdOfLast(), order.getOrderPhone(), order.getFromAddress(), order.getToAddress(),
                order.getPrice(), order.getOrderTime(),order.getDeliveryTime(),
                order.getStatus(), order.getNote(), order.getSize());
        return ResponseEntity.ok(orderService.getIdOfLast());
    }

    @PostMapping(_PATH + "add/courier")
    @ApiOperation(value = "Принятие заказа")
    public ResponseEntity<?> createCourier(
            @ApiParam(value = "Данные для добавления принятого оформленного заказа")
            @RequestBody Courier courier) {
        orderAndCourierService.create(courier.orderId, courier.courierPhone, "Processing");
        return ResponseEntity.ok().build();
    }

    @GetMapping(_PATH + "get/orders")
    @ApiOperation(value = "Получение всех заказов со стороны заказчика")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/couriers")
    @ApiOperation(value = "Получение всех объявлений о заказах")
    public ResponseEntity<List<Courier>> getAllCouriers(){
        List<Courier> couriers = orderAndCourierService.getAll();
        return ResponseEntity.ok(couriers);
    }

    @GetMapping(_PATH + "get/active-orders")
    @ApiOperation(value = "Получение всех курьерских заказов")
    public ResponseEntity<List<ActiveOrders>> getActiveOrders(
    ) {
        List<ActiveOrders> orders = orderAndCourierService.getActiveOrders();
        if(orders.isEmpty())
            throw new NotFoundException();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/my-orders")
    @ApiOperation(value = "Получение всех заказов, связанных с переданным номер телефона. ")
    public ResponseEntity<List<MyOrders>> getMyOrders(
            @RequestParam(value = "phone", required = true) String phone
    ) {
        List<MyOrders> orders = orderAndCourierService.getMyOrders(phone);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/status")
    @ApiOperation(value = "Получение статуса заказа по номеру и orderId заказа")
    public ResponseEntity<String> getStatus(
            @RequestBody Courier courier){
        String result = orderAndCourierService.getStatus(courier.orderId, courier.getCourierPhone());
        return ResponseEntity.ok(result);
    }


    @PatchMapping(_PATH + "change/status")
    @ApiOperation(value = "Изменение статута заказа от заказчика")
    public  ResponseEntity<?> ChangeStatus(
            @ApiParam(value = "Данные для изменения статуса")
            @RequestBody Courier courier) {
        orderAndCourierService.changeStatus(courier.orderId, courier.status, courier.getCourierPhone());
        return ResponseEntity.ok().build();
    }
}
