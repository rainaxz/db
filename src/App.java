import java.sql.*;

public class App {

    String url = "jdbc:postgresql://localhost:3005/LookInnaBook";
    String user = "postgres";
    String password = "pass";

    public Connection connect() {

        Connection connection = null;
        ResultSet result; 

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");

            Statement statement = connection.createStatement();
            result = statement.executeQuery("SELECT * FROM book");

            while (result.next()){
                System.out.println(result.getString("book_name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    public static void main(String[] args) {
        App app = new App();
        app.connect();
    }
}
