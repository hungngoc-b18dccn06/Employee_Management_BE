package employee.example.EmployeeProjetc.Controller;

import employee.example.EmployeeProjetc.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestParam int employeeId,
            @RequestParam int productId,
            @RequestParam int quantity) {
        try {
            cartItemService.addToCart(employeeId, productId, quantity);
            return ResponseEntity.ok("Item added to the cart successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding item to cart: " + e.getMessage());
        }
    }
}
