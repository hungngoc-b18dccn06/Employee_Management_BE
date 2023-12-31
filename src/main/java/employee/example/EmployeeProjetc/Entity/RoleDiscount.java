package employee.example.EmployeeProjetc.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "role_discount")
public class RoleDiscount {
    @Id
    @Column(name="id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "role", unique = true, nullable = false)
    private String role;

    @Column(name = "discount", precision = 5, scale = 2)
    private Integer discount;
}
