package employee.example.EmployeeProjetc.DTO;

import employee.example.EmployeeProjetc.Entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private Employee employee;
    private Integer cartItemId;
    private Date orderDate;
    private String totalPrice;
    private String methodPayment;
    private String address;
    private Integer orderStatus;
}