package employee.example.EmployeeProjetc.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CartItemService {

    void addToCart(int employeeId, int productId, int quantity);

    List<Map<String, Object>> getAllCart();

    @Transactional
    ResponseEntity<String> deleteCartItemProductByProductId(Integer productId);

}
