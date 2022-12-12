
import java.util.*;

public class Store {

    public void openBookstore() {

        User user = new User();
        Owner owner = new Owner();
        

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Look Inna Book");

        while (true) {
            System.out.println("Make a selection:");
            System.out.println("--------------------");
            System.out.println("1. Access as User");
            System.out.println("2. Access as Owner");
            System.out.println("3. Quit");

            String input = scanner.next();

            if (input.equals("1")) {
                System.out.println("\nUser Account\n--------------------\n");
                user.UserLogin();
            } else if (input.equals("2")) {
                System.out.print("Owner Account\n--------------------\n");
                owner.OwnerLogin();
            } else {
                break;
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Store store = new Store();
        store.openBookstore();
    }
}
