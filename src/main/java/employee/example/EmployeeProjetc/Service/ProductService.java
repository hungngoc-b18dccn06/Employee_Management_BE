package employee.example.EmployeeProjetc.Service;

import employee.example.EmployeeProjetc.DTO.ProductDTO;
import employee.example.EmployeeProjetc.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface ProductService {
    Page<Product> getAllProduct(Pageable pageable);

    String addProductWithImage(ProductDTO productDTO, MultipartFile file);
}
