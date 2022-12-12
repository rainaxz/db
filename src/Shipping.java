import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Shipping {

    static String url = "jdbc:postgresql://localhost:3005/LookInnaBook";
    static String usern = "postgres";
    static String pass = "pass";

    static void ShippingInfo(ArrayList<String> cart) {

        Scanner scanner = new Scanner(System.in);
        User user = new User();
        ResultSet bookInfo;
        String isbn;
        Integer quantity = 0;

        while (true) {
            System.out.println("\nAdd a book to cart?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            int input = scanner.nextInt();
            scanner.nextLine();

            if (input == 1) {
                System.out.println("\nEnter book name:");
                String name = scanner.nextLine();

                try (Connection connection = DriverManager.getConnection(url, usern, pass)) {

                    Statement statement = connection.createStatement();
                    bookInfo = statement.executeQuery("SELECT * " +
                            "FROM book " +
                            "WHERE book_name = '" + name + "'");

                    if (bookInfo.next()) {
                        isbn = bookInfo.getString("isbn");
                        quantity = bookInfo.getInt("quantity");
                        System.out.println("In stock: " + quantity);

                        // if there are books left order
                        if (quantity > 0) {
                            cart.add(isbn);
                            System.out.println("Added to cart!");
                            // update quantity of that book
                            statement.executeUpdate("UPDATE book " +
                                    "SET quantity = quantity - 1 " +
                                    "WHERE isbn = '" + isbn + "'");
                            quantity = quantity - 1;
                        }

                        // if there is 5 left then request more
                        if (quantity == 5) {
                            System.out.println("Restock needed!");
                            ResultSet publisher = statement.executeQuery("SELECT fname, lname  " +
                                    "FROM publisher " +
                                    "INNER JOIN published ON published.p_id = publisher.publisher_id " +
                                    "WHERE published.book_id = '" + isbn + "'");
                            if (publisher.next()) {
                                System.out.println("Email being sent to " + publisher.getString("fname") + " "
                                        + publisher.getString("lname"));
                            }

                            // check how many books to request
                            // look through book_order with isbn
                            ResultSet request = statement.executeQuery("SELECT * " +
                                    "FROM book_order " +
                                    "INNER JOIN order_receipt ON book_order.order_num = order_receipt.order_number " +
                                    "WHERE book_id = '" + isbn + "'");
                            // variable month, but hard coded to last month from today
                            int numBookSoldPreviousMonth = 0;
                            while (request.next()) {
                                String month = request.getString("o_month");
                                Integer year = request.getInt("o_year");
                                if (month.equals("November") && year == 2022) {
                                    numBookSoldPreviousMonth += 1;
                                }
                            }

                            System.out.println("Requested " + numBookSoldPreviousMonth + " more books\n");

                        } else if (quantity == 0) {
                            System.out.println("Not in stock. Request already sent.");
                        }
                    }
                } catch (Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }

                if (!cart.isEmpty()) {
                    System.out.println("\nWould you like to checkout? \n1 Yes\n2 No");
                    int answer = scanner.nextInt();
                    if (answer == 1) {
                        checkout(cart);
                    }
                }

            } else {
                user.userPrompts();
            }
        }
    }

    static void checkout(ArrayList<String> cart) {

        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, usern, pass)) {

            Statement statement = connection.createStatement();
            scanner.nextLine();
            ResultSet orders;

            String order_num, tracking_num, o_d, o_m, o_y, b_street, b_num, b_pc, b_city, b_p, b_c, s_street,
                    s_num, s_pc, s_city, s_p, s_c;

            System.out.println("Enter 1 to use a different shipping and billing information");
            String input = scanner.nextLine();
            System.out.println("Enter billing information");
            System.out.println("\nStreet name: ");
            b_street = scanner.nextLine();

            System.out.println("\nBuilding number: ");
            b_num = scanner.nextLine();

            System.out.println("\nPostal code: ");
            b_pc = scanner.nextLine();

            System.out.println("\nCity: ");
            b_city = scanner.nextLine();

            System.out.println("\nProvince: ");
            b_p = scanner.nextLine();

            System.out.println("\nCountry: ");
            b_c = scanner.nextLine();

            System.out.println("Enter shipping information");
            System.out.println("\nStreet name: ");
            s_street = scanner.nextLine();

            System.out.println("\nBuilding number: ");
            s_num = scanner.nextLine();

            System.out.println("\nPostal code: ");
            s_pc = scanner.nextLine();

            System.out.println("\nCity: ");
            s_city = scanner.nextLine();

            System.out.println("\nProvince: ");
            s_p = scanner.nextLine();

            System.out.println("\nCountry: ");
            s_c = scanner.nextLine();

            ResultSet orders2;

            while (true) {
                int temp = 8;
                orders2 = statement.executeQuery("SELECT * " +
                        "FROM order_receipt " +
                        "WHERE order_number = '" + temp + "'");

                if (!orders2.next()) {
                    statement.executeUpdate(
                            "INSERT INTO orders_receipt VALUES ('" + temp + "', '" + temp + "', '" + temp
                                    + "', 'November', 2022, '" + b_street + "', '" + b_num + "', '" + b_pc + "', '"
                                    + b_city + "', '" + b_p + "', '" + b_c + "', '" + s_street
                                    + "', '" + s_num + "', '" + s_pc + "', '" + s_city + "', '" + s_p
                                    + "', '" + s_c + "')");
                    for (String i : cart) {
                        statement.executeUpdate("INSERT INTO book_order values('" + i + "', '" + temp + "')");
                    }
                    System.out.println("Order #" + temp + " has been placed.");
                    break;
                } else {
                    temp += 1;
                }
            }

        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
    }

}
