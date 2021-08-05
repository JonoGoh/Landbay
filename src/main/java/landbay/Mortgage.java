package landbay;

/*
 * Mortgage Class. Inherits Product class as Mortgages are a type of Product.
 * Implements the Comparable Class so that it can be sorted by "profit" amount (Might be useful in future)
 */
public class Mortgage extends Product implements Comparable<Mortgage> {
    private String mortgage_id;
    private String loan_amount;
    private String postcode;

    /*
     * Constructors
     */
    public Mortgage() {}

    public Mortgage(Product product) {
        super(product.getProduct_id(), product.getRate(), product.getProduct_name());
    }

    public Mortgage(Product product, String mortgage_id, String loan_amount, String postcode) {
        super(product.getProduct_id(),product.getRate(),product.getProduct_name());
        this.mortgage_id = mortgage_id;
        this.loan_amount = loan_amount;
        this.postcode = postcode;
    }

    /*
     * Calculate "profit" for 1 year based on the rate of the Mortgage * loan amount.
     * Could be useful for future implementations of Mortgage allocation algorithms.
     *
     * @return      the "profit" amount
     */
    public double calculateOneYearProfit() {
        double l = Double.parseDouble(getLoan_amount());
        double r = Double.parseDouble(getRate().substring(0,getRate().length()-1));
        return l * (r/100);
    }

    public int compareTo(Mortgage mortgage) {
        return (int) Math.round(mortgage.calculateOneYearProfit() - calculateOneYearProfit());
    }

    /*
     * Setters/Getters/toString
     */
    public void setMortgage_id(String mortgage_id) {
        this.mortgage_id = mortgage_id;
    }

    public void setLoan_amount(String loan_amount) {
        this.loan_amount = loan_amount;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMortgage_id() {
        return mortgage_id;
    }

    public String getLoan_amount() {
        return loan_amount;
    }

    public String getPostcode() {
        return postcode;
    }

    public String toString() {
        return "Mortgage{mortgage_id=" + mortgage_id
                + ", rate=" + loan_amount
                + ", postcode=" + postcode
                + ", product_id=" + getProduct_id()
                + ", rate" + getRate()
                + ", product_name=" + getProduct_name()
                + "}";
    }
}
