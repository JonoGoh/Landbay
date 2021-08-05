package landbay;

public class Funder implements Comparable<Funder> {
    private String funder_name;
    private String product_id;

    public Funder() {}

    public Funder(String funder_name, String product_id) {
        this.funder_name = funder_name;
        this.product_id = product_id;
    }

    public int compareTo(Funder funder) {
        return product_id.compareTo(funder.getProduct_id());
    }

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
