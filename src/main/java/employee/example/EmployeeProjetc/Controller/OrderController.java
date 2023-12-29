package employee.example.EmployeeProjetc.Controller;

import employee.example.EmployeeProjetc.Entity.Order;
import employee.example.EmployeeProjetc.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping(path = "/list")
    public List<Order> listOrders(){
        return orderService.listOrders();
    }
}
