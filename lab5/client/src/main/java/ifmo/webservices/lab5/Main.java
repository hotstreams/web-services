package ifmo.webservices.lab5;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        ProductCreationRequest request = new ProductCreationRequest();
        request.setCode(444);
        request.setName("New Product x444 Turbo food");
        request.setCategory(Category.Food);
        request.setQuantity(1);
        request.setCost(2300);

        HttpEntity<ProductCreationRequest> entity = new HttpEntity<>(request);
        Product product = restTemplate.postForObject("http://localhost:8082/products", entity, Product.class);
        System.out.println("Created product: " + product);

        ProductUpdateRequest request2 = new ProductUpdateRequest();
        request2.setCategory(Category.Stationery);
        request2.setCost(4000L);

        RequestEntity<ProductUpdateRequest> entity2 = new RequestEntity<>(request2, HttpMethod.PUT, URI.create("http://localhost:8082/products/" + product.getId()));
        ResponseEntity<Product> response = restTemplate.exchange(entity2, Product.class);
        System.out.println("Updated product: " + response.getBody());

        RequestEntity<ProductUpdateRequest> entity3 = new RequestEntity<>(HttpMethod.DELETE, URI.create("http://localhost:8082/products/" + 5));
        response = restTemplate.exchange(entity3, Product.class);
        System.out.println(response);
    }
}
