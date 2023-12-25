package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.CartItem;
import employee.example.EmployeeProjetc.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderByCartItem(CartItem cartItem);
}
