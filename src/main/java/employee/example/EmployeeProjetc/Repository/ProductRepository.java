package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
