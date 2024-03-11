package ifmo.webservices.lab6;

public class Product {
    private long id;
    private long code;
    private String name;

    private Category category;
    private long quantity;
    private long cost;

    public Product(long id, long code, String name, Category category, long quantity, long cost) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.cost = cost;
    }

    public Product() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                ", cost=" + cost +
                '}';
    }
}
