package ftc.shift.sample.api;


import ftc.shift.sample.exception.NotFoundException;
import ftc.shift.sample.models.Courier;
import ftc.shift.sample.models.GetActiveOrders;
import ftc.shift.sample.models.GetMyOrders;
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

@RestController
@Api(description = "Запросы на работу с заказами")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private FullOrder fullOrder;
    private static final String _PATH = "api/v001/";


    @PostMapping(_PATH + "addOrder")
    @ApiOperation(value = "Оформление заказа")
    public ResponseEntity<Integer> createOrder(
        @ApiParam(value = "Данные для добавления нового заказа")
        @RequestBody Order order) {
        orderService.create(orderService.getIdofLast()+1, order.getOrderPhone(), order.getFromAddress(), order.getToAddress(),
                order.getPrice(), order.getOrderTime(),order.getDeliveryTime(),
                order.getStatus(), order.getNote(), order.getSize());
        return ResponseEntity.ok(orderService.getIdofLast());
    }

    @PostMapping(_PATH + "addCourier")
    @ApiOperation(value = "Принятие заказа")
    public ResponseEntity<?> createCourier(
            @ApiParam(value = "Данные для добавления принятого оформленного заказа")
            @RequestBody Courier courier) {
            fullOrder.create(courier.ID, courier.CourierPhone, 'P');
            return ResponseEntity.ok().build();
    }

    @GetMapping(_PATH + "getAllOrders")
    @ApiOperation(value = "Получение всех заказов")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "getAllCouriers")
    @ApiOperation(value = "Получение всех заказов")
    public ResponseEntity<List<Courier>> getAllCouriers(){
        List<Courier> couriers = fullOrder.getAll();
        return ResponseEntity.ok(couriers);
    }

    @GetMapping(_PATH + "getActiveOrders")
    @ApiOperation(value = "Получение всех существующих заказов")
    public ResponseEntity<List<GetActiveOrders>> getActiveOrders(
    ) {
        System.out.println("\n\nIN THE GET ACTIVE ORDERS FROM GET MAPPING\n\n");
        List<GetActiveOrders> orders = fullOrder.getActiveOrders();
        if(orders.isEmpty())
            throw new NotFoundException();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "getMyOrders")
    @ApiOperation(value = "Получение всех заказов, связанных с переданным номер телефона. ")
    public ResponseEntity<List<GetMyOrders>> getMyOrders(
            @RequestParam(value = "phone", required = true) String phone
    ) {
        List<GetMyOrders> orders = fullOrder.getMyOrders(phone);
        return ResponseEntity.ok(orders);
    }

    @GetMapping(_PATH + "getStatus")
    @ApiOperation(value = "Получение статуса заказа по номеру и ID заказа")
    public ResponseEntity<String> getStatus(
            @RequestBody Courier courier){
            char result = fullOrder.getStatus(courier.ID, courier.getCourierPhone());
            String string = result + "\0";
            return ResponseEntity.ok(string);
    }


    @PatchMapping(_PATH + "ChangeStatus")
    @ApiOperation(value = "Изменение статута заказа от заказчика")
    public  ResponseEntity<?> ChangeStatus(
            @ApiParam(value = "Данные для изменения статуса")
            @RequestBody Courier courier) {
        if (courier.getStatus() == 'C' && courier.getStatus() == 'D')
            throw new NotFoundException();
        fullOrder.changeStatus(courier.ID, courier.Status, courier.getCourierPhone());
        return ResponseEntity.ok().build();
    }


}
