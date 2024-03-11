package ifmo.webservices.lab3.exceptions;

import javax.xml.ws.WebFault;

@WebFault(faultBean = "ProductServiceFault")
public class UnknownProductParameterException extends Exception {
    private static final long serialVersionUID = -6647544772732631047L;
    private final ProductServiceFault fault;

    public UnknownProductParameterException(String message, ProductServiceFault fault) {
        super(message);
        this.fault = fault;
    }

    public UnknownProductParameterException(String message, ProductServiceFault fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public ProductServiceFault getFaultInfo() {
        return fault;
    }
}
