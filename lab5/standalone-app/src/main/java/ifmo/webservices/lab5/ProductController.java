package ifmo.webservices.lab5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    List<Product> getProducts(@RequestParam final Map<String, String> args) {
        return productService.getProducts(args);
    }

    @PostMapping
    ResponseEntity<Product> createProduct(@RequestBody final ProductCreationRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Product> removeProduct(@PathVariable long id) {
        productService.removeProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody final ProductUpdateRequest request) {
        Product product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }
}
