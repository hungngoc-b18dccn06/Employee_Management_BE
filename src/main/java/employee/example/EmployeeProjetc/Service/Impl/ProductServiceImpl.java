package employee.example.EmployeeProjetc.Service.Impl;

import employee.example.EmployeeProjetc.DTO.ProductDTO;
import employee.example.EmployeeProjetc.Entity.Product;
import employee.example.EmployeeProjetc.Repository.ProductRepository;
import employee.example.EmployeeProjetc.Service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;



    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getAllProduct(Pageable pageable) {
        return null;
    }

    @Transactional
    @Override
    public String addProductWithImage(ProductDTO productDTO , MultipartFile file) {
        if (productDTO.getProductName() != null) {
            try {
                String originalFilename = file.getOriginalFilename();


                if (originalFilename != null && !originalFilename.isEmpty()) {

                    byte[] imageBytes = file.getBytes();
                    String imageFileName = UUID.randomUUID() + "_" + originalFilename;

                    Path imagePath = Paths.get("src/main/resources/images/" + imageFileName);
                    Files.copy(new ByteArrayInputStream((imageBytes)), imagePath, StandardCopyOption.REPLACE_EXISTING);

                    Product product = new Product();
                    product.setProductName(productDTO.getProductName());
                    product.setProductCode(productDTO.getCode());
                    product.setCategory(productDTO.getCategory());
                    product.setReview(Integer.parseInt(productDTO.getReview()));
                    product.setStatus(String.valueOf(productDTO.getStatus()));
                    product.setPrice(productDTO.getPrice());
                    product.setProductImage("src/main/resources/images/" + imageFileName);

                    Product savedProduct = productRepository.save(product);
                    return savedProduct.getProductImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}