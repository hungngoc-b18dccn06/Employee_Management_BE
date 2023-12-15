package employee.example.EmployeeProjetc.Controller;

import employee.example.EmployeeProjetc.DTO.ProductDTO;
import employee.example.EmployeeProjetc.Entity.Product;
import employee.example.EmployeeProjetc.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                       @RequestPart ("category") String category,
                                                       @RequestPart ("average_rating") String average_rating,
                                                       @RequestPart ("file") MultipartFile file){
        ProductDTO productDTO = new ProductDTO(productName,null,price, average_rating, category, status );
         String result = productService.addProductWithImage(productDTO, file);
         if(result == null || result.trim().isEmpty()){
             ResponseEntity.badRequest().body("Product registration failed") ;
         }
        return ResponseEntity.status(HttpStatus.OK).body("Product registration Success");
    }

    @GetMapping(path = "/list")
    public Page<Product> getAllEmployees(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return productService.getAllProduct(pageRequest);
    }


}
