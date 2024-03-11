package ifmo.webservices.lab7;

import ifmo.webservices.lab3.generated.*;

import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JUDDI juddi = new JUDDI("uddiadmin", "da_password1");
        boolean exit = false;
        while(!exit) {
            System.out.println("1. Register service");
            System.out.println("2. Find service");
            System.out.println("3. Exit");

            switch (Integer.parseInt(new Scanner(System.in).nextLine())) {
                case 3:
                    exit = true;
                    break;
                case 1: {
                    System.out.println("Enter service name for registration: ");
                    final String serviceName = new Scanner(System.in).nextLine();
                    juddi.registerService("ProductServiceBusinessName", serviceName, "http://localhost:8081/ProductService?wsdl");
                    System.out.println("Service registered!");
                    break;
                }
                case 2: {
                    System.out.println("Enter service name for search: ");
                    final String serviceName = new Scanner(System.in).nextLine();

                    try {
                        URL serviceURL = juddi.findServiceURL(serviceName);
                        ProductService_Service service = new ProductService_Service(serviceURL);
                        List<Product> products = service.getProductServicePort().getProducts(new GetProducts.Arg0());
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

                    } catch (NoSuchServiceException ex) {
                        System.out.println("Service not found!");
                    } catch (DatabaseException | UnknownProductParameterException | ValueParsingException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                default:
                    System.out.println("Wrong option");
                    break;
            }
        }
    }
}
