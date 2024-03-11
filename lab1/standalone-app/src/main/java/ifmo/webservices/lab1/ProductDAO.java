package ifmo.webservices.lab1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {
    private final String SELECT_ALL_QUERY = "SELECT id, code, name, category, quantity, cost FROM PRODUCT";

    private String convertArgsToWhereClause(final Map<String, String> args) {
        final StringBuilder builder = new StringBuilder(" WHERE");
        args.forEach((k, v) -> builder.append(String.format(" %s=? AND", k)));
        builder.delete(builder.length() - 4, builder.length());
        return builder.toString();
    }

    private List<Product> extractResultFromSet(final ResultSet resultSet) {
        final List<Product> products = new ArrayList<>();
        try {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long code = resultSet.getLong("code");
                String name = resultSet.getString("name");
                Category category = Category.valueOf(resultSet.getString("category"));
                long quantity = resultSet.getLong("quantity");
                long cost = resultSet.getLong("cost");
                products.add(new Product(id, code, name, category, quantity, cost));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        return products;
    }

    private List<Product> getAllProducts() {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractResultFromSet(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    private List<Product> getSpecifiedProducts(final Map<String, String> args) {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_QUERY + convertArgsToWhereClause(args));
            int objectIndex = 1;
            for (Map.Entry<String, String> o : args.entrySet()) {
                switch (o.getKey()) {
                    case "code", "cost", "quantity" -> preparedStatement.setLong(objectIndex++, Long.parseLong(o.getValue()));
                    case "name", "category" -> preparedStatement.setString(objectIndex++, o.getValue());
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractResultFromSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts(final Map<String, String> args) {
        return args == null ? getAllProducts() : getSpecifiedProducts(args);
    }
}
