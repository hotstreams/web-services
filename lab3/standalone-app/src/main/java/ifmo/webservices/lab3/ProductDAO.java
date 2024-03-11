package ifmo.webservices.lab3;

import ifmo.webservices.lab3.exceptions.*;

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

    public List<Product> getProducts(final Map<String, String> args) throws DatabaseException, ValueParsingException, UnknownProductParameterException {
        return args == null ? getAllProducts() : getSpecifiedProducts(args);
    }

    public long createProduct(final Map<String, String> args) throws ProductCreationException, DatabaseException, ValueParsingException {
        return executeInsertQuery(args);
    }

    public boolean updateProduct(long id, final Map<String, String> args) throws ProductUpdateException, DatabaseException, UnknownProductParameterException, ValueParsingException {
        return executeUpdateQuery(id, args);
    }

    public boolean removeProduct(long id) throws ProductRemoveException, DatabaseException {
        return executeDeleteQuery(id);
    }

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

    private List<Product> extractResultFromSet(final ResultSet resultSet) throws DatabaseException {
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
            throw new DatabaseException(ExceptionMessageConstants.DATABASE_ACCESS_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        }
        return products;
    }

    private List<Product> getAllProducts() throws DatabaseException {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractResultFromSet(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException(ExceptionMessageConstants.DATABASE_ACCESS_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        }
    }

    private List<Product> getSpecifiedProducts(final Map<String, String> args) throws DatabaseException, ValueParsingException, UnknownProductParameterException {
        try (Connection conn = PostgresConnection.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_QUERY + convertArgsToWhereClause(args));
            int objectIndex = 1;
            for (Map.Entry<String, String> o : args.entrySet()) {
                switch (o.getKey()) {
                    case "id", "code", "cost", "quantity" -> preparedStatement.setLong(objectIndex++, Long.parseLong(o.getValue()));
                    case "name", "category" -> preparedStatement.setString(objectIndex++, o.getValue());
                    default -> throw new UnknownProductParameterException(ExceptionMessageConstants.UNKNOWN_PARAMETER_MESSAGE, ProductServiceFault.defaultInstance());
                }
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return extractResultFromSet(resultSet);
        } catch (SQLException ex) {
            throw new DatabaseException(ExceptionMessageConstants.DATABASE_ACCESS_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        } catch (NumberFormatException ex) {
            throw new ValueParsingException(ExceptionMessageConstants.VALUE_PARSING_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        }
    }

    private long executeInsertQuery(final Map<String, String> args) throws ValueParsingException, DatabaseException, ProductCreationException {
        try (Connection conn = PostgresConnection.getConnection()) {
            String INSERT_QUERY = "INSERT INTO PRODUCT (code, name, category, quantity, cost) VALUES (?, ?, ?, ?, ?)";
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
                throw new ProductCreationException(ExceptionMessageConstants.PRODUCT_CREATION_MESSAGE, ProductServiceFault.defaultInstance());
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ExceptionMessageConstants.DATABASE_ACCESS_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        } catch (NumberFormatException ex) {
            throw new ValueParsingException(ExceptionMessageConstants.VALUE_PARSING_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        }
    }

    private boolean executeUpdateQuery(long id, final Map<String, String> args) throws UnknownProductParameterException, ProductUpdateException, DatabaseException, ValueParsingException {
        try (Connection conn = PostgresConnection.getConnection()) {
            String UPDATE_QUERY = "UPDATE PRODUCT";
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY + convertArgsToSetClause(id, args));
            int objectIndex = 1;
            for (Map.Entry<String, String> o : args.entrySet()) {
                switch (o.getKey()) {
                    case "code", "cost", "quantity" -> preparedStatement.setLong(objectIndex++, Long.parseLong(o.getValue()));
                    case "name", "category" -> preparedStatement.setString(objectIndex++, o.getValue());
                    default -> throw new UnknownProductParameterException(ExceptionMessageConstants.UNKNOWN_PARAMETER_MESSAGE, ProductServiceFault.defaultInstance());
                }
            }
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new ProductUpdateException(ExceptionMessageConstants.PRODUCT_UPDATE_MESSAGE, ProductServiceFault.defaultInstance());
            }
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            throw new DatabaseException(ExceptionMessageConstants.DATABASE_ACCESS_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        } catch (NumberFormatException ex) {
            throw new ValueParsingException(ExceptionMessageConstants.VALUE_PARSING_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        }
    }

    private boolean executeDeleteQuery(long id) throws ProductRemoveException, DatabaseException {
        try (Connection conn = PostgresConnection.getConnection()) {
            String DELETE_QUERY = "DELETE FROM PRODUCT WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new ProductRemoveException(ExceptionMessageConstants.PRODUCT_UPDATE_MESSAGE, ProductServiceFault.defaultInstance());
            }
            return rowsDeleted > 0;
        } catch (SQLException ex) {
            throw new DatabaseException(ExceptionMessageConstants.DATABASE_ACCESS_MESSAGE, ProductServiceFault.defaultInstance(), ex);
        }
    }
}
