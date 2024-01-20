package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import utils.Product;

class ServerData {
    Connection conn;

    ServerData(String dburl, String user, String password) throws SQLException {
        this.conn = DriverManager.getConnection(dburl, user, password);
        initializeTables();
    }

    /**
     * Initializes all required tables for database
     */
    void initializeTables() throws SQLException {
        Statement stmt = this.conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(40) NOT NULL," +
            "description VARCHAR(150)," +
            "quantity INT DEFAULT 0" +
            ")"
        );
    }

    /**
     * Records a new product information in databse
     */
    void addNewProduct(Product prod) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO" +
            " products(name, quantity, description) " +
            " VALUES(?, ?, ?)"
        );
        stmt.setString(1, prod.name);
        stmt.setInt(2, prod.quantity);
        stmt.setString(3, prod.description);
        stmt.executeUpdate();
    }

    /**
     * Changes quantity of product in database
     */
    void editProductQuantity(int id, int quantity) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE products" +
            " SET quantity = ?" +
            " WHERE id = ?"
        );
        stmt.setInt(1, quantity);
        stmt.setInt(2, id);
    }
}