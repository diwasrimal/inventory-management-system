package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    private void initializeTables() throws SQLException {
        Statement stmt = this.conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "name VARCHAR(40) NOT NULL," +
            "description VARCHAR(150)," +
            "quantity INT DEFAULT 0," +
            "UNIQUE(name, description)" +
            ")"
        );
        stmt.close();
    }

    /**
     * Records a new product information in databse
     */
    void addNewProduct(Product prod) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO" +
            " products(name, quantity, description) " +
            " VALUES(?, ?, ?)"
        );
        stmt.setString(1, prod.name);
        stmt.setInt(2, prod.quantity);
        stmt.setString(3, prod.description);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Changes quantity of product in database
     */
    void editProductQuantity(int id, int quantity) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE products" +
            " SET quantity = ?" +
            " WHERE id = ?"
        );
        stmt.setInt(1, quantity);
        stmt.setInt(2, id);
    }

    List<Product> getProducts() throws SQLException {
        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM products");
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            Product prod = new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getString("description")
            );
            products.add(prod);
        }
        return products;
    }
}