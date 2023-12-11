package employee.example.EmployeeProjetc.EmployeeController;


import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Entity.EmployeeDTO;
import employee.example.EmployeeProjetc.Response.LoginResponse;
import employee.example.EmployeeProjetc.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping(path = "/register")
    public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
        ResponseEntity<Map<String, Object>> responseEntity = employeeService.registerEmployee(employee);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(responseEntity.getBody().get("message").toString());
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody().get("error_message").toString());
        }
    }

    @GetMapping(path = "/list")
    public Page<EmployeeDTO> getAllEmployees(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(name = "role_type", defaultValue = "") Integer role,
                                             @RequestParam(name = "search_text", defaultValue = "") String searchText) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return employeeService.searchEmployees(pageRequest, role, searchText);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(@RequestBody Employee employee)
    {
        ResponseEntity<String> loginResponse = employeeService.loginEmployee(employee);
        return ResponseEntity.ok(loginResponse);
    }
}
