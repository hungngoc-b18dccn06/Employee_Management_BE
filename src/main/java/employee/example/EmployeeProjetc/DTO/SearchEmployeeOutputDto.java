package employee.example.EmployeeProjetc.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchEmployeeOutputDto {
    private String employeeid;
    private String employeename;
    private String email;
    private String phone;
    private int role;
    private Integer status;
    private Date createdAt;
    private Integer id;
}
