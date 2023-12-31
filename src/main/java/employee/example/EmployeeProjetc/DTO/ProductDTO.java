package employee.example.EmployeeProjetc.DTO;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class ProductDTO {
    private String productName;
    private String code;
    private String price;
    private String average_rating ;
    private String category;
    private String status;
    private String description;
    private String quantity;


    public ProductDTO(String productName, String code, String price, String average_rating , String category, String status, String quantity, String description) {
        this.productName = productName;
        this.code = code;
        this.price = price;
        this.average_rating  = average_rating ;
        this.category = category;
        this.status = status;
        this.quantity = quantity;
        this.description = description;
    }
}

