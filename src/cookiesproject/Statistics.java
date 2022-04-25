package cookiesproject;

import java.io.*;
import java.util.Scanner;

public class Statistics {
    String mode;
    int mode2;
    double med;
    double maxValue;
    double minValue;
    double strd;
    double variance;
    double mean;
    int countcol , countrow ;
    String filename;

    public void statistics(){

        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name : ");
        filename = s.nextLine()+".csv";

        System.out.println("Choose one column.");
        System.out.print("Does the column contain non-numeric(string) values? Select-> (1: Yes or 2: No): ");
        int answer = s.nextInt();

        switch(answer){
            case 1 -> nonNumericalValue(filename);
            case 2 -> numericalValue(filename);
        }

        if (answer==1){
            askChoice();
        }
        else
            writeInFile(mode);

    }

    public void nonNumericalValue(String filename) {
        Scanner s = new Scanner(System.in);
//      display count number of columns
//        int countcol = 0, countrow = 0;
        try{
            try (Scanner out = new Scanner(new FileInputStream(filename))) {
                String[] col=out.nextLine().split(",");
                while (out.hasNextLine()){
                    out.nextLine();
                    countrow++;
                }
                countcol=col.length;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        try{

            Scanner ArrInputStream = new Scanner(new FileInputStream(filename));
            String[][] value = new String[countrow+1][];

            System.out.print("Enter column header's title: ");
            String header = s.nextLine();

            for (int i = 0; i <= countrow; i++)
                value[i] = ArrInputStream.nextLine().split(","); //value array

            System.out.println("----------Statistics----------");

            String[] valueStr = new String[countrow];
            int i = 0;
            while(i<countcol) {
                String check = value[0][i];
                if(check.equalsIgnoreCase(header)) {
                    int n = i;
                    for(int k=1, y=0; k<countrow; k++, y++){
                        valueStr[y] = value[k][n];
                    }
                    System.out.println("Mode: " + modeString(valueStr, countrow));
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void numericalValue(String filename) {

        Scanner s = new Scanner(System.in);
//      display count number of columns
//        int countcol = 0, countrow = 0;
//        countcol = 0, countrow = 0;
        try{
            try (Scanner out = new Scanner(new FileInputStream(filename))) {
                String[] col=out.nextLine().split(",");  // how know?
                while (out.hasNextLine()){
                    out.nextLine();
                    countrow++;
                }
                countcol=col.length;
            } // how know?
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        double[] valueDbl;
        try {
            Scanner ArrInputStream = new Scanner(new FileInputStream(filename));
            String[][] value = new String[countrow+1][];
            System.out.print("Enter column header's title: ");
            String header = s.nextLine();
            for (int i = 0; i <= countrow; i++)
                value[i] = ArrInputStream.nextLine().split(","); //value array

            System.out.println("----------Statistics----------");

            valueDbl = new double[countrow];

            int i = 0;
            while(i<countcol) {
                String check = value[0][i];
                if(check.equalsIgnoreCase(header)) {
                    int n = i;
                    for(int k=1, y=0; y<countrow; k++, y++){

                        valueDbl[y] = Double.parseDouble(value[k][n]);

                    }
                }
                i++;
            }   // Loop through all columns of current row
            System.out.print("The values: ");
            for (double x : valueDbl)
                System.out.print(x + " ");


            System.out.println("\nMinimum value: " + minValue(valueDbl));
            System.out.println("Maximum value: " + maxValue(valueDbl));
            range(valueDbl);
            System.out.printf("Mean: %.2f\n", mean(valueDbl, countrow));
            modeDouble(valueDbl, countrow);
            System.out.println("Median: " + median(valueDbl, countrow));
            standardDeviation(valueDbl, countrow);
            System.out.printf("\nVariance: %.2f\n", variance(valueDbl, countrow));

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------
    public double mean(double[] valueDbl, int countrow) {
        // Compute mean (average
        // of elements)
        double sum = 0;

        for (int i = 0; i < countrow; i++)
            sum += valueDbl[i];
//        double mean = (double)sum / (double)countrow;
        mean = (double)sum / (double)countrow;
        return mean;
    }

    public double variance(double[] valueDbl, int countrow) {
        // Compute sum squared differences with mean.
        double sqDiff = 0;
        for (int i = 0; i < countrow; i++)
            sqDiff += (valueDbl[i] - mean(valueDbl, countrow)) * (valueDbl[i] - mean(valueDbl, countrow));

//        double variance = (double)sqDiff / countrow;
        variance = (double)sqDiff / countrow;
        return variance;
    }

    public void standardDeviation(double[] valueDbl, int countrow){
        strd = Math.sqrt(variance(valueDbl, countrow));
        System.out.printf("Standard deviation: %.2f", strd);
    }

    public double minValue(double[] valueDbl) {

//        double minValue = valueDbl[0];
        minValue = valueDbl[0];
        for(int i=1;i<valueDbl.length;i++){
            if(valueDbl[i] < minValue){
                minValue = valueDbl[i];
            }
        }
        return minValue;
    }

    public double maxValue(double[] valueDbl){
//        double maxValue = valueDbl[0];
        maxValue = valueDbl[0];
        for(int i=1;i < valueDbl.length;i++){
            if(valueDbl[i] > maxValue){
                maxValue = valueDbl[i];
            }
        }
        return maxValue;
    }

    public void range(double[] valueDbl) {
        System.out.println("Range: " + (maxValue(valueDbl)-minValue(valueDbl)));
    }

    public double median(double[] valueDbl, int countrow) {
        if(countrow%2==1){
            med = valueDbl[(countrow+1)/2-1];
            return med;
        }
        else {
            med = (valueDbl[countrow/2-1]+valueDbl[countrow/2])/2;
            return med;
        }
    }

    public void modeDouble(double[] valueDbl, int countrow) {
//        int mode = 0, maxCount = 0, i, j;
        mode2 = 0;
        int maxCount = 0, i, j;

        for (i = 0; i < countrow; ++i) {

            int count = 0;
            for (j = 0; j < countrow; ++j) {
                if (valueDbl[j] == valueDbl[i])
                    ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                mode2 = (int)valueDbl[i];
            }
        }
        System.out.println("Mode: " + mode2);
    }

    public String modeString(String[] valueStr, int countrow) {
//        String mode = null;
        mode = null;
        int maxCount = 0, h, j;

        for (h = 0; h < countrow; ++h) {

            int count = 0;
            for (j = 0; j < countrow; ++j) {
                if (valueStr[j] == null ? valueStr[h] == null : valueStr[j].equals(valueStr[h]))
                    ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                mode = valueStr[h];
            }
        }
        return mode;
    }

    public void askChoice(){
        Scanner s = new Scanner(System.in);
        System.out.print("Which value do you want to write in file?");
        System.out.println("Choice 1 : Mode = " + mode2);
        System.out.println("Choice 2 : Mean = " + mean);
        System.out.println("Choice 3 : Median = " + med);
        int choose = s.nextInt();
        switch (choose){
            case 1 ->writeInFile(Double.toString(mode2));
            case 2 ->writeInFile(Double.toString(mean));
            case 3 ->writeInFile(Double.toString(med));
            default -> writeInFile("no value input");
        }

    }

    public void writeInFile(String input) {
//        try {
//            Scanner sn = new Scanner(new FileInputStream(filename));
//            String[][] fill = new String[countrow][countcol];
//            int i = 0;
//
//            //scan all lines and split save in array
//            while (sn.hasNextLine()) {
//                String[] line = sn.nextLine().split(",");
//                for (int j = 0; j < line.length; j++)
//                    fill[i][j] = line[j];
//                i++;
//            }
//
//            fill[row_predicted_value - 1][column_predicted_value - 1] = input;
//            try {
//                PrintWriter pw = new PrintWriter(new FileOutputStream("Imputers.csv"));
//                for (int k = 0; k < countrow; k++) {
//                    for (int j = 0; j < countcol - 1; j++) {
//                        pw.print(fill[k][j] + ",");
//                    }
//                    pw.println();
//                }
//                pw.close();
//            } catch (IOException e) {
//                System.out.println("Input error");
//            }
//            sn.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        }
    }

}
