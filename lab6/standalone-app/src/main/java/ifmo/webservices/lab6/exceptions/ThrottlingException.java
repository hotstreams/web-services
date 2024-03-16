package ifmo.webservices.lab6.exceptions;

public class ThrottlingException extends RuntimeException {
    public ThrottlingException(String message) { super(message); }
}
