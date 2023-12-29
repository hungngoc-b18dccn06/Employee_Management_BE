package employee.example.EmployeeProjetc.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "employee")
public class Employee {
    @Id
    @Column(name="id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="employee_id")
    private String employeeid;

    @Column(name="employee_name")
    private String employeename;

    @Column(name="email" )
    private String email;

    @Column(name="phone")
    private String phone;

    @Column(name="password")
    private String password;

    @Column(name = "role")
    private int role;

    @Column(name = "created_at" )
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "status")
    private int status;

    @PrePersist
    public void createAt() {
        createdAt = new Date();
    }
    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Order> orders;
}
