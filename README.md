# Landbay Coding Challenge
A program to output the best (or 'a' best) allocation of mortgages to funders.
### Running the Program
This application uses Gradle. Once you are in the directory containing the project you can run `gradle build` and 
`gradle run`.

After running the application, a menu will come up asking you which method to use.

> Chose which method to assign Mortgages to Funders:\
> 1: Assign each Funder 1 Mortgage per "order"\
> 2: Share all Mortgages of Product type between Funders requesting that Product

Enter `1` or `2` in the terminal to select option.

### Assumptions
- The CSV files containing the data are in the correct format with no null fields
  - I have worked primarily on functionality of the program with less consideration towards the potential 
    bugs/errors so far.

### Defining "fairness"
Since this problem is very vague with no clear direction for how to distribute the mortgages to the funders, there 
are many potential interpretations for the solution. Below are the two solutions I have come up with for this coding 
challenge:

1. The first method assumes that each entry in the funded_products_to_funder.csv file corresponds to that funder 
   being assigned one mortgage in the pool. For this method to be fair, the Funder objects are shuffled to remove 
   any predetermined bias from the input file as well as any alphabetical bias in the funder names. Funders are then 
   assigned mortgages in descending order from the FunderRequests list. It is therefore "fair" since 
2. The second method assumes that all mortgages of type 1 will be assigned between all funders requesting that type. 
   In effect, this becomes a "Partitioning problem" or a "Multiway number partitioning problem", both of which are 
   NP-complete. As a result, for the time being, I have implemented a "Greedy Algorithm" that assigns mortgages from 
   a sorted Mortgage List to the funder with the lowest current sum of loans.

Note: There are many other implementations for the second method, including "Largest Differencing Method" and a 
"Multifit Algorithm", which may have better performance for mortgage allocations.

### Bonus Questions

#### Question 1
Ideas for implementing this includes:
- Using a penalty function that takes into consideration the distance from properties currently funding. This would 
  be a baseline penalty if the properties are right next to each other, dropping off to a penalty of 0 at a max 
  distance away from said property.
- Use of a postcode "checker" so that two mortgages with the same first half of the postcode cannot be funded by the 
  same funder (or using a penalty function for each property with the same area code e.g. a 20% loss in "potential 
  profit" for each property owned in the same postcode).
#### Question 2
Ideas for implementing this includes:
- Changing of the assignment methods to allow for this new condition:
  - Only applicable to method 2 as this is a partition problem, and we can adapt this by changing the comparison 
    function of the greedy algorithm.
  - Instead of assigning next property in the sorted mortgage list to the funder with the lowest current sum, we can 
    assign it to the funder with the current sum that is the furthest below the current distribution of mortgages.
    - For example, Product P1 to be funded 60% by Funder A and 40% by Funder B, if the current distribution of 
      mortgages means that A owns a ratio of 64% and B has 36%, then B will be assigned the next mortgage in the 
      list and the ratios can be recalculated