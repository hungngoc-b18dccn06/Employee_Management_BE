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
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Sử dụng GenerationType.IDENTITY cho ID tự tăng
        @Column(name = "id", nullable = false, unique = true)
        private int id;

        @ManyToOne
        @JoinColumn(name = "employee_id")
        private Employee employee;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

        @Column(name = "order_date")
        private Date orderDate;

        @Column(name = "quantity")
        private int quantity;

        @Column(name = "total_price", precision = 10, scale = 2)
        private String totalPrice;
}
