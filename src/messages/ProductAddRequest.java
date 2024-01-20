package messages;

import utils.Product;

public class ProductAddRequest implements java.io.Serializable {
    public Product prod;

    public ProductAddRequest(Product prod) {
        this.prod = prod;
    }
}