package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.CartItemProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemProductRepository extends JpaRepository<CartItemProduct, Integer> {
    CartItemProduct findCartItemProductByCartItemIdAndProductId(int a, int b);
}
