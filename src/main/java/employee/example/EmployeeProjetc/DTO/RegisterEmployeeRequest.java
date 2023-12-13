package employee.example.EmployeeProjetc.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RegisterEmployeeRequest {
    private String employeeid;
    private String employeename;
    private String email;
    private String phone;
    private int role;
    private String password;
    private int status;
}
