package landbay;

/*
 * Product Class
 */
public class Product {
    private String product_id;
    private String rate;
    private String product_name;

    /*
     * Constructors
     */
    public Product() {}

    public Product(String product_id, String rate, String product_name) {
        this.product_id = product_id;
        this.rate = rate;
        this.product_name = product_name;
    }

    /*
     * Setters/Getters/toString
     */
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getRate() {
        return rate;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String toString() {
        return "Product{product_id=" + product_id
                + ", rate=" + rate
                + ", product_name=" +product_name
                + "}";
    }
}
