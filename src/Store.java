
import java.util.*;
public class Store{


    public void openBookstore() {


        User user = new User();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to Look Inna Book");

            while (true) {
                System.out.println("Make a selection:");
                System.out.println("--------------------");
                System.out.println("1. Access as User");
                System.out.println("2. Access as Owner");
                System.out.println("3. Quit");

                String input = scanner.next();

                if (input.contains("1")) {
                    System.out.println("\nUser Account\n--------------------\n");
                    user.UserRun();
                } 

                break;
            }

            scanner.close();

        //Connection connection = null;
        //ResultSet result; 

        // try {

        //     //conect to the database
        //     // connection = DriverManager.getConnection(url, user, password);
        //     // System.out.println("Connected to the PostgreSQL server successfully.");


            

        //     // Statement statement = connection.createStatement();
        //     // result = statement.executeQuery("SELECT * FROM book");

        //     // while (result.next()){
        //     //     System.out.println(result.getString("book_name"));
        //     // }

        // } catch (SQLException e) {
        //     System.out.println(e.getMessage());
        // }

        // return connection;
    }

    public static void main(String[] args) {
        Store store = new Store();
        store.openBookstore();
    }
}
