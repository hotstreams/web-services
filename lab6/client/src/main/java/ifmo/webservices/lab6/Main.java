package ifmo.webservices.lab6;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
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
        request.setCode(444L);
        request.setName("New Product x444 Turbo food");
        request.setCategory(Category.Food);

        HttpEntity<ProductCreationRequest> entity = new HttpEntity<>(request);
        try {
            restTemplate.postForObject("http://localhost:8082/products", entity, Product.class);
        } catch (RestClientException ex) {
            System.err.println(ex);
        }

        ProductUpdateRequest request2 = new ProductUpdateRequest();
        RequestEntity<ProductUpdateRequest> entity2 = new RequestEntity<>(request2, HttpMethod.PUT, URI.create("http://localhost:8082/products/" + 12));
        try {
            restTemplate.exchange(entity2, Product.class);
        } catch (RestClientException ex) {
            System.err.println(ex);
        }

        try {
            restTemplate.getForEntity("http://localhost:8082/products?test=123", Product[].class);
        } catch (RestClientException ex) {
            System.err.println(ex);
        }
    }
}
