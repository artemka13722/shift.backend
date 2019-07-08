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
    private OrderAndCourierService orderAndCourier;
    private static final String _PATH = "api/v001/";


    @PostMapping(_PATH + "add/Order")
    @ApiOperation(value = "Оформление заказа")
    public ResponseEntity<Integer> createOrder(
            @ApiParam(value = "Данные для добавления нового заказа")
            @RequestBody Order order) {
        orderService.create(orderService.getIdOfLast(), order.getOrderPhone(), order.getFromAddress(), order.getToAddress(),
                order.getPrice(), order.getOrderTime(),order.getDeliveryTime(),
                order.getStatus(), order.getNote(), order.getSize());
        return ResponseEntity.ok(orderService.getIdOfLast());
    }

    @PostMapping(_PATH + "add/Courier")
    @ApiOperation(value = "Принятие заказа")
    public ResponseEntity<?> createCourier(
            @ApiParam(value = "Данные для добавления принятого оформленного заказа")
            @RequestBody Courier courier) {
        orderAndCourier.create(courier.ID, courier.CourierPhone, 'P');
        return ResponseEntity.ok().build();
    }

    @GetMapping(_PATH + "get/AllOrders")
    @ApiOperation(value = "Получение всех заказов со стороны заказчика")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/AllCouriers")
    @ApiOperation(value = "Получение всех объявлений о заказах")
    public ResponseEntity<List<Courier>> getAllCouriers(){
        List<Courier> couriers = orderAndCourier.getAll();
        return ResponseEntity.ok(couriers);
    }

    @GetMapping(_PATH + "get/ActiveOrders")
    @ApiOperation(value = "Получение всех курьерских заказов")
    public ResponseEntity<List<ActiveOrders>> getActiveOrders(
    ) {
        List<ActiveOrders> orders = orderAndCourier.getActiveOrders();
        if(orders.isEmpty())
            throw new NotFoundException();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/MyOrders")
    @ApiOperation(value = "Получение всех заказов, связанных с переданным номер телефона. ")
    public ResponseEntity<List<MyOrders>> getMyOrders(
            @RequestParam(value = "phone", required = true) String phone
    ) {
        List<MyOrders> orders = orderAndCourier.getMyOrders(phone);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "get/Status")
    @ApiOperation(value = "Получение статуса заказа по номеру и ID заказа")
    public ResponseEntity<String> getStatus(
            @RequestBody Courier courier){
        char result = orderAndCourier.getStatus(courier.ID, courier.getCourierPhone());
        String string = result + "\0";
        return ResponseEntity.ok(string);
    }


    @PatchMapping(_PATH + "Change/Status")
    @ApiOperation(value = "Изменение статута заказа от заказчика")
    public  ResponseEntity<?> ChangeStatus(
            @ApiParam(value = "Данные для изменения статуса")
            @RequestBody Courier courier) {
        if (courier.getStatus() == 'C' && courier.getStatus() == 'D')
            throw new NotFoundException();
        orderAndCourier.changeStatus(courier.ID, courier.Status, courier.getCourierPhone());
        return ResponseEntity.ok().build();
    }
}
