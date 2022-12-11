import java.sql.*;
import java.util.*;

public class App {

    String url = "jdbc:postgresql://localhost:5432/LookInnaBook";
    String user = "postgres";
    String password = "coco";

    public Connection connect() {

        Connection connection = null;
        ResultSet result; 

        User user = new User();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to our Book Store");

        while (true){
            System.out.println("Make a selection:");
            System.out.println("--------------------");
            System.out.println("1. Switch to User Mode");

            String input = scanner.next();

            if (input.contains("1")){
                System.out.println("Welcome to Look Inna Book");
                user.UserRun();
            }
        }
    }

        // try {
        //     connection = DriverManager.getConnection(url, user, password);
        //     System.out.println("Connected to the PostgreSQL server successfully.");

        //     Statement statement = connection.createStatement();
        //     result = statement.executeQuery("SELECT * FROM book");

        //     while (result.next()){
        //         System.out.println(result.getString("book_name"));
        //     }

        // } catch (SQLException e) {
        //     System.out.println(e.getMessage());
        // }

        

    public static void main(String[] args) {
        App app = new App();
        app.connect();
    }
}
