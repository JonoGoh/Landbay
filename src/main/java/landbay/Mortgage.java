package landbay;

public class Mortgage extends Product {
    private int mortgage_id;
    private int loan_amount;
    private String postcode;

    public Mortgage(Product product, int mortgage_id, int loan_amount, String postcode) {
        super(product.getName(),product.getRate(),product.getName());
        this.mortgage_id = mortgage_id;
        this.loan_amount = loan_amount;
        this.postcode = postcode;
    }

    public int getMortgage_id() {
        return mortgage_id;
    }

    public int getLoan_amount() {
        return loan_amount;
    }

    public String getPostcode() {
        return postcode;
    }

    public void printInfo() {
        System.out.println("Mortgage ID:\t" + mortgage_id);
        System.out.println("Loan Amount:\t" + loan_amount);
        System.out.println("Product ID:\t\t" + getProduct_id());
        System.out.println("Postcode:\t\t" + postcode);
    }
}
