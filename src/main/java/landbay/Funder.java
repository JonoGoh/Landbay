package landbay;

public class Funder {
    private String funder_name;

    public Funder(String funder_name) {
        this.funder_name = funder_name;
    }

    public String getFunder_name() {
        return funder_name;
    }

    public void printInfo() {
        System.out.println("Funder name:\t" + funder_name);
    }

    public static void main (String[] args) {
        Funder funder1 = new Funder("Eagle");
        funder1.printInfo();
    }
}
