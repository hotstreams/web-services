package ifmo.webservices.lab3;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8081/ProductService", new ProductService());
    }
}
