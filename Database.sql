-- Database: TravelTourismDB

CREATE DATABASE TravelTourismDB;

USE TravelTourismDB;

-- Table for storing users (customers)
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    phone VARCHAR(20),
    address TEXT
);

-- Table for storing tour packages
CREATE TABLE Packages (
    package_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    price DECIMAL(10, 2),
    duration INT, -- in days
    available_seats INT
);

-- Table for storing bookings
CREATE TABLE Bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    package_id INT,
    booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (package_id) REFERENCES Packages(package_id)
);

-- Sample Data for Packages
INSERT INTO Packages (name, description, price, duration, available_seats)
VALUES
    ('Beach Getaway', 'A relaxing tour at the beach with all-inclusive amenities.', 799.99, 7, 50),
    ('Mountain Adventure', 'Explore the mountains with trekking and camping.', 1200.50, 10, 30),
    ('City Escape', 'A city tour visiting all major historical sites and attractions.', 499.00, 5, 100);

-- Sample Data for Users
INSERT INTO Users (first_name, last_name, email, password, phone, address)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'password123', '123-456-7890', '123 Main St, City, Country'),
    ('Jane', 'Smith', 'jane.smith@example.com', 'password456', '987-654-3210', '456 Another St, City, Country');
