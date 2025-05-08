package trainreservation;

import java.util.Scanner;

import service.LoginService;
import service.AdminService;
import entity.Passenger;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        LoginService authService = new LoginService(); // Authentication service
        AdminService adminService = new AdminService(); // Admin service (to be created)
        
        System.out.println("🚉 Welcome to Train Reservation System 🚉");

        // Main loop to show login dashboard
        while (true) {
            System.out.println("\n===== Login Dashboard =====");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Register as New User");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();  // Take user input for login choice
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Admin login process
                    System.out.print("Enter Admin Username: ");
                    String adminUsername = scanner.nextLine(); // Admin username input
                    //System.out.print("Enter Admin Password: ");
                    //String adminPassword = scanner.nextLine(); // Admin password input
                    
                 // Use Swing dialog for password input
                    JPasswordField pf = new JPasswordField();
                    JPanel panel = new JPanel();
                    panel.add(new JLabel("Enter Admin Password:"));
                    panel.add(pf);

                    // Show password popup
                    int option = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Admin Login",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                    );

                    String adminPassword = null;

                    // If user clicks OK, fetch the password
                    if (option == JOptionPane.OK_OPTION) {
                        adminPassword = new String(pf.getPassword());  // Get entered password

                        // Validate admin credentials with the hardcoded password "admin123"
                        if ("admin123".equals(adminPassword)) {
                            System.out.println("✅ Admin Login Successful");
                            adminService.showAdminPanel(scanner); // Show admin dashboard
                        } else {
                            System.out.println("❌ Invalid admin credentials.");
                        }
                    } else {
                        System.out.println("❌ Login cancelled.");
                    }
                    break;


                    /* Validate admin credentials
                    if (adminService.login(adminUsername, adminPassword)) {
                        System.out.println("✅ Admin Login Successful");
                        adminService.showAdminPanel(scanner); // Show admin dashboard
                    } else {
                        System.out.println("❌ Invalid admin credentials.");
                    }
                    break;*/

                case 2:
                    // User login process
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine(); // User email input
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine(); // User password input

                    /*String password;
                    if (console != null) {
                        char[] PassChars = console.readPassword("Enter your password: ");
                        password = new String(PassChars);
                    } else {
                        System.out.print("Enter your password (Warning: Not hidden in IDE): ");
                        password = scanner.nextLine();
                    }*/
                    
                    // Validate user credentials
                    Passenger loggedInPassenger = authService.login(email, password);
                    if (loggedInPassenger != null) {
                        System.out.println("✅ Welcome, " + loggedInPassenger.getName());
                        authService.showUserPanel(scanner, loggedInPassenger); // Show user dashboard
                    } else {
                        System.out.println("❌ Invalid email or password.");
                    }
                    break;

                case 3:
                    // New user registration process
                    System.out.println("\n===== 📝 New User Registration =====");

                    System.out.print("Enter your full name: ");
                    String name = scanner.nextLine(); // User name input

                    System.out.print("Enter your email: ");
                    String newEmail = scanner.nextLine(); // New user email input

                    System.out.print("Enter your password: ");
                    String newPassword = scanner.nextLine(); // New user password input

                    System.out.print("Enter your gender: ");
                    String gender = scanner.nextLine(); // User gender input

                    System.out.print("Enter your age: ");
                    int age = scanner.nextInt(); // User age input
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter your phone number: ");
                    String phone = scanner.nextLine(); // User phone number input

                    // Create new Passenger object
                    Passenger newPassenger = new Passenger(name, newEmail, age, gender, phone, newPassword);

                    // Register new user
                    boolean registered = authService.registerPassenger(newPassenger);

                    if (registered) {
                        System.out.println("✅ Registration successful! You can now log in.");
                    } else {
                        System.out.println("❌ Registration failed. Email might already be in use.");
                    }
                    break;

                case 4:
                    // Exit the application
                    System.out.println("Thank you! Exiting...");
                    return;

                default:
                    // Handle invalid input
                    System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }
}
