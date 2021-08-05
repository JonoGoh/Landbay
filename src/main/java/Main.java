/*
 * Main --- Program to allocate mortgages to funders, based on the product that was selected
 *          Ensures the allocation is as fair as possible between funders
 *
 * @author  Jonathan Goh
 */

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import landbay.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    // Relative paths to each file
    private static final String DEFAULT_PATH
            = "\\src\\main\\resources";
    private static final String CSV_MORTGAGES
            = "\\mortgages.csv";
    private static final String CSV_PRODUCTS
            = "\\products.csv";
    private static final String CSV_FUNDED_PRODUCTS_BY_FUNDER
            = "\\funded_products_by_funder.csv";

    /*
     * funderProducts - The final output. A HashMap containing Funder name as the Key and the list of all Mortgages it
     *                  has been assigned in an ArrayList as the Value
     * funderRequests - A list of all funded products by each funder
     * mortgageMap    - Hashmap containing all mortgages by product type
     * products       - Hashmap containing all Products by product name
     */
    private static Map<String, ArrayList<Mortgage>> funderProducts = new HashMap<String, ArrayList<Mortgage>>();
    private static List<Funder> funderRequests = new ArrayList<Funder>();
    private static Map<String, ArrayList<Mortgage>> mortgageMap = new HashMap<String, ArrayList<Mortgage>>();
    private static Map<String, Product> products = new HashMap<String, Product>();

    private static boolean debug = false;

    public static void main (String[] args) throws IOException {
        // Generate path to be used in data reading methods
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String PWD = System.getProperty("user.dir") + DEFAULT_PATH;

        // Read data from relevant files and populate datastructures
        readProducts(PWD + CSV_PRODUCTS);
        readMortgages(PWD + CSV_MORTGAGES);
        readFunders(PWD + CSV_FUNDED_PRODUCTS_BY_FUNDER);

        // Menu for different allocation types since I was unsure about how to implement it
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Chose which method to assign Mortgages to Funders: ");
        System.out.println("1: Assign each Funder 1 Mortgage per \"order\"");
        System.out.println("2: Share all Mortgages of Product type between Funders requesting that Product");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String option = reader.readLine();

        switch (option) {
            case "1" -> assignMortgages1();
            case "2" -> assignMortgages2();
            default -> System.out.println("Invalid Option selected");
        }

        printAssignedMortgages();
    }

    /*
     * Read methods to read data from the relevant files for processing later.
     * Use of OpenCSV to read data
     */
    public static void readProducts(String fileName) {
        try {
            System.out.println("Reading " + fileName);
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).build();
            products = reader.readAll().stream().map(data-> {
                Product product = new Product();
                product.setProduct_id(data[0]);
                product.setRate(data[1]);
                product.setProduct_name(data[2]);
                mortgageMap.computeIfAbsent(data[0], k -> new ArrayList<Mortgage>());
                return product;
            }).collect(Collectors.toMap(Product::getProduct_id, Function.identity()));
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public static void readMortgages(String fileName) {
        try {
            System.out.println("Reading " + fileName);
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).build();
            List<Mortgage> mortgageList = reader.readAll().stream().map(data-> {
//                Mortgage mortgage = new Mortgage(products.get(data[2]));
                Product productRef = products.get(data[2]);
                Mortgage mortgage = new Mortgage();
                mortgage.setMortgage_id(data[0]);
                mortgage.setProduct_id(data[2]);
                mortgage.setLoan_amount(data[1]);
                mortgage.setPostcode(data[3]);
                if (productRef == null) {
                    products.put(data[2], null);
                } else {
                    mortgage.setRate(productRef.getRate());
                    mortgage.setProduct_name(productRef.getProduct_name());
                }
                return mortgage;
            }).collect(Collectors.toList());
            mortgageList.forEach(m -> {
                mortgageMap.computeIfAbsent(m.getProduct_id(), k -> new ArrayList<Mortgage>());
                mortgageMap.get(m.getProduct_id()).add(m);
            });
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public static void readFunders(String fileName) {
        try {
            System.out.println("Reading " + fileName);
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).build();
            funderRequests = reader.readAll().stream().map(data-> {
                Funder funder = new Funder();
                funder.setFunder_name(data[0]);
                funder.setProduct_id(data[1]);
                if (!funderProducts.containsKey(data[0])) {
                    funderProducts.put(data[0], new ArrayList<Mortgage>());
                }
                return funder;
            }).collect(Collectors.toList());
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    /*
     * Basic Implementation of assigning Mortgages to Funders. We randomise the order of funder requests to remove any
     * alphabetical or input order bias, creating a more "fair" distribution of Mortgages. Mortgages are allocated to
     * funders by highest "profit" amount. One per funder request.
     *
     * O(nlog(n)) complexity as we sort the Mortgages by loan amount.
     */
    public static void assignMortgages1() {
        mortgageMap.values().forEach(Collections::sort);
        Collections.shuffle(funderRequests);
        funderRequests.forEach(x -> {
            if (!mortgageMap.get(x.getProduct_id()).isEmpty()) {
                funderProducts.get(x.getFunder_name())
                        .add(mortgageMap.get(x.getProduct_id()).remove(0));
            }
        });
        if (debug) funderProducts.forEach((key, value) -> System.out.println(key + " " + value));
    }

    /*
     * A different approach to the assigning of mortgages whereby all mortgages of a product type are shared between all
     * funders requesting mortgages from that product type. This effectively becomes a Partitioning problem which is an
     * NP-complete problem. My approach here is to use a Greedy algorithm with the use of a sort to improve the overall
     * fairness of the algorithm.
     *
     * An improvement could be to use the Karmarkar-Karp Differencing Algorithm
     *
     * O(nlog(n)) complexity as we sort the Mortgages by loan amount.
     */
    public static void assignMortgages2() {
        mortgageMap.values().forEach(Collections::sort);
        Collections.sort(funderRequests);

        Iterator<Funder> iterator = funderRequests.iterator();
        String currProduct = "";
        ArrayList<String> currFunders = new ArrayList<String>();
        while (iterator.hasNext()){
            Funder curr = iterator.next();
            if (!curr.getProduct_id().equals(currProduct)) {
                if (!currProduct.equals("")) fitMortgages(currProduct, currFunders);
                currProduct = curr.getProduct_id();
                currFunders.clear();
            }
            currFunders.add(curr.getFunder_name());
        }
        fitMortgages(currProduct,currFunders);

    }

    /*
     * Implementation of Greedy Algorithm for a partitioning problem.
     * Loops over the list of mortgages, and adds each mortgage to the funder with the lowest current loan sum.
     */
    public static void fitMortgages(String P, ArrayList<String> L) {
        Collections.shuffle(L);
        ArrayList<Double> sums = new ArrayList<Double>(Collections.nCopies(L.size(), 0.0));
        ArrayList<Mortgage> M = mortgageMap.get(P);

        for (Mortgage m : M) {
            int index = sums.indexOf(Collections.min(sums));
            funderProducts.get(L.get(index)).add(m);
            Double currSum = sums.get(index);
            sums.set(index, currSum + m.calculateOneYearProfit());
        }
    }

    /*
     * Output the results in the console. List of mortgage IDs output for each funder.
     */
    public static void printAssignedMortgages() {
        funderProducts.forEach((key, value) -> {
            StringBuilder buffer = new StringBuilder();
            buffer.append(key).append(" is assigned Mortgages:\t");
            for (Mortgage mortgage : value) {
                buffer.append(" ").append(mortgage.getMortgage_id());
            }
            System.out.println(buffer);
        });
    }
}
