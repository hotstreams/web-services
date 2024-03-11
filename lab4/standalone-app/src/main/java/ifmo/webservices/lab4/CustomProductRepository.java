package ifmo.webservices.lab4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProductRepository {
    @Autowired
    private ProductRepository repository;

    private Specification<Product> byId(long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

    private Specification<Product> byCode(long code) {
        return (root, query, builder) -> builder.equal(root.get("code"), code);
    }

    private Specification<Product> byName(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }

    private Specification<Product> byCategory(Category category) {
        return (root, query, builder) -> builder.equal(root.get("category"), category);
    }

    private Specification<Product> byQuantity(long quantity) {
        return (root, query, builder) -> builder.equal(root.get("quantity"), quantity);
    }

    private Specification<Product> byCost(long cost) {
        return (root, query, builder) -> builder.equal(root.get("cost"), cost);
    }

    public List<Product> getProducts(final Map<String, String> args) {
        List<Specification<Product>> specifications = new ArrayList<>();
        for (Map.Entry<String, String> entry : args.entrySet()) {
            switch (entry.getKey()) {
                case "id" -> specifications.add(byId(Long.parseLong(entry.getValue())));
                case "code" -> specifications.add(byCode(Long.parseLong(entry.getValue())));
                case "name" -> specifications.add(byName(entry.getValue()));
                case "category" -> specifications.add(byCategory(Category.valueOf(entry.getValue())));
                case "quantity" -> specifications.add(byQuantity(Long.parseLong(entry.getValue())));
                case "cost" -> specifications.add(byCost(Long.parseLong(entry.getValue())));
                default -> throw new RuntimeException("Unknown product parameter");
            }
        }

        return repository.findAll(Specification.allOf(specifications));
    }
}
