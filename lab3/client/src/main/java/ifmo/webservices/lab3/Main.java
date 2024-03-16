package ifmo.webservices.lab3;

import ifmo.webservices.lab3.generated.*;

import javax.xml.ws.BindingProvider;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException, DatabaseException, UnknownProductParameterException, ValueParsingException, MissingFieldsException, ProductCreationException, ProductUpdateException, ProductRemoveException {
        URL url = new URL("http://localhost:8080/ProductService?wsdl");

        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("username", "password".toCharArray());
            }
        });

        ProductService_Service productService = new ProductService_Service(url);
        ProductService service = productService.getProductServicePort();

        System.out.println("All products: ");
        List<Product> products = service.getProducts(new GetProducts().getArg0());
        for (Product product : products) {
            System.out.println("Product {" +
                    "id=" + product.getId() +
                    ", code=" + product.getCode() +
                    ", name='" + product.getName() + '\'' +
                    ", category=" + product.getCategory() +
                    ", quantity=" + product.getQuantity() +
                    ", cost=" + product.getCost() +
                    '}');
        }

        {
            GetProducts.Arg0 arg = new GetProducts.Arg0();

            GetProducts.Arg0.Entry entry = new GetProducts.Arg0.Entry();
            entry.setKey("category");
            entry.setValue("Food");

            arg.getEntry().add(entry);

            System.out.println("Products by category: ");
            List<Product> productsByCategory = productService.getProductServicePort().getProducts(arg);
            for (Product product : productsByCategory) {
                System.out.println("Product {" +
                        "id=" + product.getId() +
                        ", code=" + product.getCode() +
                        ", name='" + product.getName() + '\'' +
                        ", category=" + product.getCategory() +
                        ", quantity=" + product.getQuantity() +
                        ", cost=" + product.getCost() +
                        '}');
            }
        }

        {
            GetProducts.Arg0 arg = new GetProducts.Arg0();

            GetProducts.Arg0.Entry entry = new GetProducts.Arg0.Entry();
            entry.setKey("name");
            entry.setValue("Household soap");

            arg.getEntry().add(entry);

            System.out.println("Products by name: ");
            List<Product> productsByCategory = productService.getProductServicePort().getProducts(arg);
            for (Product product : productsByCategory) {
                System.out.println("Product {" +
                        "id=" + product.getId() +
                        ", code=" + product.getCode() +
                        ", name='" + product.getName() + '\'' +
                        ", category=" + product.getCategory() +
                        ", quantity=" + product.getQuantity() +
                        ", cost=" + product.getCost() +
                        '}');
            }
        }

        {
            GetProducts.Arg0 arg = new GetProducts.Arg0();

            GetProducts.Arg0.Entry entry = new GetProducts.Arg0.Entry();
            entry.setKey("code");
            entry.setValue("205");

            arg.getEntry().add(entry);

            System.out.println("Products by code: ");
            List<Product> productsByCategory = productService.getProductServicePort().getProducts(arg);
            for (Product product : productsByCategory) {
                System.out.println("Product {" +
                        "id=" + product.getId() +
                        ", code=" + product.getCode() +
                        ", name='" + product.getName() + '\'' +
                        ", category=" + product.getCategory() +
                        ", quantity=" + product.getQuantity() +
                        ", cost=" + product.getCost() +
                        '}');
            }
        }

        {
            CreateProduct.Arg0 arg = new CreateProduct.Arg0();

            CreateProduct.Arg0.Entry code = new CreateProduct.Arg0.Entry();
            code.setKey("code");
            code.setValue("101");

            CreateProduct.Arg0.Entry name = new CreateProduct.Arg0.Entry();
            name.setKey("name");
            name.setValue("Keyboard");

            CreateProduct.Arg0.Entry category = new CreateProduct.Arg0.Entry();
            category.setKey("category");
            category.setValue("Equipment");

            CreateProduct.Arg0.Entry quantity = new CreateProduct.Arg0.Entry();
            quantity.setKey("quantity");
            quantity.setValue("100");

            CreateProduct.Arg0.Entry cost = new CreateProduct.Arg0.Entry();
            cost.setKey("cost");
            cost.setValue("1300");

            arg.getEntry().add(code);
            arg.getEntry().add(name);
            arg.getEntry().add(category);
            arg.getEntry().add(quantity);
            arg.getEntry().add(cost);

            long productId = productService.getProductServicePort().createProduct(arg);
            System.out.println("Created product id = " + productId);
        }

        {
            UpdateProduct.Arg1 arg = new UpdateProduct.Arg1();

            UpdateProduct.Arg1.Entry cost = new UpdateProduct.Arg1.Entry();
            cost.setKey("cost");
            cost.setValue("4444");

            arg.getEntry().add(cost);

            boolean status = productService.getProductServicePort().updateProduct(3, arg);
            System.out.println("Update product status = " + status);

            GetProducts.Arg0 byIdArg = new GetProducts.Arg0();

            GetProducts.Arg0.Entry id = new GetProducts.Arg0.Entry();
            id.setKey("id");
            id.setValue("1");

            byIdArg.getEntry().add(id);

            List<Product> productsList = productService.getProductServicePort().getProducts(byIdArg);
            for (Product product : productsList) {
                System.out.println("Product {" +
                        "id=" + product.getId() +
                        ", code=" + product.getCode() +
                        ", name='" + product.getName() + '\'' +
                        ", category=" + product.getCategory() +
                        ", quantity=" + product.getQuantity() +
                        ", cost=" + product.getCost() +
                        '}');
            }
        }

//        {
//            boolean status = productService.getProductServicePort().removeProduct(1);
//            System.out.println("Remove product status = " + status);
//
//            System.out.println("All products:");
//            List<Product> productsList = productService.getProductServicePort().getProducts(new GetProducts().getArg0());
//            for (Product product : productsList) {
//                System.out.println("Product {" +
//                        "id=" + product.getId() +
//                        ", code=" + product.getCode() +
//                        ", name='" + product.getName() + '\'' +
//                        ", category=" + product.getCategory() +
//                        ", quantity=" + product.getQuantity() +
//                        ", cost=" + product.getCost() +
//                        '}');
//            }
//        }

        {
            CreateProduct.Arg0 arg = new CreateProduct.Arg0();

            CreateProduct.Arg0.Entry code = new CreateProduct.Arg0.Entry();
            code.setKey("code");
            code.setValue("101");

            arg.getEntry().add(code);

            try {
                productService.getProductServicePort().createProduct(arg);
            } catch (MissingFieldsException ex) {
                System.err.println(ex);
            }
        }

        {
            UpdateProduct.Arg1 arg = new UpdateProduct.Arg1();

            UpdateProduct.Arg1.Entry cost = new UpdateProduct.Arg1.Entry();
            cost.setKey("cost");
            cost.setValue("test");

            arg.getEntry().add(cost);

            try {
                productService.getProductServicePort().updateProduct(1, arg);
            } catch (ValueParsingException ex) {
                System.err.println(ex);
            }
        }

        {
            try {
                productService.getProductServicePort().removeProduct(9999);
            } catch (ProductRemoveException ex) {
                System.err.println(ex);
            }
        }
    }
}
