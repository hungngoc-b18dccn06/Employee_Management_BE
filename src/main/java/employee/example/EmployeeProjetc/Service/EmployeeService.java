package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.DTO.EmployeeDTO;
import employee.example.EmployeeProjetc.DTO.RegisterEmployeeRequest;
import employee.example.EmployeeProjetc.DTO.SearchEmployeeOutputDto;
import employee.example.EmployeeProjetc.Entity.Employee;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    public ResponseEntity<Map<String, Object>> registerEmployee(RegisterEmployeeRequest employee) throws ParseException;
    Page<Employee> getAllEmployees(Pageable pageable);

    List<SearchEmployeeOutputDto> searchEmployees(Pageable pageable, Integer roleType, String searchText , Integer status,String startDate,String endDate);

    ResponseEntity<String> loginEmployee(Employee employee);

    public ResponseEntity<String> updateEmployee(int id, Employee employee);

    public ResponseEntity<Employee> getEmployeeDetail(int id);

    public ResponseEntity<String> deleteEmployee(int id);

    public ResponseEntity<EmployeeDTO> getCurrentEmployee(HttpServletRequest request);

}
