package employee.example.EmployeeProjetc.Controller;


import employee.example.EmployeeProjetc.DTO.EmployeeDTO;
import employee.example.EmployeeProjetc.DTO.FilterValue;
import employee.example.EmployeeProjetc.DTO.RegisterEmployeeRequest;
import employee.example.EmployeeProjetc.DTO.SearchEmployeeOutputDto;
import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Service.EmployeeService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
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

    @PostMapping(path = "/list")
    public Page<SearchEmployeeOutputDto> searchAllEmployees(@RequestBody FilterValue filterValue) {
        PageRequest pageRequest = PageRequest.of(filterValue.getPageIndex(), filterValue.getPageSize());
        List<SearchEmployeeOutputDto> rs = employeeService.searchEmployees(pageRequest, filterValue);
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

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportEmployeeToExcel(@RequestBody FilterValue filterValue) {
        try {
            Workbook workbook = employeeService.exportEmployeeToExcel(filterValue);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();
            outputStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "employees.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting data to Excel".getBytes());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/export-csv")
    public ResponseEntity<byte[]> exportEmployeeToCsv(@RequestBody FilterValue filterValue) {
        try {
            String csvData = employeeService.exportEmployeeToCsv(filterValue);
            byte[] csvBytes = csvData.getBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "employees.csv");

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error exporting data to CSV".getBytes());
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateEmployeeProfile(HttpServletRequest request, @RequestBody EmployeeDTO updatedEmployeeDTO) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return employeeService.updateEmployeeProfile(authorizationHeader.substring(7), updatedEmployeeDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

}
