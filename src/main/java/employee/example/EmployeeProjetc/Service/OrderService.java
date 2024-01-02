package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.DTO.OrderDTO;
import employee.example.EmployeeProjetc.Entity.Order;
import jakarta.transaction.Transactional;

import java.util.List;

public interface OrderService {
    @Transactional
    String purchaseOrder(OrderDTO order);

    List<Order> listOrders();

    @Transactional
    String updateOrderStatus(Integer orderId, int newStatus);
}
