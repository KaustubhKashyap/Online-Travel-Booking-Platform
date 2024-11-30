import java.sql.*;
import java.util.Scanner;

public class TravelTourismSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/TravelTourismDB";
    private static final String USER = "root"; // Change as per your MySQL user
    private static final String PASSWORD = "password"; // Change as per your MySQL password
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Welcome to the Travel Tourism Management System!");
            
            // Displaying available packages
            System.out.println("\nAvailable Tour Packages:");
            displayPackages(connection);
            
            // User login or registration
            System.out.print("\nEnter your email to login or type 'register' to create a new account: ");
            String email = scanner.nextLine();
            
            int userId = getUserId(connection, email);
            if (userId == -1) {
                userId = registerUser(connection, scanner);
            }
            
            // Making a booking
            System.out.print("\nEnter the package ID you want to book: ");
            int packageId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline
            
            makeBooking(connection, userId, packageId);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Displaying all available packages
    private static void displayPackages(Connection connection) {
        String query = "SELECT * FROM Packages";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                System.out.println("Package ID: " + resultSet.getInt("package_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println("Price: $" + resultSet.getDouble("price"));
                System.out.println("Duration: " + resultSet.getInt("duration") + " days");
                System.out.println("Available Seats: " + resultSet.getInt("available_seats"));
                System.out.println("------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get user ID by email (if user exists)
    private static int getUserId(Connection connection, String email) throws SQLException {
        String query = "SELECT user_id FROM Users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        }
        return -1; // User not found
    }

    // Register a new user
    private static int registerUser(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter your phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your address: ");
        String address = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        
        String query = "INSERT INTO Users (first_name, last_name, email, password, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, password);
            statement.setString(5, phone);
            statement.setString(6, address);
            statement.executeUpdate();
            
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    // Making a booking
    private static void makeBooking(Connection connection, int userId, int packageId) throws SQLException {
        // Calculate the total amount (for simplicity, we assume no discount or taxes)
        String query = "SELECT price FROM Packages WHERE package_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, packageId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                double price = resultSet.getDouble("price");
                String bookingQuery = "INSERT INTO Bookings (user_id, package_id, total_amount) VALUES (?, ?, ?)";
                try (PreparedStatement bookingStatement = connection.prepareStatement(bookingQuery)) {
                    bookingStatement.setInt(1, userId);
                    bookingStatement.setInt(2, packageId);
                    bookingStatement.setDouble(3, price);
                    bookingStatement.executeUpdate();
                    System.out.println("Booking successful! Total amount: $" + price);
                }
            }
        }
    }
}
