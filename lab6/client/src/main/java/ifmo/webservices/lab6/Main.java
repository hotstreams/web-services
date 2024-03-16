package ifmo.webservices.lab6;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private String getAuthHeader(String username, String password) {
        String str = username + ":" + password;
        return "Basic " + Base64.encodeBase64String(str.getBytes());
    }

    private void checkTrottling() {
        ExecutorService service = Executors.newFixedThreadPool(6);

        for (int i = 0; i < 6; ++i) {
            service.submit(this::task);
        }
        service.shutdown();
    }

    private void task() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getAuthHeader("username", "password"));

        try {
            restTemplate.exchange("http://localhost:8082/products?category=Equipment", HttpMethod.GET, new HttpEntity<Object>(headers), Product[].class);
        } catch (RestClientException ex) {
            System.err.println(ex);
        }
    }

    public static Resource getTestFile() throws IOException {
        Path testFile = Files.createTempFile("test-file", ".txt");
        Files.write(testFile, "Test file".getBytes());
        return new FileSystemResource(testFile.toFile());
    }

    public Product postRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getAuthHeader("username", "password"));

        ProductCreationRequest request = new ProductCreationRequest();
        request.setCode(444L);
        request.setName("New Product x444 Turbo food");
        request.setCategory(Category.Food);

        HttpEntity<ProductCreationRequest> entity = new HttpEntity<>(request, headers);
        return restTemplate.postForObject("http://localhost:8082/products", entity, Product.class);
    }

    public Product putRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getAuthHeader("username", "password"));

        ProductUpdateRequest request = new ProductUpdateRequest();
        RequestEntity<ProductUpdateRequest> entity2 = new RequestEntity<>(request, headers, HttpMethod.PUT, URI.create("http://localhost:8082/products/" + 12));

        return restTemplate.exchange(entity2, Product.class).getBody();
    }

    public Product[] getRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getAuthHeader("username", "password"));

        return restTemplate.exchange("http://localhost:8082/products?test=123", HttpMethod.GET, new HttpEntity<Object>(headers), Product[].class).getBody();
    }

    public void checkFileUpload() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getAuthHeader("username", "password"));

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", getTestFile());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Resource> response = restTemplate.postForEntity("http://localhost:8082/products/files", requestEntity, Resource.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            System.err.println(response);
        }

        response = restTemplate.exchange("http://localhost:8082/products/files?name=file", HttpMethod.GET, new HttpEntity<Object>(headers), Resource.class);
        System.out.println(response.getBody().getContentAsString(Charset.defaultCharset()));
    }

    @Override
    public void run(String... args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);

        List<Future<?>> futures = new ArrayList<>();

        futures.add(service.submit(this::postRequest));
        futures.add(service.submit(this::getRequest));
        futures.add(service.submit(this::putRequest));

        service.shutdown();

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception ex) {
                System.err.println(ex);
            }
        }

        //checkFileUpload();
        //checkTrottling();
    }
}
