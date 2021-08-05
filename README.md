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