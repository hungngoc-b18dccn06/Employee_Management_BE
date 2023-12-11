package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.DTO.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.Map;

public interface EmployeeService {
    public ResponseEntity<Map<String, Object>> registerEmployee(Employee employee);
    Page<Employee> getAllEmployees(Pageable pageable);

    Page<EmployeeDTO> searchEmployees(Pageable pageable, Integer roleType, String searchText);

    ResponseEntity<String> loginEmployee(Employee employee);

    public ResponseEntity<String> updateEmployee(int id, Employee updatedEmployee);

    public ResponseEntity<Employee> getEmployeeDetail(int id);

    public ResponseEntity<String> deleteEmployee(int id);

}
