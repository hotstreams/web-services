package ifmo.webservices.lab6;

import ifmo.webservices.lab6.exceptions.FileUploadException;
import ifmo.webservices.lab6.exceptions.ProductNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    CustomProductRepository customProductRepository;

    @Autowired
    ProductRepository repository;

    @Autowired
    FileRepository fileRepository;

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
        final Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException("id = " + id));
        mapUpdateRequestToEntity(product, request);
        return repository.save(product);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get("../resources"));
        } catch (IOException ex) {
            throw new FileUploadException("Could not create upload directory", ex);
        }
    }

    public Resource load(String filename) {
        File file = fileRepository.findById(filename).orElseThrow(() -> new FileUploadException("Not found"));
        return new ByteArrayResource(file.getData());
    }

    public void uploadFile(MultipartFile file) {
        try {
            fileRepository.save(new File(file.getName(), file.getBytes()));
        } catch (IOException ex) {
            System.out.println(ex);
            throw new FileUploadException("Error occurred during file upload", ex);
        }
    }
}
