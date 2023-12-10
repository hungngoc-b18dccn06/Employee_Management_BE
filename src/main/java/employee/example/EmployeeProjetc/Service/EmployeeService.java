package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.Entity.Employee;
import employee.example.EmployeeProjetc.Response.LoginResponse;


import java.util.List;

public interface EmployeeService {
    public String registerEmployee(Employee employee);
    public List<Employee> getAllEmployees();

    LoginResponse loginEmployee(Employee employee);
}
