package project6;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("*********WELCOME*********");
        chooseStep();
    }

    public static void chooseStep(){
        Scanner s = new Scanner(System.in);
        System.out.println("1: Create/edit file");
        System.out.println("2: Display file");
        System.out.println("3: Column concatenation");
        System.out.println("4: Row concatenation");
        System.out.println("5: Subset Row");
        System.out.println("6: Subset Column");
        System.out.println("7: Sort by column");
        System.out.println("8: Drop duplicate rows");
        System.out.println("9: Remove rows with missing data"); // takde lagi

        System.out.println("10: Variance, Mean, Min, Max, Median, Mode, Range, St. Dev");
        System.out.println("11: Standard Scaling and Min Max Scaling");
        System.out.println("12: K-Nearest Neighbors");
        System.out.println("13: Error metric");
        System.out.println("14: Web Scrapping"); // additional challenge
        System.out.print("Enter number : ");
        int num = s.nextInt();

        Manipulation manipulate = new Manipulation();
        Scalers scale = new Scalers(); // for standard scaling
        KNN kNearest = new KNN();   //for k-nearest neighbour
        ErrorMetrics error = new ErrorMetrics();
        Statistics statistic = new Statistics();
        WebScrapping web = new WebScrapping();

        switch(num){
            case 1 -> manipulate.editFile();
            case 2 -> manipulate.readfile();
            case 3 -> manipulate.ColumnConcatenation();
            case 4 -> manipulate.RowConcatenation();
            case 5 -> manipulate.subsetRow();
            case 6 -> manipulate.subsetColumn();
            case 7 -> manipulate.sortByColumn();
            case 8 -> manipulate.duplicate();
            case 9 -> manipulate.dropNull();  //
            case 10 -> statistic.mainStatistics(); // number 3
            case 11 -> scale.mainScalers(); // number 4
            case 12 -> kNearest.mainKNN(); // number 5
            case 13 -> error.mainError(); // number 6
            case 14 -> web.mainWebScraper(); // add challenge
            default -> System.out.println("Process Ended");
        }
    }

}
