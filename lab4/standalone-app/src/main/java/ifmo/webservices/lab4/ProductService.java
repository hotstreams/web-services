package ifmo.webservices.lab4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    CustomProductRepository repository;

    public List<Product> getProducts(final Map<String, String> args) {
        return repository.getProducts(args);
    }
}
