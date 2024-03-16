package ifmo.webservices.lab6.exceptions;

public class FileUploadException extends RuntimeException {
    public FileUploadException(final String message, Throwable e)  { super(message, e); }

    public FileUploadException(final String message) { super(message); }
}
