package landbay;

/*
 * Funder Class for each "order" in funded_products_by_funder.csv
 * Implements Comparable so that it can be sorted by product type
 */
public class Funder implements Comparable<Funder> {
    private String funder_name;
    private String product_id;

    /*
     * Constructors
     */
    public Funder() {}

    public Funder(String funder_name, String product_id) {
        this.funder_name = funder_name;
        this.product_id = product_id;
    }

    public int compareTo(Funder funder) {
        return product_id.compareTo(funder.getProduct_id());
    }

    /*
     * Setters/Getters/toString
     */
    public void setFunder_name(String funder_name){
        this.funder_name = funder_name;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFunder_name() {
        return funder_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String toString() {
        return "Funder{funder_name=" + funder_name
                + " product_id=" + product_id
                + "}";

    }
}
