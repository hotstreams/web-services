package ifmo.webservices.lab1;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "ProductService")
public class ProductService {
    @WebMethod(operationName = "getProducts")
    public List<Product> getProducts(final Map<String, String> args) {
        return new ProductDAO().getProducts(args);
    }
}
