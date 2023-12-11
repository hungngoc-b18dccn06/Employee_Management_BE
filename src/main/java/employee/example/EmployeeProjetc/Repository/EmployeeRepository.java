package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Entity.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findOneByEmailAndPassword(String email, String password);
    Employee findByEmail(String email);

    @Query(value = "select new employee.example.EmployeeProjetc.Entity.EmployeeDTO(e) from Employee e " +
            "where e.role in ?1 and (e.employeename like %?2% or e.email like %?2% or e.phone like %?2% or CAST(e.role AS string) like %?2%)")

    Page<EmployeeDTO> findByRoleAndEmployeenameContaining(List<Integer> roles, String employeename, Pageable pageable);
}
