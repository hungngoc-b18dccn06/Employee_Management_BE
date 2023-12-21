package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.CartItem;
import employee.example.EmployeeProjetc.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findCartItemByStatusAndEmployee(int status, Employee employee);
}
