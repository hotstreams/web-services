package ifmo.webservices.lab3;

import java.util.Map;

public class ProductValidator {
    public static boolean hasRequiredFieldsForCreation(final Map<String, String> args) {
        return args.containsKey("code") && args.containsKey("name") && args.containsKey("category") && args.containsKey("quantity") && args.containsKey("cost");
    }

    public static boolean hasRequiredFieldsForUpdate(final Map<String, String> args) {
        return args.containsKey("code") || args.containsKey("name") || args.containsKey("category") || args.containsKey("quantity") || args.containsKey("cost");
    }
}
