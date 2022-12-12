import java.sql.*;
import java.util.Scanner;

public class Owner {

    static String url = "jdbc:postgresql://localhost:3005/LookInnaBook";
    static String user = "postgres";
    static String pass = "pass";

    public void OwnerLogin() {
        // Login
        System.out.println("Please log in:\n");
        Scanner scanner = new Scanner(System.in);

        // username
        String input_username;
        System.out.println("Enter username: ");
        input_username = scanner.nextLine();

        // password
        String input_password;
        System.out.println("Enter Password: ");
        input_password = scanner.nextLine();

        Boolean success = true;
        ResultSet login;

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {

            // System.out.println("Connected to the PostgreSQL server successfully.");

            Statement statement = connection.createStatement();
            login = statement.executeQuery("SELECT * " + "FROM owner " + "WHERE username='" + input_username
                    + "' AND password='" + input_password + "'");

            if (login.next()) {

                System.out.print("\nLogin succesful! What would you like to do today?\n");
            } else {
                System.out.print("\nLogin failed. Closing bookstore...");
                System.exit(0);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }

        if (success) {
            ownerPrompts();
        }

    }

    static void ownerPrompts() {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, user, pass);
                Statement statement = connection.createStatement();) {

            System.out.println("Make a selection:");
            System.out.println("--------------------");
            System.out.println("1. Add a new book");
            System.out.println("2. Received requested quantity of books");
            System.out.println("3. Remove books from the bookstore");
            System.out.println("4. View reports");
            System.out.println("--------------------");
            System.out.println("If you want to return to the menu, enter any other number.");

            String input = scanner.nextLine();

            if (input.contains("1")) {
                String book_name, isbn = "", num_pages, sales_price, purchase_price, quantity, genre;
                String phone_num;
                String street_name, b_num, postal, city, province, country, bank_acc;
                String publisher_id = "", publisher_fname, publisher_lname;
                String[] publisher_ids;
                String[] phone_nums;
                String[] genres;
                String[] author_fname;
                String[] author_lname;
                String[] author_ids;

                // ISBN
                System.out.println("\n\n Enter book information");
                System.out.println("ISBN: ");
                Scanner scanner1 = new Scanner(System.in);
                isbn = scanner1.nextLine();
                ResultSet result1 = statement.executeQuery("SELECT * " +
                        "FROM book " +
                        "WHERE isbn = '" + isbn + "'");

                if (result1.next()) {
                    System.out.println("Book already exists. Restarting...");
                    ownerPrompts();
                } else {
                    // BOOK NAME
                    // doesn't already exist
                    System.out.println("\n\nBook name: ");
                    book_name = scanner1.nextLine();
                    ResultSet result2 = statement.executeQuery("SELECT * " +
                            "FROM book " +
                            "WHERE book_name = '" + book_name + "'");
                    if (result2.next()) {
                        System.out.println("Already exists. Restart");
                        ownerPrompts();
                    } else {
                        // SALES Price
                        System.out.println("\n\nSales price (float): ");
                        sales_price = scanner1.nextLine();
                        // purchase Price
                        System.out.println("\n\nPurchase price (float): ");
                        purchase_price = scanner1.nextLine();
                        // num pages
                        System.out.println("\n\nNumber of pages: ");
                        num_pages = scanner1.nextLine();
                        // quantity
                        System.out.println("\n\nQuantity: ");
                        quantity = scanner1.nextLine();

                        statement.executeUpdate("INSERT INTO book VALUES (" + isbn + ", '" + book_name + "', "
                                + sales_price + ", " + purchase_price + ", " + num_pages + ", " + quantity + ") ");

                        // creating genre entity
                        System.out.println("\n\nGenre(s) separated by spaces: ");
                        genre = scanner1.nextLine();
                        genres = genre.split(" ");
                        for (String i : genres) {
                            statement.executeUpdate("INSERT INTO genre VALUES (" + isbn + ", '" + i + "') ");
                        }
                        System.out.println("Genre(s) added to database.");

                        // creating written_by and author entity
                        System.out.println("\n\nAuthors(s) id separated by spaces: ");
                        String a_id = scanner1.nextLine();
                        // checking if author already exists
                        ResultSet authorCheck = statement.executeQuery("SELECT * " +
                                "FROM author " +
                                "WHERE author_id = '" + isbn + "'");

                        author_ids = a_id.split(" ");
                        System.out.println("\n\nAuthors(s) first name(s) separated by spaces: ");
                        String fname = scanner1.nextLine();
                        author_fname = fname.split(" ");
                        System.out.println("\n\nAuthors(s) last name(s) separated by spaces: ");
                        String lname = scanner1.nextLine();
                        author_lname = lname.split(" ");
                        System.out.println("*********************reached");
                        // if author does not exist
                        if (!authorCheck.next()) {
                            for (int i = 0; i < author_fname.length; i++) {
                                statement.executeUpdate(
                                        "INSERT INTO author VALUES ('" + author_ids[i] + "', '" + author_fname[i]
                                                + "', '" + author_lname[i] + "')");
                            }
                            System.out.println("Author(s) added to database.");

                        }
                        for (int i = 0; i < author_ids.length; i++) {
                            statement.executeUpdate(
                                    "INSERT INTO written_by VALUES ('" + author_ids[i] + "', '" + isbn + "')");
                        }
                        System.out.println("Written_by added to database.");

                        // create published and publisher entity
                        System.out.println("\n\nPublisher(s) id separate by spaces: ");
                        String p_id = scanner1.nextLine();
                        // checking if publisher already exists
                        ResultSet publisherCheck = statement.executeQuery("SELECT * " +
                                "FROM publisher " +
                                "WHERE publisher_id = '" + isbn + "'");

                        publisher_ids = p_id.split(" ");

                        // first name
                        System.out.println("\n\n Publisher's first name: ");
                        publisher_fname = scanner1.nextLine();
                        // last name
                        System.out.println("\n\n Publisher's last name: ");
                        publisher_lname = scanner1.nextLine();
                        // street
                        System.out.println("\n\n Publisher's street name: ");
                        street_name = scanner1.nextLine();
                        // building number
                        System.out.println("\n\n Publisher's building number: ");
                        b_num = scanner1.nextLine();
                        // postal
                        System.out.println("\n\n Publisher's postal code: ");
                        postal = scanner1.nextLine();
                        // city
                        System.out.println("\n\nPublisher's city: ");
                        city = scanner1.nextLine();
                        // province
                        System.out.println("\n\nPublisher's province: ");
                        province = scanner1.nextLine();
                        // country
                        System.out.println("\n\nPublisher's country: ");
                        country = scanner1.nextLine();
                        // bank account
                        System.out.println("\n\nPublisher's bank account: ");
                        bank_acc = scanner1.nextLine();

                        // create phone(s) entity
                        System.out.println("\n\nPhone number(s) separate by spaces: ");
                        phone_num = scanner1.nextLine();
                        phone_nums = phone_num.split(" ");

                        // if publisher does not exist
                        if (!publisherCheck.next()) {
                            statement.executeUpdate(
                                    "INSERT INTO publisher VALUES ('" + publisher_id + "', '" + publisher_fname
                                            + "', '" + publisher_lname + "', '" + street_name + "', '" + b_num
                                            + "', '" + postal + "', '" + city + "', '" + province + "', '" + country
                                            + "', '" + bank_acc + "')");
                            for (String n : phone_nums) {
                                statement.executeUpdate(
                                        "INSERT INTO phone_number VALUES (" + publisher_id + ", '" + n +
                                                "') ");
                            }
                            System.out.println("Phone number(s) added to database.");
                            System.out.println("Publisher(s) added to database.");

                        }

                        for (int i = 0; i < publisher_ids.length; i++) {
                            statement.executeUpdate(
                                    "INSERT INTO published VALUES ('" + publisher_ids[i] + "', '" + isbn + "')");
                        }
                        System.out.println("Published added to database.");

                        System.out.println("Book added to database.");
                        System.out.println("Newly Added book: " + isbn + ", '" + book_name + "', "
                                + sales_price + ", " + purchase_price + ", " + num_pages + ", " + quantity + ") ");

                    }
                }

            } else if (input.contains("2")) {

                Scanner scanner2 = new Scanner(System.in);

                System.out.println("\n\nPublisher restocking book.");

                System.out.println("Enter the book ISBN: ");
                String isbn = scanner2.nextLine();

                System.out.println("Amount of books added to stock:");
                String quantity = scanner2.nextLine();

                ResultSet bookInfo = statement.executeQuery("SELECT * " +
                        "FROM book " +
                        "WHERE isbn = '" + isbn + "'");

                System.out.println("Before changes: ");
                System.out.print("Book Name: " + bookInfo.getString("book_name") + " Quantity: "
                        + bookInfo.getString("quantity") + "\n");

                if (bookInfo.next()) {
                    statement.executeUpdate("UPDATE book " +
                            "SET quantity = quantity + " + quantity + " " +
                            "WHERE isbn = '" + isbn + "'");

                    System.out.println("Book quantity has been updated.");

                    ResultSet update = statement.executeQuery("SELECT  * " +
                            "FROM book " +
                            "WHERE isbn = '" + isbn + "'");
                    if (update.next()) {
                        System.out.print("Book Name: " + update.getString("book_name") + " Quantity: "
                                + update.getString("quantity") + "\n");
                    }
                }
                ownerPrompts();

            } else if (input.contains("3")) {
                System.out.println("\n\nRemoving book.");
                System.out.println("Enter ISBN: ");
                Scanner scanner3 = new Scanner(System.in);
                String isbn = scanner3.nextLine();

                ResultSet bookInfo = statement.executeQuery("SELECT * " +
                        "FROM book " +
                        "WHERE isbn = '" + isbn + "'");

                if (bookInfo.next()) {

                    statement.executeUpdate("DELETE FROM book " +
                            "WHERE isbn = '" + isbn + "'");

                    System.out.println("Book " + isbn + " is removed from database.");
                } else {
                    System.out.println("Can not remove. Book does not exist.");
                }
                ownerPrompts();

            } else if (input.contains("4")) {
                System.out.println("Make a report selection type:");
                System.out.println("1. View the sales vs expenditure");
                System.out.println("2. View the sales per genre");
                System.out.println("3. View the sales per author");

                input = scanner.nextLine();

                double sales = 0.0;
                double expenditures = 0.0;
                ResultSet temp;
                if (input.equals("1")) {

                    temp = statement.executeQuery(
                            "SELECT * FROM ((book INNER JOIN book_order ON book_order.book_id = book.isbn) INNER JOIN published ON published.book_id = book.isbn)");
                    while (temp.next()) {
                        sales += temp.getFloat("sales_price") * (100 - temp.getInt("percentage"));
                        expenditures += temp.getFloat("purchase_price");
                    }

                    System.out.println("sales $" + sales);
                    System.out.println("expenses $" + expenditures);

                    System.out.println("\n\nThe Report of the Sales vs Expenditure: ");
                    System.out.println("Total sales: $" + sales);
                    System.out.println("Total expenses: $" + expenditures);
                } else if (input.equals("2")) {
                    ResultSet genreResults = statement.executeQuery(
                            "SELECT * FROM (((book INNER JOIN book_order ON book_order.book_id = book.isbn) INNER JOIN published ON published.book_id = book.isbn) INNER JOIN genre ON genre.book_id = book.isbn)");
                    String g;
                    while (genreResults.next()) {
                        sales += genreResults.getFloat("sales_price") * (100 - genreResults.getInt("percentage"));
                        expenditures += genreResults.getFloat("purchase_price");
                        g = genreResults.getString("genre");
                        System.out.println("Genre: " + g + "\nSales $" + sales + "\nExpenses $" + expenditures);
                    }

                } else if (input.equals("3")) {
                    ResultSet authorResults = statement.executeQuery(
                            "SELECT * FROM (((book INNER JOIN book_order ON book_order.book_id = book.isbn) INNER JOIN published ON published.book_id = book.isbn) INNER JOIN written_by ON written_by.book_id = book.isbn)");
                    String a_f;
                    String a_l;
                    while (authorResults.next()) {
                        sales += authorResults.getFloat("sales_price") * (100 - authorResults.getInt("percentage"));
                        expenditures += authorResults.getFloat("purchase_price");
                        a_f = authorResults.getString("fname");
                        a_l = authorResults.getString("lname");
                        System.out.println(
                                "Author: " + a_f + " " + a_l + "\nSales $" + sales + "\nExpenses $" + expenditures);
                    }

                }
                ownerPrompts();
            }
        } catch (Exception sqle) {
            System.out.println("An exception: " + sqle);
        }
    }
}
