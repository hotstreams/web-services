package ifmo.webservices.lab3;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import javax.xml.ws.Endpoint;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress("localhost", 8080), 0);
        HttpContext context = server.createContext("/ProductService");
        context.setAuthenticator(new BasicAuthenticator("realm") {
            @Override
            public boolean checkCredentials(String username, String password) {
                return "username".equals(username) && "password".equals(password);
            }
        });
        Endpoint.create(new ProductService()).publish(context);
        server.start();
    }
}
