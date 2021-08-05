import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import landbay.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static final String DEFAULT_PATH
            = "\\src\\main\\resources";
    private static final String CSV_MORTGAGES
            = "\\mortgages.csv";
    private static final String CSV_PRODUCTS
            = "\\products.csv";
    private static final String CSV_FUNDED_PRODUCTS_BY_FUNDER
            = "\\funded_products_by_funder.csv";
    private static Map<String, ArrayList<Mortgage>> funderProducts = new HashMap<String, ArrayList<Mortgage>>();
    private static List<Funder> funderRequests = new ArrayList<Funder>();
    private static Map<String, ArrayList<Mortgage>> mortgageMap = new HashMap<String, ArrayList<Mortgage>>();
    private static Map<String, Product> products = new HashMap<String, Product>();
//    private static List<Mortgage> mortgageMap;

    private static boolean debug = false;

    public static void main (String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String PWD = System.getProperty("user.dir") + DEFAULT_PATH;

        readProducts(PWD + CSV_PRODUCTS);
        readMortgages(PWD + CSV_MORTGAGES);
        readFunders(PWD + CSV_FUNDED_PRODUCTS_BY_FUNDER);

        if (debug) {
            System.out.println("products:");
            products.forEach((key, value) -> System.out.println(key + " " + value));
            System.out.println("mortgageMap:");
            mortgageMap.forEach((key, value) -> System.out.println(key + " " + value));
            System.out.println("mortgageMap:");
            funderRequests.forEach(x -> System.out.println(x));
        }

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

    public static void readProducts(String fileName) {
        try {
            System.out.println("Reading " + fileName);
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).withSkipLines(1).build();
            products = reader.readAll().stream().map(data-> {
                Product product = new Product();
                product.setProduct_id(data[0]);
                product.setRate(data[1]);
                product.setProduct_name(data[2]);
                if (mortgageMap.get(data[0]) == null) mortgageMap.put(data[0], new ArrayList<Mortgage>() );
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

    public static void assignMortgages1() {
        mortgageMap.values().forEach(Collections::sort);
//        mortgageMap.forEach((key, value) -> System.out.println(key + " " + value));
        Collections.shuffle(funderRequests);
        funderRequests.forEach(x -> {
            if (!mortgageMap.get(x.getProduct_id()).isEmpty()) {
                funderProducts.get(x.getFunder_name())
                        .add(mortgageMap.get(x.getProduct_id()).remove(0));
            }
        });
        if (debug) funderProducts.forEach((key, value) -> System.out.println(key + " " + value));
    }

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
    Implementation of Greedy Algorithm for a partitioning problem.
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

    public static void printAssignedMortgages() {
        funderProducts.entrySet().forEach(entry -> {
            String buffer = "";
            buffer += entry.getKey() + " is assigned Mortgages:\t";
            for (Mortgage mortgage : entry.getValue()) {
                buffer += " " + mortgage.getMortgage_id();
            }
            System.out.println(buffer);
        });
    }
}
