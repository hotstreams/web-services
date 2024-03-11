package ifmo.webservices.lab5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    CustomProductRepository customProductRepository;

    @Autowired
    ProductRepository repository;

    public List<Product> getProducts(final Map<String, String> args) {
        return customProductRepository.getProducts(args);
    }

    private Product mapCreationRequestToEntity(final ProductCreationRequest request) {
        Product product = new Product();
        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setQuantity(request.getQuantity());
        product.setCost(request.getCost());
        return product;
    }

    private void mapUpdateRequestToEntity(final Product product, final ProductUpdateRequest request) {
        if (request.getCode() != null) {
            product.setCode(request.getCode());
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }
        if (request.getCost() != null) {
            product.setCost(request.getCost());
        }
    }

    public Product createProduct(final ProductCreationRequest request) {
        return repository.save(mapCreationRequestToEntity(request));
    }

    public void removeProduct(long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Product updateProduct(long id, final ProductUpdateRequest request) {
        final Product product = repository.findById(id).orElseThrow(RuntimeException::new);
        mapUpdateRequestToEntity(product, request);
        return repository.save(product);
    }
}
