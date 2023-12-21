package employee.example.EmployeeProjetc.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item_product")
public class CartItemProduct {

    @Id
    @Column(name="id", length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @Column(name = "quantity")
    private int quantity;


    @Column(name = "cart_item_id")
    private int cartItemId;


    @Column(name = "product_id")
    private int productId;
}
