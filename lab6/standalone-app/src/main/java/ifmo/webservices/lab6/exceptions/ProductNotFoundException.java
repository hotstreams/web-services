package ifmo.webservices.lab6.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(final String message) {
        super(message);
    }
}
