package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.Entity.CartItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CartItemService {

    void addToCart(int employeeId, int productId, int quantity);

    List<Map<String, Object>> getAllCart();

    @Transactional
    void deleteCartItemProductByProductId(Long productId);

}
