package employee.example.EmployeeProjetc.Controller;

import employee.example.EmployeeProjetc.Entity.CartItemProduct;
import employee.example.EmployeeProjetc.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/cart-items")
    public ResponseEntity<List<Map<String, Object>>> getAllCartItems() {
        try {
            List<Map<String, Object>> cartItems = cartItemService.getAllCart();
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error retrieving cart items: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(errorResponse));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {
        ResponseEntity<String> responseEntity = cartItemService.deleteCartItemProductByProductId(Integer.valueOf(id));
        return ResponseEntity.ok("The product has been removed from your shopping cart.");
    }
}
