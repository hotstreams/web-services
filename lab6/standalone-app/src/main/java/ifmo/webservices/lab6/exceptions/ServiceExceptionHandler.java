package ifmo.webservices.lab6.exceptions;

import ifmo.webservices.lab6.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceExceptionHandler {
    @ExceptionHandler(UpdateRequestNotValidException.class)
    public ResponseEntity<ErrorResponse> handleUpdateRequestNotValidException(UpdateRequestNotValidException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(CreationRequestNotValidException.class)
    public ResponseEntity<ErrorResponse> handleCreationRequestNotValidException(CreationRequestNotValidException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(UnknownProductParameterException.class)
    public ResponseEntity<ErrorResponse> handleUnknownProductParameterException(UnknownProductParameterException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
}
