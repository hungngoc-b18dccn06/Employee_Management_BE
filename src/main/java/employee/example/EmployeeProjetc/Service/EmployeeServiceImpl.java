package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.DTO.EmployeeDTO;
import employee.example.EmployeeProjetc.Entity.GlobalExceptionHandler;
import employee.example.EmployeeProjetc.Repository.EmployeeRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, GlobalExceptionHandler globalExceptionHandler){
        this.employeeRepository = employeeRepository;
        this.globalExceptionHandler = globalExceptionHandler;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseEntity<Map<String, Object>> registerEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getEmployeeid())) {
            return  globalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Employee with this ID already exists");
        }

        if (!isValidEmail(employee.getEmail())) {
            return  globalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid email format");
        }
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee != null) {
            return  globalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Email already exists");
        }

        Employee newEmployee = new Employee(
                employee.getId(),
                employee.getEmployeeid(),
                employee.getEmployeename(),
                employee.getEmail(),
                employee.getPhone(),
                passwordEncoder.encode(employee.getPassword()),
                employee.getRole()
        );
        employeeRepository.save(newEmployee);
        return ResponseEntity.ok().body( globalExceptionHandler.buildSuccessResponse("Employee registered successfully: " + newEmployee.getEmployeename()).getBody());
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    @Override
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<EmployeeDTO> searchEmployees(Pageable pageable, Integer role, String searchText) {
        List<Integer> roles = new ArrayList<>();
        if (role == null){
            roles.add(1);
            roles.add(2);
        } else {
            roles.add(role);
        }
        searchText = "%" + searchText + "%";
        return employeeRepository.findByRoleAndEmployeenameContaining( roles, searchText, pageable);
    }
    private final String JWT_SECRET = "A31E8C6D5BF7E9A08C6D7E528A4F01B5E26C9D0F387E4A1B5C76D4E7A9081B5A3E28C76D7A08C6D5B7E9A0B4C76D8A4F012E5C6D9A";
    private final long JWT_EXPIRATION = 604800000L;
    @Override
    public ResponseEntity<String> loginEmployee(Employee employee) {
        Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (foundEmployee != null && passwordEncoder.matches(employee.getPassword(), foundEmployee.getPassword())) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

            String token =  Jwts.builder()
                    .setSubject(foundEmployee.getEmail())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSignInKey(JWT_SECRET), SignatureAlgorithm.HS256)
                    .compact();
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response.toString());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid employee ID or password");
    }
    private Key getSignInKey(String secretKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    public ResponseEntity<String> updateEmployee(int id, Employee employee) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        employee.setId(id);
        employeeRepository.save(employee);

        return ResponseEntity.ok("Employee updated successfully");
    }

    @Override
    public ResponseEntity<Employee> getEmployeeDetail(int id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Employee employee = employeeRepository.findById(id).orElse(null);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<String> deleteEmployee(int id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        employeeRepository.deleteById(id);

        return ResponseEntity.ok("Employee deleted successfully");
    }
}
