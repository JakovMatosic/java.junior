# Java Junior Order Management 
A Spring Boot web application for managing orders, buyers, and delivery addresses (for a restaurant far far away).

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

## Installation and Running

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd java.junior
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

   Or run the main class:
   ```bash
   java -jar target/java.junior-0.0.1-SNAPSHOT.jar
   ```

4. **Access the application**:
   Open your browser and go to: `http://localhost:8080`

## Usage

### Main Pages

- **Home** (`/`): Landing page with navigation
- **Orders** (`/orders`): View all orders with sorting options
- **Add New Order** (`/orders/addnew`): Create a new order
- **Edit Order** (`/orders/edit/{id}`): Modify existing orders

### Key Functionality

1. **Creating Orders**:
   - Select buyer and delivery address from dropdowns
   - Fill in contact details, currency, status, and payment method
   - Add multiple order items with name, quantity, and price
   - Total price is automatically calculated

2. **Managing Order Items**:
   - Add items dynamically with "Add Another Item" button
   - Remove items with the "Remove" button
   - Validation ensures at least one valid item per order

3. **Order Status Updates**:
   - Change status directly from the orders list using the dropdown and "Update" button
   - Available statuses: Waiting for Confirmation, Preparing, Done

4. **Sorting Orders**:
   - Sort by customer name (alphabetical, last then first)
   - Sort by total price

### Database

The application uses H2 in-memory database that initializes with sample data on startup:
- 5 sample buyers
- 4 sample addresses
- 2 sample orders with items

Database console available at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)
