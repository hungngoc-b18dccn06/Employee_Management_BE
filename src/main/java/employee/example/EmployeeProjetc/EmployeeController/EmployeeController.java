package employee.example.EmployeeProjetc.EmployeeController;


import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Response.LoginResponse;
import employee.example.EmployeeProjetc.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping(path = "/save")
    public String saveEmployee(@RequestBody Employee employee)
    {
        String id = employeeService.registerEmployee(employee);
        return  id;
    }

    @GetMapping("/list")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(@RequestBody Employee employee)
    {
        LoginResponse loginResponse = employeeService.loginEmployee(employee);
        return ResponseEntity.ok(loginResponse);
    }
}
