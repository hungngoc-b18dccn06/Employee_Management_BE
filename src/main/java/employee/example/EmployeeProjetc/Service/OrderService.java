package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.DTO.OrderDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    @Transactional
    String purchaseOrder(OrderDTO order);
}
