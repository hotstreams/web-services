package ifmo.webservices.lab3.exceptions;

public class ProductServiceFault {
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ProductServiceFault defaultInstance() {
        ProductServiceFault fault = new ProductServiceFault();
        fault.message = "ProductService fault has occurred!";
        return fault;
    }
}
