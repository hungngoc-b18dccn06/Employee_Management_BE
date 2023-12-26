package employee.example.EmployeeProjetc.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true)
        private int id;

        @ManyToOne
        @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
        private Employee employee;

        @ManyToOne
        @JoinColumn(name = "cart_item_id")
        private CartItem cartItem;

        @Column(name = "order_date")
        private Date orderDate;

        @Column(name = "total_price", precision = 10, scale = 2)
        private String totalPrice;


        @Column(name = "method_payment")
        private String methodPayment;


        @Column(name = "address")
        private String address;


        @Column(name = "order_status")
        private Integer orderStatus;


}
