package ifmo.webservices.lab6.exceptions;

public class UnknownProductParameterException extends RuntimeException {
    public UnknownProductParameterException(final String message) {
        super(message);
    }
}
