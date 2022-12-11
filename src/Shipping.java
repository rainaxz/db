import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.*;

public class Shipping {
    static void ShippingInfo(ArrayList<String> cart) throws SQLException {
        User user = new User();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Would you like to add this book to your cart? ");
            System.out.println("1. Yes ");
            System.out.println("2. No");
            int ab = scan.nextInt();
            ResultSet result1;
            scan.nextLine();
            if (ab == 1) {
                System.out.println("Enter ISBN\n");
                String isbn = scan.nextLine();
                System.out.println("ISBN " + isbn + ".");
                try (Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/LookInnaBook",
                        "postgres", "coco")) {
                    Statement statement = connection.createStatement();
                    

                    result1 = statement.executeQuery("SELECT * " +
                            "FROM book " +
                            "WHERE isbn = '" + isbn + "'");
                    // if there are books left order
                    if (result1.getInt("quantity") > 0) {
                        cart.add(isbn);
                        System.out.println("Added to cart!");
                    }
                    // if there is 5 left then request more
                    else if (result1.getInt("quantity") == 5) {
                        String p_id;
                        ResultSet publisher = statement.executeQuery("SELECT * " +
                                "FROM ((book" +
                                "INNER JOIN published on published.book_id = book.isbn)" +
                                "INNER JOIN publisher on publisher.publisher_id = published.p_id)" +
                                "WHERE book.isbn = '" + isbn + "'");
                        System.out.println("Email being sent to " + publisher.getString("publisher.fname") + " "
                                + publisher.getString("publisher.lname"));
                        p_id = publisher.getString("publisher.publisher_id");
                        // check how many books to request
                        // look through book_order with isbn
                        ResultSet request = statement.executeQuery("SELECT *" +
                                "FROM book_order" +
                                "INNER JOIN order_receipt ON book_order.order_num = order_receipt.order_number" +
                                "WHERE book_id = '" + isbn + "'");
                        // variable month, but hard coded to last month from today
                        int numBookSoldPreviousMonth = 0;
                        if (request.next()) {
                            if (request.getString("order_receipt.o_month") == "November") {
                                numBookSoldPreviousMonth += 1;
                            }
                        }
                        System.out.println("Requested " + numBookSoldPreviousMonth + " more books");
                    }
                    // if there is no more
                    else if (result1.getInt("book.quantity") == 0) {
                        System.out.println("Not in stock. Requested more.");
                    }
                }
                catch (Exception sqle) {
                System.out.println("Exception: " + sqle);
                }
            }
            else{
                User.userPrompts();
            }
        }
    }
}
