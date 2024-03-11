package ifmo.webservices.lab4;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Product[]> response = restTemplate.getForEntity("http://localhost:8082/products", Product[].class);
        Product[] products = response.getBody();

        for (Product product : products) {
            System.out.println("Product{" +
                    "id=" + product.getId() +
                    ", code=" + product.getCode() +
                    ", name='" + product.getName() + '\'' +
                    ", category=" + product.getCategory() +
                    ", quantity=" + product.getQuantity() +
                    ", cost=" + product.getCost() +
                    '}');
        }

        response = restTemplate.getForEntity("http://localhost:8082/products?category=Equipment&code=101", Product[].class);
        products = response.getBody();
        System.out.println("Equipment:");
        for (Product product : products) {
            System.out.println("Product{" +
                    "id=" + product.getId() +
                    ", code=" + product.getCode() +
                    ", name='" + product.getName() + '\'' +
                    ", category=" + product.getCategory() +
                    ", quantity=" + product.getQuantity() +
                    ", cost=" + product.getCost() +
                    '}');
        }

        response = restTemplate.getForEntity("http://localhost:8082/products?id=7", Product[].class);
        products = response.getBody();
        System.out.println("Id = 7:");
        for (Product product : products) {
            System.out.println("Product{" +
                    "id=" + product.getId() +
                    ", code=" + product.getCode() +
                    ", name='" + product.getName() + '\'' +
                    ", category=" + product.getCategory() +
                    ", quantity=" + product.getQuantity() +
                    ", cost=" + product.getCost() +
                    '}');
        }
    }
}
