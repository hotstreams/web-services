package ifmo.webservices.lab5;

public class ProductCreationRequest {
    private long code;
    private String name;
    private Category category;
    private long quantity;
    private long cost;

    public ProductCreationRequest(long code, String name, Category category, long quantity, long cost) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.cost = cost;
    }

    public ProductCreationRequest() {
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
