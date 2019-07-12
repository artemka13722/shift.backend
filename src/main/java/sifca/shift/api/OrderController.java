package sifca.shift.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sifca.shift.exception.NotFoundException;
import sifca.shift.models.Courier;
import sifca.shift.models.MyOrders;
import sifca.shift.models.Order;
import sifca.shift.services.OrderAndCourierService;
import sifca.shift.services.OrderService;
import sifca.shift.services.UserService;

import java.util.List;

@RestController
@Api(description = "Запросы на работу с заказами/Queries for work with orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderAndCourierService orderAndCourierService;

    @Autowired
    private UserService userService;

    private static final String _PATH = "api/v001/";

    public boolean isCorrectPhone(String phone) {
        return true;
//        Pattern p = Pattern.compile("[0-9]+");
//        Matcher m = p.matcher(phone);
//        if (m.matches() && userService.exists(phone)) {
//            return true;
//        }
//        return false;
    }

    @PostMapping(_PATH + "add/order")
    @ApiOperation(value = "Оформление заказа/Placing new order")
    public ResponseEntity<?> createOrder(
            @ApiParam(value = "Данные для добавления нового заказа/" +
                    "Data for adding new order")
            @RequestHeader(value = "orderPhone", required = true) String phone,
            @RequestBody Order order) {
        if (isCorrectPhone(phone)) {
            orderService.create(null, order.getTitle(), phone, order.getFromAddress(),
                    order.getToAddress(), order.getContactPhone(), order.getPrice(), order.getDeliveryDate(),
                    order.getDeliveryTime(), "Active", order.getNote(), order.getSize());
            return ResponseEntity.ok().build();
        } else
            throw new NotFoundException("Phone number is incorrect or access error");
    }

    @PostMapping(_PATH + "add/courier")
    @ApiOperation(value = "Принятие заказа/Taking a order")
    public ResponseEntity<?> createCourier(
            @ApiParam(value = "Данные для добавления принятого оформленного заказа/" +
                    "Data for taking the order")
            @RequestHeader(value = "orderPhone", required = true) String phone,
            @RequestBody Integer id) {
        if (isCorrectPhone(phone)) {
            orderAndCourierService.create(id, phone, "Processing");
            return ResponseEntity.ok().build();
        } else
            throw new NotFoundException("Phone number is incorrect or access error");
    }

    @GetMapping(_PATH + "get/orders")
    @ApiOperation(value = "Получение всех заказов со стороны заказчика/" +
            "Getting all orders")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader(value = "orderPhone", required = true) String phone) {
        if (isCorrectPhone(phone)) {
            List<Order> orders = orderService.getAll();
            return ResponseEntity.ok(orders);
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }

    @GetMapping(_PATH + "get/couriers")
    @ApiOperation(value = "Получение всех объявлений о заказах/" +
            "Getting all taking orders")
    public ResponseEntity<List<Courier>> getAllCouriers(
            @RequestHeader(value = "orderPhone", required = true) String phone) {
        if (isCorrectPhone(phone)) {
            List<Courier> couriers = orderAndCourierService.getAll();
            return ResponseEntity.ok(couriers);
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }

    @GetMapping(_PATH + "get/active-orders")
    @ApiOperation(value = "Получение всех активных заказов/" +
            "Getting all active orders")
    public ResponseEntity<List<Order>> getActiveOrders(
            @RequestHeader(value = "orderPhone", required = true) String phone) {
        if (isCorrectPhone(phone)) {
            List<Order> orders = orderAndCourierService.getActiveOrders(phone);
            return ResponseEntity.ok(orders);
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }

    @GetMapping(_PATH + "get/my-orders")
    @ApiOperation(value = "Получение всех заказов, связанных с переданным номером телефона/" +
            "Getting my orders")
    public ResponseEntity<List<MyOrders>> getMyOrders(
            @RequestHeader(value = "orderPhone", required = true) String phone) {
        if (isCorrectPhone(phone)) {
            List<MyOrders> orders = orderAndCourierService.getMyOrders(phone);
            return ResponseEntity.ok(orders);
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }

    @GetMapping(_PATH + "get/status")
    @ApiOperation(value = "Получение статуса заказа по номеру и orderId заказа/" +
            "Getting status of order by orderPhone number and OrderId")
    public ResponseEntity<String> getStatus(
            @RequestHeader(value = "orderPhone", required = true) String phone,
            @RequestBody Integer id) {
        if (isCorrectPhone(phone)) {
            String result = orderAndCourierService.getStatus(id, phone);
            return ResponseEntity.ok(result);
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }

    @PatchMapping(_PATH + "close")
    @ApiOperation(value = "Закрытие заказа/" +
            "Closing the order")
    public ResponseEntity<?> close(
            @ApiParam(value = "Данные для изменения статуса/" +
                    "Data for changing status")
            @RequestHeader(value = "orderPhone", required = true) String phone,
            @RequestBody Integer id) {
        if (isCorrectPhone(phone)) {
            orderAndCourierService.close(id, phone);
            return ResponseEntity.ok().build();
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }

    @PatchMapping(_PATH + "cancel")
    @ApiOperation(value = "Отмена заказа/" +
            "Closing the order")
    public ResponseEntity<?> cancel(
            @ApiParam(value = "Данные для изменения статуса/" +
                    "Data for changing status")
            @RequestHeader(value = "orderPhone", required = true) String phone,
            @RequestBody Integer id) {
        if (isCorrectPhone(phone)) {
            orderAndCourierService.cancel(id, phone);
            return ResponseEntity.ok().build();
        }
        throw new NotFoundException("Phone number is incorrect or access error");
    }
}