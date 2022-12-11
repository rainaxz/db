import java.sql.*;
import java.util.*;

public class User {

    static String url = "jdbc:postgresql://localhost:3005/LookInnaBook";
    static String user = "postgres";
    static String pass = "pass";

    public void UserRun(){

        //Login
        System.out.println("Please log in:\n");
        Scanner scanner = new Scanner(System.in);

        //username
        String input_username;
        System.out.println("Enter username: ");
        input_username = scanner.nextLine();
        
        //password
        String input_password;
        System.out.println("Enter Password: ");
        input_password = scanner.nextLine();

        // System.out.println("username: " + input_username);
        // System.out.println("password: " + input_password);

        Boolean success = true;
        ResultSet login; 

        try (Connection connection = DriverManager.getConnection(url, user, pass)){

            System.out.println("Connected to the PostgreSQL server successfully.");

            Statement statement = connection.createStatement();
            login = statement.executeQuery("SELECT * " + "FROM user_info " + "WHERE username='" + input_username + "' AND password='" + input_password + "'");

            while(login.next()){
                System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(login.getString("username"));
                System.out.println(login.getString("password"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }

        scanner.close();

        if (success){
            userPrompts();
        }   
    }

    static void userPrompts(){

    
        ArrayList<String> cart = new ArrayList<>();

        while (true) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Make a selection:");
            System.out.println("--------------------");
            System.out.println("1. Track an order");
            System.out.println("2. Search for a book");
            
            int input = scanner.nextInt();

            if (input == 1) {
                System.out.println("Enter order number to be tracked");
                int order_num = scanner.nextInt();

                try (Connection connection = DriverManager.getConnection(url, user, pass)) {
                    
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

                    } else {
                        System.out.println("Sorry, tracking number not found!");
                    }

                } catch(Exception sqle) {
                    System.out.println("Exception: " + sqle);
                }

            } else if (input == 2) {
                System.out.println("Search by: \n1 Book name\n2 Author name\n3 ISBN\n4 Book Genre\n5 Book Sales price");
                int a = scanner.nextInt();
                bookSearch(a, cart);

            } else {
                break;
            }

            scanner.close();
        } 
    }

    static ArrayList<String> bookSearch (int a, ArrayList<String > cart){

        Scanner scanner = new Scanner(System.in);
        
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {

            Statement statement = connection.createStatement();
            String isbn = " ";
            if (a == 1) {
                System.out.println("Search by book name. \nEnter book name.");
                String book_name = scan.nextLine();

                ResultSet book_nameOutput = statement.executeQuery("SELECT * " +
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

                    authorSearch(isbn);
                    genreSearch(isbn);
                }

                // else {
                // System.out.println("Sorry, this book does not exit!");
                // return cart;
                // }

                // Shipping.ShippingInfo(cart);
            } else if (a == 2) {
                System.out.println("Search Book by Author's name. \nEnter Author's first name. ");
                String authorFName = scan.nextLine();
                System.out.println("Enter Author's last name. ");
                String authorLName = scan.nextLine();
                isbn = " ";

                ResultSet authorOutput = statement.executeQuery("SELECT *" +
                        "FROM ((author " +
                        "INNER JOIN written_by on written_by.author_id = author.author_id) " +
                        "INNER JOIN book ON written_by.book_id = book.isbn) " +
                        "WHERE author.fname = '" + authorFName + "' AND author.lname = '" + authorLName + "'");

                if (authorOutput.next()) {
                    System.out.println("Book Name: " + authorOutput.getString("book_name"));
                    System.out.println("Author first name: " + authorOutput.getString("fname"));
                    System.out.println("Author last name: " + authorOutput.getString("lname"));
                    System.out.println("ISBN: " + authorOutput.getString("isbn"));
                    isbn = authorOutput.getString("isbn");
                    System.out.println("Sale price: " + authorOutput.getString("sales_price"));
                    System.out.println("Number of pages: " + authorOutput.getString("num_pages"));
                    System.out.println("Quantity available: " + authorOutput.getString("quantity"));

                    ResultSet publisherOutput = statement.executeQuery("SELECT fname, lname " +
                            "FROM ((book " +
                            "INNER JOIN published on published.book_id = book.isbn) " +
                            "INNER JOIN publisher ON publisher.publisher_id = published.p_id) " +
                            "WHERE book.isbn = '" + isbn + "'");

                    if (publisherOutput.next()) {
                        System.out.println("Publisher first name: " + publisherOutput.getString("fname"));
                        System.out.println("Publisher last name: " + publisherOutput.getString("lname"));
                    }
                    genreSearch(isbn);
                }

                // else {
                // System.out.println("Sorry, this author does not exit!");
                // return cart;
                // }

                // Shipping.ShippingInfo(cart, authorOutput, statement);

            } else if (a == 3) {
                System.out.println("Search by ISBN number. \nEnter ISBN number.");
                String isbnSearch = scan.nextLine();

                ResultSet isbnOutput = statement.executeQuery(
                        "SELECT book.isbn, book.book_name, book.sales_price, book.num_pages, book.quantity, publisher.fname, publisher.lname "
                                +
                                "FROM ((book " +
                                "INNER JOIN published on published.book_id = book.isbn) " +
                                "INNER JOIN publisher on publisher.publisher_id = published.p_id) " +
                                "WHERE book.isbn = '" + isbnSearch + "'");

                if (isbnOutput.next()) {
                    System.out.println("Book Name: " + isbnOutput.getString("book_name"));
                    System.out.println("ISBN: " + isbnOutput.getString("isbn"));
                    System.out.println("Sale price: " + isbnOutput.getString("sales_price"));
                    System.out.println("Number of pages: " + isbnOutput.getString("num_pages"));
                    System.out.println("Quantity available: " + isbnOutput.getString("quantity"));
                    System.out.println("Publisher first name: " + isbnOutput.getString("fname"));
                    System.out.println("Publisher last name: " + isbnOutput.getString("lname"));

                    authorSearch(isbnSearch);
                    genreSearch(isbnSearch);
                }

                // else {
                // System.out.println("Sorry this ISBN does not exit!");
                // return cart;
                // }

                // Shipping.ShippingInfo(cart, isbnOutput, statement);

            } else if (a == 4) {

                System.out.println("Search by genre. \nEnter genre.");
                String genreSearch = scan.nextLine();
                ResultSet genreOutput = statement.executeQuery("SELECT * " +
                        "FROM (((genre " +
                        "INNER JOIN book on book.isbn = genre.book_id) " +
                        "INNER JOIN published on published.book_id = book.isbn) " +
                        "INNER JOIN publisher on publisher.publisher_id = published.p_id) " +
                        "WHERE genre.genre = '" + genreSearch + "'");

                while (genreOutput.next()) {
                    ResultSet authorOutput2;
                    isbn = " ";
                    System.out.println("Book Name: " + genreOutput.getString("book_name"));
                    System.out.println("ISBN: " + genreOutput.getString("isbn"));
                    isbn = genreOutput.getString("isbn");
                    System.out.println("Sale price: " + genreOutput.getString("sales_price"));
                    System.out.println("Number of pages: " + genreOutput.getString("num_pages"));
                    System.out.println("Quantity available: " + genreOutput.getString("quantity"));
                    System.out.println("Publisher first name: " + genreOutput.getString("fname"));
                    System.out.println("Publisher last name: " + genreOutput.getString("lname"));

                    authorSearch(isbn);

                    genreSearch(isbn);
                }
                // Shipping.ShippingInfo(cart, genreOutput, statement);

            } else if (a == 5) {
                isbn = " ";
                System.out.println("Search by price. \nEnter price.");
                String price = scan.nextLine();
                ResultSet priceOutput = statement.executeQuery("SELECT * " +
                        "FROM ((book " +
                        "INNER JOIN published on published.book_id = book.isbn) " +
                        "INNER JOIN publisher on publisher.publisher_id = published.p_id) " +
                        "WHERE book.sales_price = '" + price + "'");

                while (priceOutput.next()) {
                    System.out.println("Book Name: " + priceOutput.getString("book_name"));
                    System.out.println("ISBN: " + priceOutput.getString("isbn"));
                    isbn = priceOutput.getString("isbn");
                    System.out.println("Sale price: " + priceOutput.getString("sales_price"));
                    System.out.println("Number of pages: " + priceOutput.getString("num_pages"));
                    System.out.println("Quantity available: " + priceOutput.getString("quantity"));
                    System.out.println("Publisher first name: " + priceOutput.getString("fname"));
                    System.out.println("Publisher last name: " + priceOutput.getString("lname"));
                    authorSearch(isbn);
                    genreSearch(isbn);
                }
                // Shipping.ShippingInfo(cart, priceOutput, statement);
            }
                
                

                // else {
                //     System.out.println("Sorry, this book does not exit!");
                //     return cart;
                // }

                Shipping.ShippingInfo(cart);


            } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }

        scanner.close();
        return cart;
    }
}
