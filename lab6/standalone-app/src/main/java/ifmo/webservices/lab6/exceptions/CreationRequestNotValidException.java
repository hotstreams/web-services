package ifmo.webservices.lab6.exceptions;

public class CreationRequestNotValidException extends RuntimeException {
    public CreationRequestNotValidException(final String message) {
        super(message);
    }
}
