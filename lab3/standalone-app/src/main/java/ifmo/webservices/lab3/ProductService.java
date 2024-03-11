package ifmo.webservices.lab3;

import ifmo.webservices.lab3.exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "ProductService")
public class ProductService {
    @WebMethod(operationName = "getProducts")
    public List<Product> getProducts(final Map<String, String> args) throws DatabaseException, ValueParsingException, UnknownProductParameterException {
        return new ProductDAO().getProducts(args);
    }

    @WebMethod(operationName = "createProduct")
    public long createProduct(final Map<String, String> args) throws ProductCreationException, DatabaseException, ValueParsingException, MissingFieldsException {
        if (!ProductValidator.hasRequiredFieldsForCreation(args)) {
            throw new MissingFieldsException(ExceptionMessageConstants.MISSING_FIELDS_MESSAGE, ProductServiceFault.defaultInstance());
        }
        return new ProductDAO().createProduct(args);
    }

    @WebMethod(operationName = "updateProduct")
    public boolean updateProduct(long id, final Map<String, String> args) throws ProductUpdateException, DatabaseException, UnknownProductParameterException, MissingFieldsException, ValueParsingException {
        if (!ProductValidator.hasRequiredFieldsForUpdate(args)) {
            throw new MissingFieldsException(ExceptionMessageConstants.MISSING_FIELDS_MESSAGE, ProductServiceFault.defaultInstance());
        }
        return new ProductDAO().updateProduct(id, args);
    }

    @WebMethod(operationName = "removeProduct")
    public boolean removeProduct(long id) throws ProductRemoveException, DatabaseException {
        return new ProductDAO().removeProduct(id);
    }
}
