package employee.example.EmployeeProjetc.Repository;

import employee.example.EmployeeProjetc.Entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findOneByEmailAndPassword(String email, String password);
    Employee findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.role IN ?1 AND (e.employeeid LIKE %?2%  OR e.employeename LIKE %?2% OR e.email LIKE %?2% OR e.phone LIKE %?2%)")
    Page<Employee> findByRoleAndEmployeenameContaining(List<Integer> roles, String search, Pageable pageable);


    @Query(value = "SELECT * FROM employee e WHERE e.role = :role AND (COALESCE(:employeeId, '') = '' or e.employeeId = :employeeId) ", nativeQuery = true)
    Page<Employee> findByRoleAndEmployeenameContainingAndStatus(@RequestParam Integer role, @RequestParam String employeeId, Pageable pageable);


    @Query("SELECT e FROM Employee e WHERE e.employeeid = :employeeId")
    Optional<Employee> findEmployeeByEmployeeId(@Param("employeeId") String employeeId);

}
