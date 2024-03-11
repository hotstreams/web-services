package ifmo.webservices.lab1;

import ifmo.webservices.lab1.generated.Product;
import ifmo.webservices.lab1.generated.ProductService_Service;
import ifmo.webservices.lab1.generated.GetProducts;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
//        URL url = new URL("http://localhost:8080/ProductService?wsdl");
        URL url = new URL("http://localhost:8080/webapp/ws/service?wsdl");
        ProductService_Service productService = new ProductService_Service(url);

        System.out.println("All products: ");
        List<Product> products = productService.getProductServicePort().getProducts(new GetProducts().getArg0());
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
    }
}
