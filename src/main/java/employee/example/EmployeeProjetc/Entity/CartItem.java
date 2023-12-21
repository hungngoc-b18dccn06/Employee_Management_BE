package employee.example.EmployeeProjetc.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItem {

    @Id
    @Column(name = "id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;


    @Column(name = "status")
    private int status;

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", employee=" + employee +
                ", status=" + status +
                '}';
    }
}
