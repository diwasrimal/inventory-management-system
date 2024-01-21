package messages;

import java.util.List;

import utils.Product;

public class ProductListResponse implements java.io.Serializable {
    public List<Product> products;

    public ProductListResponse(List<Product> products) {
        this.products = products;
    }
}