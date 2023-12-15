package employee.example.EmployeeProjetc.Service.Impl;

import com.mysql.cj.util.StringUtils;
import employee.example.EmployeeProjetc.DTO.EmployeeDTO;
import employee.example.EmployeeProjetc.DTO.RegisterEmployeeRequest;
import employee.example.EmployeeProjetc.DTO.SearchEmployeeOutputDto;
import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Entity.GlobalExceptionHandler;
import employee.example.EmployeeProjetc.Repository.EmployeeRepository;
import employee.example.EmployeeProjetc.Service.EmployeeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final GlobalExceptionHandler globalExceptionHandler;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, GlobalExceptionHandler globalExceptionHandler) {
        this.employeeRepository = employeeRepository;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Map<String, Object>> registerEmployee(RegisterEmployeeRequest employee) throws ParseException {
        if (employeeRepository.existsById(Integer.valueOf(employee.getEmployeeid()))) {
            return globalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Employee with this ID already exists");
        }
        if (!isValidEmail(employee.getEmail())) {
            return globalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid email format");
        }
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee != null) {
            return globalExceptionHandler.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Email already exists");
        }
        Employee newEmployee = Employee.builder()
                .status(0)
                .employeeid(employee.getEmployeeid())
                .employeename(employee.getEmployeename())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .password(passwordEncoder.encode(employee.getPassword())).build();
        employeeRepository.save(newEmployee);
        return ResponseEntity.ok().body(globalExceptionHandler.buildSuccessResponse("Employee registered successfully: " + newEmployee.getEmployeename()).getBody());
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
//    @Override
//    public Page<Employee> searchEmployees(Pageable pageable, Integer role, String searchText, Integer status) {
//        List<Integer> roles = new ArrayList<>();
//        if (role == null) {
//            roles.add(1);
//            roles.add(2);
//        } else {
//            roles.add(role);
//        }
//        if (StringUtils.isNullOrEmpty(searchText)) {
//            searchText = "";
//        } else {
//            searchText = "%" + searchText + "%";
//        }
//
//
//        return employeeRepository.findByRoleAndEmployeenameContainingAndStatus(1, searchText, pageable);
//
//    }

    @Override
    public List<SearchEmployeeOutputDto> searchEmployees(Pageable pageable, Integer role, String searchText, Integer status, String startDate, String endDate) {
        List<Integer> roles = new ArrayList<>();
        if (role == null) {
            roles.add(1);
            roles.add(2);
        } else {
            roles.add(role);
        }
        if (StringUtils.isNullOrEmpty(searchText)) {
            searchText = "";
        } else {
            searchText = "%" + searchText + "%";
        }

        Map<String, Object> input = new HashMap<>();
        StringBuilder query = new StringBuilder("SELECT " +
                "e.employee_id employeeid,\n" +
                "e.employee_name employeename,\n" +
                "e.email email,\n" +
                "e.phone,\n" +
                "e.role,\n" +
                "e.status,\n" +
                "e.created_at createdAt,\n" +
                "e.id id\n" +
                "FROM employee e " +
                "WHERE 1 = 1 ");
        if (!CollectionUtils.isEmpty(roles)) {
            query.append("AND e.role IN (:roles) ");
            input.put("roles", roles);
        }
        if (status != null) {
            query.append("AND e.status = :status ");
            input.put("status", status);
        }
        if (StringUtils.isNullOrEmpty(searchText) && !searchText.trim().equals("")) {
            query.append("AND (e.phone like :searchText OR e.email like :searchText OR e.employee_id like :searchText )");
            input.put("searchText", "%" + searchText + "%");
        }
        if(startDate != null && !startDate.trim().equals("")){
            query.append("AND e.created_at >= :startDate ");
            input.put("startDate", startDate);
        }
        if(endDate != null && !endDate.trim().equals("")){
            query.append("AND e.created_at <= :endDate ");
            input.put("endDate", endDate);
        }
        query.append("ORDER BY e.id asc ");
        query.append("LIMIT :start,:size");
        input.put("start", pageable.getPageSize() * pageable.getPageNumber());
        input.put("size", pageable.getPageSize());
        List<SearchEmployeeOutputDto> result = namedParameterJdbcTemplate.query(query.toString(), input, BeanPropertyRowMapper.newInstance(SearchEmployeeOutputDto.class));
        return result;
    }

    private final String JWT_SECRET = "A31E8C6D5BF7E9A08C6D7E528A4F01B5E26C9D0F387E4A1B5C76D4E7A9081B5A3E28C76D7A08C6D5B7E9A0B4C76D8A4F012E5C6D9A";
    private final long JWT_EXPIRATION = 604800000L;

    @Override
    public ResponseEntity<String> loginEmployee(Employee employee) {
        Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (foundEmployee != null && passwordEncoder.matches(employee.getPassword(), foundEmployee.getPassword())) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
            String token = Jwts.builder()
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
    public ResponseEntity<EmployeeDTO> getCurrentEmployee(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String jwtToken = authorizationHeader.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSignInKey(JWT_SECRET))
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();
                String email = claims.getSubject();
                Employee employee = employeeRepository.findByEmail(email);
                if (employee != null) {
                    EmployeeDTO result = new EmployeeDTO(employee);
                    return ResponseEntity.ok(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
