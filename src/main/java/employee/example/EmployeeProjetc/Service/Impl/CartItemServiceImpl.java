package employee.example.EmployeeProjetc.Service.Impl;

import employee.example.EmployeeProjetc.Entity.*;
import employee.example.EmployeeProjetc.Repository.CartItemProductRepository;
import employee.example.EmployeeProjetc.Repository.CartItemRepository;
import employee.example.EmployeeProjetc.Repository.EmployeeRepository;
import employee.example.EmployeeProjetc.Repository.ProductRepository;
import employee.example.EmployeeProjetc.Service.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    private final CartItemProductRepository cartItemProductRepository;

    @Autowired
    public CartItemServiceImpl(EmployeeRepository employeeRepository,
                               ProductRepository productRepository,
                               CartItemRepository cartItemRepository,
                               CartItemProductRepository cartItemProductRepository) {
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemProductRepository = cartItemProductRepository;
    }

    @Override
    @Transactional
    public void addToCart(int employeeId, int productId, int quantity) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
            Optional<Product> productOptional = productRepository.findById(productId);

            if (employeeOptional.isPresent() && productOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                Product product = productOptional.get();

                // CART ITEM
                CartItem checkExistedCart = cartItemRepository.findCartItemByStatusAndEmployee(0, employee);
                Set<Product> products = new HashSet<>();
                if (checkExistedCart == null) {
                    checkExistedCart = CartItem.builder()
                            .employee(employee)
                            .build();
                    checkExistedCart.setEmployee(employee);
                }
                checkExistedCart = cartItemRepository.save(checkExistedCart);

                //CART ITEM PRODUCT

                CartItemProduct cartItemProduct = cartItemProductRepository.findCartItemProductByCartItemIdAndProductId(checkExistedCart.getId(), productId);
                if (cartItemProduct == null) {
                    cartItemProduct = new CartItemProduct();
                    cartItemProduct.setCartItemId(checkExistedCart.getId());
                    cartItemProduct.setProductId(productId);
                    cartItemProduct.setQuantity(1);
                } else {
                    cartItemProduct.setQuantity(cartItemProduct.getQuantity() + 1);
                }
                cartItemProductRepository.save(cartItemProduct);
            } else {
                throw new RuntimeException("Employee or Product not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding item to cart: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getAllCart() {
        try {
            List<Map<String, Object>> cartItemProducts = cartItemProductRepository.findAllWithDetails();
            List<Map<String, Object>> result = new ArrayList<>();

            List<Integer> cartItemIds = cartItemProducts.stream()
                    .map(cp -> (Integer) cp.get("cartItemId"))
                    .collect(Collectors.toList());
            List<Integer> productIds = cartItemProducts.stream()
                    .map(cp -> (Integer) cp.get("productId"))
                    .collect(Collectors.toList());

            List<Map<String, Object>> employeeDetails = cartItemProductRepository.findEmployeeDetailsByCartItems(cartItemIds);
            List<Map<String, Object>> productDetails = cartItemProductRepository.findProductDetailsByIds(productIds);

            for (Map<String, Object> cartItemProduct : cartItemProducts) {
                Map<String, Object> itemDetails = new HashMap<>();

                itemDetails.putAll(cartItemProduct);
                itemDetails.putAll(employeeDetails.stream()
                        .filter(e -> Objects.equals(e.get("cartItemId"), cartItemProduct.get("cartItemId")))
                        .findFirst().orElse(Collections.emptyMap()));
                itemDetails.putAll(productDetails.stream()
                        .filter(p -> Objects.equals(p.get("productId"), cartItemProduct.get("productId")))
                        .findFirst().orElse(Collections.emptyMap()));

                result.add(itemDetails);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all cart items: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public ResponseEntity<String> deleteCartItemProductByProductId(Integer productId) {
        try {
            cartItemProductRepository.deleteByProductId(Math.toIntExact(productId));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting cart item product by product ID: " + e.getMessage());
        }
        return null;
    }
}
