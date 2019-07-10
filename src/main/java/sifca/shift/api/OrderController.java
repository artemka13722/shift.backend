package sifca.shift.api;


import sifca.shift.exception.NotFoundException;
import sifca.shift.models.*;
import sifca.shift.services.OrderService;
import sifca.shift.services.OrderAndCourierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "Запросы на работу с заказами/Queries for work with orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderAndCourierService orderAndCourierService;
    private static final String _PATH = "api/v001/";


    @PostMapping(_PATH + "add/order")
    @ApiOperation(value = "Оформление заказа/Placing new order")
    public ResponseEntity<Integer> createOrder(
            @ApiParam(value = "Данные для добавления нового заказа/" +
                    "Data for adding new order")
            @RequestBody Order order) {
        orderService.create(null, order.getTitle(), order.getOrderPhone(), order.getFromAddress(),
                order.getToAddress(), order.getContactPhone(), order.getPrice(), order.getDeliveryDate(),
                order.getDeliveryTime(), order.getNote(), order.getSize());
        return ResponseEntity.ok(orderService.getIdOfLast());
    }

    @PostMapping(_PATH + "add/courier")
    @ApiOperation(value = "Принятие заказа/Taking a order")
    public ResponseEntity<?> createCourier(
            @ApiParam(value = "Данные для добавления принятого оформленного заказа/" +
                    "Data for taking the order")
            @RequestBody Id_phone body) {
        orderAndCourierService.create(body.getId(), body.getPhone(), "Processing");
        return ResponseEntity.ok().build();
    }

    @GetMapping(_PATH + "get/orders")
    @ApiOperation(value = "Получение всех заказов со стороны заказчика/" +
            "Getting all orders")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/couriers")
    @ApiOperation(value = "Получение всех объявлений о заказах/" +
            "Getting all taking orders")
    public ResponseEntity<List<Courier>> getAllCouriers(){
        List<Courier> couriers = orderAndCourierService.getAll();
        return ResponseEntity.ok(couriers);
    }

    @GetMapping(_PATH + "get/active-orders")
    @ApiOperation(value = "Получение всех активных заказов/" +
            "Getting all active orders")
    public ResponseEntity<List<ActiveOrders>> getActiveOrders(
    ) {
        List<ActiveOrders> orders = orderAndCourierService.getActiveOrders();
        if(orders.isEmpty())
            throw new NotFoundException();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/my-orders")
    @ApiOperation(value = "Получение всех заказов, связанных с переданным номером телефона/" +
            "Getting my orders")
    public ResponseEntity<List<MyOrders>> getMyOrders(
            @RequestParam(value = "phone", required = true) String phone
    ) {
        List<MyOrders> orders = orderAndCourierService.getMyOrders(phone);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/status")
    @ApiOperation(value = "Получение статуса заказа по номеру и orderId заказа/" +
            "Getting status of order by phone number and OrderId")
    public ResponseEntity<String> getStatus(
            @RequestBody Id_phone body){
        String result = orderAndCourierService.getStatus(body.getId(), body.getPhone());
        return ResponseEntity.ok(result);
    }

    @PatchMapping(_PATH + "close")
    @ApiOperation(value = "Закрытие заказа/" +
            "Closing the order")
    public  ResponseEntity<?> ChangeStatus(
            @ApiParam(value = "Данные для изменения статуса/" +
                    "Data for changing status")
            @RequestBody Id_phone body) {
        orderAndCourierService.changeStatus(body.getId(), body.getPhone());
        return ResponseEntity.ok().build();
    }
}
