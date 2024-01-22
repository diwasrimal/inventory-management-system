package messages;

import utils.Product;

public class ProductEditRequest implements java.io.Serializable {
    public int prodId;
    public Product newProd;

    public ProductEditRequest(int prodId, Product newProd) {
        this.prodId = prodId;
        this.newProd = newProd;
    }
}