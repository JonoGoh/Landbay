package landbay;

public class Product {
    private String product_id;
    private double rate;
    private String product_name;

    public Product(String product_id, double rate, String product_name) {
        this.product_id = product_id;
        this.rate = rate;
        this.product_name = product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public double getRate() {
        return rate;
    }

    public String getName() {
        return product_name;
    }

    public void printInfo() {
        System.out.println("Product ID:\t\t" + product_id);
        System.out.println("Product rate:\t" + rate);
        System.out.println("Product name:\t" + product_name);
    }
}
