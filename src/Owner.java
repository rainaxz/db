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

            System.out.println("Connected to the PostgreSQL server successfully.");

            Statement statement = connection.createStatement();
            login = statement.executeQuery("SELECT * " + "FROM owner " + "WHERE username='" + input_username
                    + "' AND password='" + input_password + "'");

            if (login.next()) {

                System.out.print("Login succesful! What would you like to do today?\n");
            } else {
                System.out.print("Login failed. Closing bookstore...");
                System.exit(0);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }

        if (success) {
            ownerPrompts();
        }
        scanner.close();

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

            int input = scanner.nextInt();

            if (input == 1) {
                String book_name, isbn = "", num_pages, sales_price, purchase_price, quantity, genre;
                String phone_num;
                String street_name, b_num, postal, city, province, country, bank_acc;
                String publisher_id = "", publisher_fname, publisher_lname;
                String[] publisher_ids;
                String[] book_ids;
                String[] phone_nums;
                String[] genres;
                String[] author_fname;
                String[] author_lname;
                String[] author_ids;

                // ISBN
                System.out.println("\n\n Enter book information");
                System.out.println("ISBN: ");
                isbn = scanner.nextLine();
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
                    book_name = scanner.nextLine();
                    ResultSet result2 = statement.executeQuery("SELECT * " +
                            "FROM book " +
                            "WHERE book_name = '" + book_name + "'");
                    if (result2.next()) {
                        System.out.println("Already exists. Restart");
                        ownerPrompts();
                    } else {
                        // SALES Price
                        System.out.println("\n\nSales price (float): ");
                        sales_price = scanner.nextLine();
                        // purchase Price
                        System.out.println("\n\nPurchase price (float): ");
                        purchase_price = scanner.nextLine();
                        // num pages
                        System.out.println("\n\nNumber of pages: ");
                        num_pages = scanner.nextLine();
                        // quantity
                        System.out.println("\n\nQuantity: ");
                        quantity = scanner.nextLine();

                        statement.executeUpdate("INSERT INTO book VALUES (" + isbn + ", '" + book_name + "', "
                                + sales_price + ", " + purchase_price + ", " + num_pages + ", " + quantity + ") ");

                        // creating genre entity
                        System.out.println("\n\nGenre(s) seperate by spaces: ");
                        genre = scanner.nextLine();
                        genres = genre.split(" ");
                        for (String i : genres) {
                            statement.executeUpdate("INSERT INTO genre VALUES (" + isbn + ", '" + i + "') ");
                        }
                        System.out.println("Genre(s) added to database.");

                        // creating written_by and author entity
                        System.out.println("\n\nAuthors(s) id seperated by spaces: ");
                        String a_id = scanner.nextLine();
                        // checking if author already exists
                        ResultSet authorCheck = statement.executeQuery("SELECT * " +
                                "FROM author " +
                                "WHERE author_id = '" + isbn + "'");

                        author_ids = a_id.split(" ");
                        System.out.println("\n\nAuthors(s) first name(s) seperated by spaces: ");
                        String fname = scanner.nextLine();
                        author_fname = fname.split(" ");
                        System.out.println("\n\nAuthors(s) last name(s) seperated by spaces: ");
                        String lname = scanner.nextLine();
                        author_lname = lname.split(" ");

                        // if author does not exist
                        if (!authorCheck.next()) {
                            for (int i = 0; i < author_fname.length; i++) {
                                statement.executeUpdate("INSERT INTO author VALUES ('" + isbn + "', '" + author_fname[i]
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
                        System.out.println("\n\nPublisher(s) id seperated by spaces: ");
                        String p_id = scanner.nextLine();
                        // checking if publisher already exists
                        ResultSet publisherCheck = statement.executeQuery("SELECT * " +
                                "FROM publisher " +
                                "WHERE publisher_id = '" + isbn + "'");

                        publisher_ids = p_id.split(" ");

                        System.out.println("\n\nPublisher(s)'s book's id seperated by spaces: ");
                        String b_id = scanner.nextLine();
                        book_ids = b_id.split(" ");

                        // first name
                        System.out.println("\n\n Publisher's first name: ");
                        publisher_fname = scanner.nextLine();
                        // last name
                        System.out.println("\n\n Publisher's last name: ");
                        publisher_lname = scanner.nextLine();
                        // street
                        System.out.println("\n\n Publisher's street name: ");
                        street_name = scanner.nextLine();
                        // building number
                        System.out.println("\n\n Publisher's building number: ");
                        b_num = scanner.nextLine();
                        // postal
                        System.out.println("\n\n Publisher's postal code: ");
                        postal = scanner.nextLine();
                        // city
                        System.out.println("\n\nPublisher's city: ");
                        city = scanner.nextLine();
                        // province
                        System.out.println("\n\nPublisher's province: ");
                        province = scanner.nextLine();
                        // country
                        System.out.println("\n\nPublisher's country: ");
                        country = scanner.nextLine();
                        // bank account
                        System.out.println("\n\nPublisher's bank account: ");
                        bank_acc = scanner.nextLine();

                        // if publisher does not exist
                        if (!publisherCheck.next()) {
                            statement.executeUpdate(
                                    "INSERT INTO publisher VALUES ('" + publisher_id + "', '" + publisher_fname
                                            + "', '" + publisher_lname + "', '" + street_name + "', '" + b_num + "', '"
                                            + "', '" + postal + "', '" + city + "', '" + province + "', '" + country
                                            + bank_acc + "')");
                            System.out.println("Publisher(s) added to database.");

                        }
                        for (int i = 0; i < publisher_ids.length; i++) {
                            statement.executeUpdate(
                                    "INSERT INTO published VALUES ('" + publisher_ids[i] + "', '" + book_ids[i] + "')");
                        }
                        System.out.println("Published added to database.");

                        // create phone(s) entity
                        System.out.println("\n\nPhone number(s) seperate by spaces: ");
                        phone_num = scanner.nextLine();
                        phone_nums = phone_num.split(" ");
                        for (String i : phone_nums) {
                            statement.executeUpdate("INSERT INTO phone number(s) VALUES (" + isbn + ", '" + i +
                                    "') ");
                        }
                        System.out.println("Phone number(s) added to database.");

                        System.out.println("Book added to database.");
                        System.out.println("Added book: " + isbn + ", '" + book_name + "', "
                                + sales_price + ", " + purchase_price + ", " + num_pages + ", " + quantity + ") ");

                    }
                }

            } else if (input == 2) {
                System.out.println("\n\nPublisher restocking book.");

                System.out.println("Enter the book ISBN:");
                String isbn = scanner.nextLine();
                System.out.println("Amount of books added to stock:");
                String quantity = scanner.nextLine();
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
            } else if (input == 3) {
                System.out.println("\n\nRemoving book.");
                System.out.println("Enter ISBN: ");
                String isbn = scanner.nextLine();

                ResultSet bookInfo = statement.executeQuery("SELECT * " +
                        "FROM book " +
                        "WHERE isbn = '" + isbn + "'");

                if (bookInfo.next()) {

                    statement.executeUpdate("DELETE FROM book " +
                            "WHERE isbn = '" + isbn + "'");

                    System.out.println("Book added to database.");
                } else {
                    System.out.println("Can not remove. Book does not exist.");
                }
            } else if (input == 4) {
                System.out.println("Make a report selection type:");
                System.out.println("1. View the sales vs expenditure");
                System.out.println("2. View the sales per genre");
                System.out.println("3. View the sales per author");

                input = scanner.nextInt();

                if (input == 1) {
                    ResultSet result = statement.executeQuery(
                            "select ISBN,SUM(sale_price) as sale_price, SUM(purchase_price) as purchase_price " +
                                    " from sales" +
                                    " group by ISBN");

                    double sales = 0.0;
                    double expense = 0.0;
                    while (result.next()) {
                        sales += result.getFloat("sale_price");
                        expense += result.getFloat("purchase_price");
                    }
                    expense = sales - expense;
                    System.out.println("The Report of the Sales vs Expenditure: ");
                    System.out.println("Total sales: $" + sales);
                    System.out.println("Total expenses: $" + expense);
                }
                // else if (input.equals("2")) {
                // ResultSet result = statement.executeQuery("select genre,SUM(sale_price) as
                // sale_price" +
                // " from sales" +
                // " group by genre");

                // System.out.println("The Report of the Sales per genre: ");
                // while (result.next()) {
                // System.out.printf(result.getString("genre") + "-> $" +
                // result.getFloat("sale_price") + "\n");
                // }
                // } else if (input.equals("3")) {
                // ResultSet result = statement
                // .executeQuery(" select author_id, SUM(sale_price) as sale_price, fname,
                // lname" +
                // " from author natural join sales" +
                // " group by author_id");

                // System.out.println("The Report of the Sales per author: ");
                // while (result.next()) {
                // System.out.printf(result.getString("fname") + " " + result.getString("lname")
                // + " -> $"
                // + result.getFloat("sale_price") + "\n");
                // }
                // }
                // }

            }
        } catch (Exception sqle) {
            System.out.println("An exception: " + sqle);
        }
        scanner.close();
    }
}
