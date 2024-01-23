package utils;

public class Product implements java.io.Serializable {
    public int id;
    public String name;
    public int quantity;
    public String description;

    public static Product[] dummies = {
        new Product("Laptop", 30, "High-performance laptop with SSD storage"),
        new Product("Smartphone", 50, "Latest model with advanced camera features"),
        new Product("Headphones", 100, "Noise-canceling wireless headphones"),
        new Product("Tablet", 20, "10-inch tablet with retina display"),
        new Product("Smartwatch", 15, "Water-resistant smartwatch with fitness tracking"),
        new Product("Digital Camera", 25, "Mirrorless camera with 4K video recording"),
        new Product("Bluetooth Speaker", 40, "Portable speaker with long battery life"),
    };

    public Product(int id, String name, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }

    public Product(String name, int quantity, String description) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }
}