package ifmo.webservices.lab6;

public class ProductCreationRequest {
    private Long code;
    private String name;
    private Category category;
    private Long quantity;
    private Long cost;

    public ProductCreationRequest(Long code, String name, Category category, Long quantity, Long cost) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.cost = cost;
    }

    public ProductCreationRequest() {
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getCost() {return cost; }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
