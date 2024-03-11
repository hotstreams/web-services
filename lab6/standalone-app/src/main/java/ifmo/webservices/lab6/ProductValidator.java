package ifmo.webservices.lab6;

public class ProductValidator {
    public static boolean validateCreationRequest(final ProductCreationRequest request) {
        return request.getName() != null && request.getCode() != null && request.getCategory() != null && request.getQuantity() != null && request.getCost() != null;
    }

    public static boolean validateUpdateRequest(final ProductUpdateRequest request) {
        return request.getName() != null || request.getCode() != null || request.getCategory() != null || request.getQuantity() != null || request.getCost() != null;
    }
}
