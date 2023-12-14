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
    private String review;
    private String category;
    private String status;

    public ProductDTO(String productName, String code, String price, String review, String category, String status) {
        this.productName = productName;
        this.code = code;
        this.price = price;
        this.review = review;
        this.category = category;
        this.status = status;
    }
}

