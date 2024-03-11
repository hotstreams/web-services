package ifmo.webservices.lab2;

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
    private final String INSERT_QUERY = "INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (?, ?, ?, ?, ?)";
    private final String DELETE_QUERY = "DELETE FROM PRODUCT WHERE id = ?";
    private final String UPDATE_QUERY = "UPDATE PRODUCT";

    private String convertArgsToWhereClause(final Map<String, String> args) {
        final StringBuilder builder = new StringBuilder(" WHERE");
        args.forEach((k, v) -> builder.append(String.format(" %s=? AND", k)));
        builder.delete(builder.length() - 4, builder.length());
        return builder.toString();
    }

    private String convertArgsToSetClause(long id, final Map<String, String> args) {
        final StringBuilder builder = new StringBuilder(" SET");
        args.forEach((k, v) -> builder.append(String.format(" %s=? AND", k)));
        builder.delete(builder.length() - 4, builder.length());
        builder.append(" WHERE id = ").append(id);
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
                    case "id", "code", "cost", "quantity" -> preparedStatement.setLong(objectIndex++, Long.parseLong(o.getValue()));
                    case "name", "category" -> preparedStatement.setString(objectIndex++, o.getValue());
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractResultFromSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasRequiredFieldsForCreation(final Map<String, String> args) {
        return args.containsKey("code") && args.containsKey("name") && args.containsKey("category") && args.containsKey("quantity") && args.containsKey("cost");
    }

    private boolean hasRequiredFieldsForUpdate(final Map<String, String> args) {
        return args.containsKey("code") || args.containsKey("name") || args.containsKey("category") || args.containsKey("quantity") || args.containsKey("cost");
    }

    private long insertProduct(final Map<String, String> args) {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1,  Long.parseLong(args.get("code")));
            preparedStatement.setString(2,  args.get("name"));
            preparedStatement.setString(3,  args.get("category"));
            preparedStatement.setLong(4,  Long.parseLong(args.get("quantity")));
            preparedStatement.setLong(5,  Long.parseLong(args.get("cost")));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean executeUpdateQuery(long id, final Map<String, String> args) {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY + convertArgsToSetClause(id, args));
            int objectIndex = 1;
            for (Map.Entry<String, String> o : args.entrySet()) {
                switch (o.getKey()) {
                    case "code", "cost", "quantity" -> preparedStatement.setLong(objectIndex++, Long.parseLong(o.getValue()));
                    case "name", "category" -> preparedStatement.setString(objectIndex++, o.getValue());
                }
            }
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean executeDeleteQuery(long id) {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts(final Map<String, String> args) {
        return args == null ? getAllProducts() : getSpecifiedProducts(args);
    }

    public long createProduct(final Map<String, String> args) {
        return hasRequiredFieldsForCreation(args) ? insertProduct(args) : -1;
    }

    public boolean updateProduct(long id, final Map<String, String> args) {
        return hasRequiredFieldsForUpdate(args) && executeUpdateQuery(id, args);
    }

    public boolean removeProduct(long id) {
        return executeDeleteQuery(id);
    }
}
