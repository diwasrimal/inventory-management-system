package utils;

public class Product implements java.io.Serializable {
    public int id;
    public String name;
    public int quantity;
    public String description;

    public Product(String name, int quantity, String description) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }
}