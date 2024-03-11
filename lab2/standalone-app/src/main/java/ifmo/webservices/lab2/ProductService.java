package ifmo.webservices.lab2;

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

    @WebMethod(operationName = "createProduct")
    public long createProduct(final Map<String, String> args) {
        return new ProductDAO().createProduct(args);
    }

    @WebMethod(operationName = "updateProduct")
    public boolean updateProduct(long id, final Map<String, String> args) {
        return new ProductDAO().updateProduct(id, args);
    }

    @WebMethod(operationName = "removeProduct")
    public boolean removeProduct(long id) {
        return new ProductDAO().removeProduct(id);
    }
}
