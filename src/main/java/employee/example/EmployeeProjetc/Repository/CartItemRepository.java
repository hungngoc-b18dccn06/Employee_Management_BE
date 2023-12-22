package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.CartItem;
import employee.example.EmployeeProjetc.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findCartItemByStatusAndEmployee(int status, Employee employee);

    List<CartItem> findByEmployeeAndStatus(Employee employee, int i);

    @Modifying
    @Query("UPDATE CartItem c SET c.status = 1 WHERE c.id = :cartItemId")
    void updateStatusToOrdered(@Param("cartItemId") Integer cartItemId);
}
