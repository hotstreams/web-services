package ifmo.webservices.lab1;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ProductService", new ProductService());
    }
}
