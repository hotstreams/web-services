package ifmo.webservices.lab6.exceptions;

public class UpdateRequestNotValidException extends RuntimeException {
    public UpdateRequestNotValidException(final String message) {
        super(message);
    }
}
