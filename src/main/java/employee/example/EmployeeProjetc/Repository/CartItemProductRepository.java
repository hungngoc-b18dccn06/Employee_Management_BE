package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.CartItemProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CartItemProductRepository extends JpaRepository<CartItemProduct, Integer> {
    CartItemProduct findCartItemProductByCartItemIdAndProductId(int a, int b);

    List<CartItemProduct> findCartItemProductByCartItemId(int a);

    @Query("SELECT new map(cp.id as cartItemProductId, cp.quantity as quantity, " +
            "cp.cartItemId as cartItemId, cp.productId as productId) " +
            "FROM CartItemProduct cp")
    List<Map<String, Object>> findAllWithDetails();

    @Query("SELECT new map(c.id as cartItemId, c.employee.id as employeeId, c.status as status) " +
            "FROM CartItem c WHERE c.id IN :cartItemIds")
    List<Map<String, Object>> findEmployeeDetailsByCartItems(@Param("cartItemIds") List<Integer> cartItemIds);

    @Query("SELECT new map(p.id as productId, p.productName as productName ,p.productImage as productImage, p.price as productPrice) " +
            "FROM Product p WHERE p.id IN :productIds")
    List<Map<String, Object>> findProductDetailsByIds(@Param("productIds") List<Integer> productIds);

    @Transactional
    void deleteByProductId(int productId);


    @Modifying
    @Query("UPDATE CartItem c SET c.status = 1 WHERE c.id = :cartItemId")
    void updateStatusToOrdered(@Param("cartItemId") int cartItemId);
}
