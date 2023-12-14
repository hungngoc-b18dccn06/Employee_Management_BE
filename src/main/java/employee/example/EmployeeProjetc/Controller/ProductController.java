package employee.example.EmployeeProjetc.Controller;

import employee.example.EmployeeProjetc.DTO.ProductDTO;
import employee.example.EmployeeProjetc.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/add")
    public ResponseEntity<Object> addProductWithImage (@RequestPart ("productName") String productName,
                                                       @RequestPart ("price") String price,
                                                       @RequestPart ("code") String code,
                                                       @RequestPart ("status") String status,
                                                       @RequestPart ("price") String category,
                                                       @RequestPart ("price") String review,
                                                       @RequestPart ("file") MultipartFile file){
        ProductDTO productDTO = new ProductDTO(productName,code,price, review, category, status );
         String result = productService.addProductWithImage(productDTO, file);
         if(result == null || result.trim().isEmpty()){
             ResponseEntity.badRequest().body("Product registration failed") ;
         }
        return ResponseEntity.status(HttpStatus.OK).body("Product registration Success");
    }

}
