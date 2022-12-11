import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Owner {
    static void OwnerRun() {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore",
                "mayawong", "password");
                Statement statement = connection.createStatement();) {
            System.out.println("Make a selection:");
            System.out.println("--------------------");
            System.out.println("1. Edit bookstore");
            System.out.println("2. Remove books from the bookstore");
            System.out.println("3. View reports");
            System.out.println("--------------------");
            System.out.println("If you want to return to the menu, enter any other number.");

            String input = scanner.nextLine();

            if (input.equals("1")) {

                System.out.println("What changes would you like to make?");
                System.out.println("1. Add a new book");
                System.out.println("2. Alter the quantity of an existing book?");

                input = scanner.nextLine();

                if (input.equals("1")) {
                    System.out.println("Please enter a book name");
                    input = scanner.nextLine();

                    ResultSet result = statement.executeQuery("select *"
                            + " from book" +
                            " where book_name = '" + input + "'");

                    String[] newString = new String[10];
                    if (!result.next()) {
                        String bookName = input;
                        System.out.println("Sorry, this book does not exist in the database!");

                        System.out.println("Please enter the ISBN: ");
                        input = scanner.nextLine();

                        if (isValid(input, "ISBN") == -1) {
                            newString[0] = input;
                        } else {
                            System.out.println("This book is already exists.");
                            return;
                        }

                        System.out.println("Please enter author id: ");
                        input = scanner.nextLine();

                        newString[1] = input;

                        System.out.println("Please enter the publisher id: ");
                        input = scanner.nextLine();

                        newString[2] = input;

                        newString[3] = bookName;

                        System.out.println("Please enter a genre: ");
                        input = scanner.nextLine();

                        newString[4] = input;

                        System.out.println("Please enter a sale price: ");
                        input = scanner.nextLine();

                        newString[5] = input;

                        System.out.println("Please enter a purchase price: ");
                        input = scanner.nextLine();

                        newString[6] = input;

                        System.out.println("Please enter the book's number of pages: ");
                        input = scanner.nextLine();

                        newString[7] = input;

                        System.out.println("Please enter the quantity: ");
                        int NewInput = scanner.nextInt();

                        if (NewInput > 9) {
                            input = "" + NewInput;
                            newString[8] = input;
                        } else {
                            return;
                        }

                        statement.executeUpdate(
                                "insert into book values('" + newString[0] + "'" + "," + "'" + newString[1] + "'" + ","
                                        + "'" + newString[2] + "'" + "," + "'" + newString[3] + "'" + "," +
                                        "'" + newString[4] + "'" + "," + "'" + newString[5] + "'" + "," + "'"
                                        + newString[6] + "'" + "," + "'" + newString[7] + "'" + "," +
                                        "'" + newString[8] + "')" + "," + "'" + newString[9] + "')");

                        System.out.println("The new book has been inserted into the database!");
                    } else {
                        System.out.println("This book already exists in the database!");
                    }

                } else if (input.equals("2")) {
                    System.out.println("Please enter the book's name");
                    input = scanner.nextLine();
                    ResultSet result = statement.executeQuery("select *"
                            + " from book" +
                            " where book_name = '" + input + "'");

                    String bookName = "";
                    if (result.next()) {
                        System.out.print("Book Name: " + result.getString("book_name") + " Quantity: "
                                + result.getString("quantity") + "\n");
                        bookName = result.getString("book_name");

                        System.out.println("So, how many books do you want to add?");
                        input = scanner.nextLine();

                        statement.executeUpdate("UPDATE book" +
                                " SET quantity = quantity+" + input +
                                " WHERE book_name = '" + bookName + "'");

                        System.out.println("THE BOOK QUANTITY HAS BEEN ALTERED AND UPDATED!");

                        result = statement.executeQuery("select  * " +
                                " from book " +
                                " where book_name = '" + bookName + "'");
                        if (result.next()) {
                            System.out.print("Book Name: " + result.getString("book_name") + " Quantity: "
                                    + result.getString("quantity") + "\n");
                        }
                    }
                }
            } else if (input.equals("2")) {
                System.out.println("Please enter a book name");
                input = scanner.nextLine();

                ResultSet result = statement.executeQuery("select *"
                        + " from book" +
                        " where book_name = '" + input + "'");

                if (result.next()) {

                    statement.executeUpdate("Delete from book"
                            + " where book_name = '" + input + "'");

                    System.out.println("THE BOOK HAS BEEN DELETED!");
                } else {
                    System.out.println("This book does not exist in the database!");
                }
            } else if (input.equals("3")) {
                System.out.println("Make a report selection type:");
                System.out.println("1. View the sales vs expenditure");
                System.out.println("2. View the sales per genre");
                System.out.println("3. View the sales per author");

                input = scanner.nextLine();

                if (input.equals("1")) {
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
                } else if (input.equals("2")) {
                    ResultSet result = statement.executeQuery("select genre,SUM(sale_price) as sale_price" +
                            " from sales" +
                            " group by genre");

                    System.out.println("The Report of the Sales per genre: ");
                    while (result.next()) {
                        System.out.printf(result.getString("genre") + "-> $" + result.getFloat("sale_price") + "\n");
                    }
                } else if (input.equals("3")) {
                    ResultSet result = statement
                            .executeQuery(" select author_id, SUM(sale_price) as sale_price, fname, lname" +
                                    " from author natural join sales" +
                                    " group by author_id");

                    System.out.println("The Report of the Sales per author: ");
                    while (result.next()) {
                        System.out.printf(result.getString("fname") + " " + result.getString("lname") + " -> $"
                                + result.getFloat("sale_price") + "\n");
                    }
                }
            }

        } catch (Exception sqle) {
            System.out.println("An exception: " + sqle);
        }
        scanner.close();
    }

    public static int isValid(String item, String attribute) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BookStore",
                "Koushik", "coolkushy");
                Statement statement = connection.createStatement();) {
            ResultSet result = statement.executeQuery("select *"
                    + " from book"
                    + " where " + attribute + "= '" + item + "'");

            if (!result.next()) {
                return -1;
            }

        } catch (Exception sqle) {
            System.out.println("Exception: " + sqle);
        }
        return 1;
    }
}
