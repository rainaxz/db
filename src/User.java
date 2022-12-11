import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class User {
    public void UserRun(){
        System.out.println("Before you proceed, please log in: ");
        Scanner scanner = new Scanner(System.in);

        // username
        String username;
        System.out.println("Enter username: ");
        username = scanner.nextLine();
        // password
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        System.out.println("username: " + username);

        Boolean success = true;
        ResultSet login;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook",
                "postgres", "coco")) {
            Statement statement = connection.createStatement();
            login = statement.executeQuery("SELECT * " +
                    "FROM user_info " +
                    "WHERE username = '" + username + "' AND password = '" + password + "'");
            //System.out.println(login.getString("country"));
                
        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
            System.out.println("You can not login, incorrect username or password");
            success = false;
        }
        
        if (success){
            userPrompts();
        }
    }
    static void userPrompts(){
        boolean temp = true;
        ArrayList<String> cart = new ArrayList<>();
        while (temp) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Make a selection:");
            System.out.println("--------------------");
            System.out.println("1. Track an order");
            System.out.println("2. Search for a book");
            int input = scan.nextInt();

            if (input == 2) {
                System.out.println("What do you want to search by? \n1 Book name\n2 Author name\n3 ISBN\n4 Book Genre\n5 Book Sales price");
                int a = scan.nextInt();
                bookSearch(a, cart);

            }else if (input == 1) {
                System.out.println("Enter order number to be tracked");
                int order_num = scan.nextInt();

                try (Connection connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/LookInnaBook",
                        "postgres", "coco")) {
                    Statement statement = connection.createStatement();

                    ResultSet result = statement.executeQuery("SELECT * " +
                                                            "FROM order_receipt " +
                                                            "WHERE order_number = '"+order_num+"'");

                    if (result.next()){
                        System.out.println("Tracking Number: " + result.getString("tracking_num"));
                        System.out.println("Day: " + result.getString("o_day"));
                        System.out.println("Month: " + result.getString("o_month"));
                        System.out.println("Year: " + result.getString("o_year"));
                        System.out.println("On the way to: " + result.getString("s_city"));
                        System.out.println("On the way to: " + result.getString("s_country"));
                    }
                    else{
                        System.out.println("Sorry, the tracking number not found!");
                    }

                }catch(Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }

            }
        } 
    }

    static ArrayList<String> bookSearch ( int a, ArrayList<String > cart){
        Scanner scan = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LookInnaBook",
                "postgres", "coco")) {
            Statement statement = connection.createStatement();
            String isbn = " ";
            if (a == 1) {
                System.out.println("Search by book name. \nEnter book name.");
                String book_name = scan.nextLine();

                ResultSet book_nameOutput = statement.executeQuery("SELECT *" +
                        "FROM (( book " +
                        "INNER JOIN published ON published.book_id = book.isbn) " +
                        "INNER JOIN publisher ON publisher.publisher_id = published.p_id) " +
                        "WHERE book_name = '" + book_name + "' ");

                if (book_nameOutput.next()) {
                    System.out.println("Book Name: " + book_nameOutput.getString("book_name"));
                    System.out.println("ISBN: " + book_nameOutput.getString("isbn"));
                    isbn = book_nameOutput.getString("isbn");
                    System.out.println("Sale price: " + book_nameOutput.getString("sales_price"));
                    System.out.println("Number of pages: " + book_nameOutput.getString("num_pages"));
                    System.out.println("Quantity available: " + book_nameOutput.getString("quantity"));
                    System.out.println("Publisher first name: " + book_nameOutput.getString("fname"));
                    System.out.println("Publisher last name: " + book_nameOutput.getString("lname"));
                }
                ResultSet authorOutput = statement.executeQuery("SELECT fname, lname " +
                        "FROM author " +
                        "INNER JOIN written_by ON written_by.author_id = author.author_id " +
                        "WHERE book_id = '" + isbn + "' ");

                while (authorOutput.next()) {
                    System.out.println("Author first name: " + authorOutput.getString("fname"));
                    System.out.println("Author last name: " + authorOutput.getString("lname"));
                }
                ResultSet genreOutput = statement.executeQuery("SELECT genre " +
                        "FROM book " +
                        "INNER JOIN genre ON genre.book_id = book.isbn " +
                        "WHERE book_id = '" + isbn + "' ");

                while (genreOutput.next()) {
                    System.out.println("Genre(s): " + genreOutput.getString("genre"));
                } 

                // else {
                //     System.out.println("Sorry, this book does not exit!");
                //     return cart;
                // }

                Shipping.ShippingInfo(cart);
            }
        }
        catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        return cart;
    }
}
