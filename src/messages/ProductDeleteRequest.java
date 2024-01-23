package messages;

public class ProductDeleteRequest implements java.io.Serializable {
    public int prodId;

    public ProductDeleteRequest(int prodId) {
        this.prodId = prodId;
    }
}