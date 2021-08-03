import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.*;
import landbay.*;

public class Main {
    private static final String DEFAULT_PATH
            = "\\src\\main\\resources";
    private static final String CSV_MORTGAGES
            = "\\mortgages.csv";
    private static final String CSV_PRODUCTS
            = "\\products.csv";
    private static final String CSV_FUNDED_PRODUCTS_BY_FUNDER
            = "\\funded_products_by_funder.csv";
    private static HashMap<String, List<Product>> funderMap;
    private static HashMap<Product, List<Mortgage>> productMap;

    public static void main (String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String PWD = System.getProperty("user.dir") + DEFAULT_PATH;

    }

    public static void test (String PWD) {
        Product something = new Product("P1", 2.5, "A product");
        System.out.println(something.getProduct_id());
        readDataLineByLine(PWD + CSV_PRODUCTS);
    }

    public static void readDataLineByLine(String file) {
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            System.out.println("Contents of " + file + ":");
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "I\t");
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
