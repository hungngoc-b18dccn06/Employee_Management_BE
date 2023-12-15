package employee.example.EmployeeProjetc.Controller;


import employee.example.EmployeeProjetc.DTO.EmployeeDTO;
import employee.example.EmployeeProjetc.DTO.FilterValue;
import employee.example.EmployeeProjetc.DTO.RegisterEmployeeRequest;
import employee.example.EmployeeProjetc.DTO.SearchEmployeeOutputDto;
import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerEmployee(@RequestBody RegisterEmployeeRequest employee) throws ParseException {
        ResponseEntity<Map<String, Object>> responseEntity = employeeService.registerEmployee(employee);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(responseEntity.getBody().get("message").toString());
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody().get("error_message").toString());
        }
    }

//    @GetMapping(path = "/list")
//    public Page<Employee> getAllEmployees(@RequestParam(defaultValue = "1") int page,
//                                          @RequestParam(defaultValue = "10") int size) {
//        PageRequest pageRequest = PageRequest.of(page - 1, size);
//        return employeeService.getAllEmployees(pageRequest);
//    }

    @PostMapping(path = "/list")
    public Page<SearchEmployeeOutputDto> searchAllEmployees(@RequestBody FilterValue filterValue) {
        PageRequest pageRequest = PageRequest.of(filterValue.getPageIndex(), filterValue.getPageSize());
        Integer role = (Integer) filterValue.getFilterValue().get("role");
        Integer status = (Integer) filterValue.getFilterValue().get("status");
        String searchText = (String) filterValue.getFilterValue().get("search_text");
        String startDate = (String) filterValue.getFilterValue().get("startDate");
        String endDate = (String) filterValue.getFilterValue().get("endDate");
        List<SearchEmployeeOutputDto> rs = employeeService.searchEmployees(pageRequest, role, searchText, status, startDate, endDate);
        return new PageImpl<SearchEmployeeOutputDto>(rs, pageRequest, rs.size());
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginEmployee(@RequestBody Employee employee) {
        ResponseEntity<String> loginResponse = employeeService.loginEmployee(employee);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successfull");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        ResponseEntity<String> responseEntity = employeeService.updateEmployee(id, employee);
        return responseEntity;
    }

    @GetMapping("/current-employee")
    public ResponseEntity<EmployeeDTO> getCurrentEmployee() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return employeeService.getCurrentEmployee(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeDetail(@PathVariable int id) {
        ResponseEntity<Employee> responseEntity = employeeService.getEmployeeDetail(id);
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        ResponseEntity<String> responseEntity = employeeService.deleteEmployee(id);
        return responseEntity;
    }
}
