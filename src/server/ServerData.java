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
        addDummyProducts();
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
     * Edits exisiting product in database
     */
    void editProduct(int id, Product newProduct) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE products" +
            " SET name = ?, quantity = ?, description = ?" +
            " WHERE id = ?"
        );
        stmt.setString(1, newProduct.name);
        stmt.setInt(2, newProduct.quantity);
        stmt.setString(3, newProduct.description);
        stmt.setInt(4, id);
        stmt.executeUpdate();
        stmt.close();
    }

    void addDummyProducts() throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO" +
            " products(name, quantity, description) " +
            " VALUES(?, ?, ?)"
        );
        for (Product dummy : Product.dummies) {
            stmt.setString(1, dummy.name);
            stmt.setInt(2, dummy.quantity);
            stmt.setString(3, dummy.description);
            stmt.executeUpdate();
        }
        stmt.close();
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