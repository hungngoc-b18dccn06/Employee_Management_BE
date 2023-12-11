package employee.example.EmployeeProjetc.Entity;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class EmployeeDTO {
    private int employeeid;
    private String employeename;
    private String email;
    private String phone;
    private int role;

    public EmployeeDTO(int employeeid, String employeename, String email, String phone, int role) {
        this.employeeid = employeeid;
        this.employeename = employeename;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
    public EmployeeDTO(Employee employee) {
        this.employeeid = employee.getEmployeeid();
        this.email = employee.getEmail();
        this.employeename = employee.getEmployeename();
        this.phone = employee.getPhone();
        this.role = employee.getRole();
    }
}
