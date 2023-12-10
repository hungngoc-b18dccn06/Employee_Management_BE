package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Repository.EmployeeRepository;
import employee.example.EmployeeProjetc.Response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public String registerEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getEmployeeid())) {
            throw new IllegalStateException("Employee with this ID already exists");
        }

        if (!isValidEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        Employee newEmployee = new Employee(
                employee.getEmployeeid(),
                employee.getEmployeename(),
                employee.getEmail(),
                employee.getPhone(),
                passwordEncoder.encode(employee.getPassword())
        );

        employeeRepository.save(newEmployee);
        return "Employee registered successfully: " + newEmployee.getEmployeename();
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
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    @Override
    public LoginResponse loginEmployee(Employee employee) {
        Employee foundEmployee = employeeRepository.findById(employee.getEmployeeid()).orElse(null);
        if (foundEmployee != null && passwordEncoder.matches(employee.getPassword(), foundEmployee.getPassword())) {
            return new LoginResponse("Login Success", true);
        }
        return new LoginResponse("Invalid employee ID or password", false);
    }
}
