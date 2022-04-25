package testcookies;

import java.io.*;
import java.util.Scanner;

public class StatisticsAndImputers {
    private String filename, newfile;//, header;
//    private int answer, count;
    private String[][] value;
    private String[] valueStr;
    private double[] valueDbl;

    //-------------------------------------------------------------STATISTICS---------------------------------------------------------------------------------------------------
    public void statistics() {

        Scanner s = new Scanner(System.in);
        System.out.print("Enter the name of the file: ");
        filename = s.nextLine()+".csv";

        System.out.println("Choose one column.");
        System.out.println("Does the column contain non-numeric(string) values?");
        System.out.println("1 : YES ");
        System.out.println("2 : NO ");
        int answer = s.nextInt();

        System.out.print("Enter column header's title: ");
        String header = s.next();

//        String[][] value=new String[countRow(filename)][countColumn(filename)];
        value=new String[countRow(filename)][countColumn(filename)];
//        typeOfColumnStats(answer,filename,header,value);
        typeOfColumnStats(answer,header);
    }

    public void typeOfColumnStats(int answer, String header) {

        try{
            //read file and store data into array String
            Scanner sn=new Scanner(new FileInputStream(filename));
            int i=0;
            while(sn.hasNextLine()){
                String[] line=sn.nextLine().split(",");
                for(int j=0;j<line.length;j++){
                    value[i][j]=line[j];
                }
                i++;
            }
            sn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        switch(answer) {
            case 1-> statsNonNumericValue(header);
            case 2-> statsNumericValue(header);
        }
    }

    public void statsNonNumericValue(String header) {

        Scanner s = new Scanner(System.in);

        //store the data of selected column in new array String
        valueStr = new String[countRow(filename)-1];
        int f=0;
        while(f<countColumn(filename)) {
            String check = value[0][f];
            if(check.equalsIgnoreCase(header)) {
                for(int k=1, y=0; y < countRow(filename)-1; k++, y++){
                    valueStr[y] = value[k][f];
                }
            }
            f++;
        }
        statsString();
//        System.out.print("\nDo you want to repeat the process? ");
//        String pick = s.nextLine();
//
//        if(pick.equalsIgnoreCase("yes"))
//            repeatStatistics();
    }

    public void statsNumericValue(String header) {

        Scanner s = new Scanner(System.in);
        valueDbl = new double[countRow(filename)-1];

        //convert array string(value[][]) to array double(valueDbl[]) for computation process
        int f = 0, count = 0;
        while(f<countColumn(filename)) {
            String check = value[0][f];
            if(check.equalsIgnoreCase(header)) {
                for(int k=1, y=0; y<countRow(filename)-1; k++, y++){
                    if(value[k][f]!=null)
                        valueDbl[y] = Double.parseDouble(value[k][f]);
                }
                count++;
            }
            f++;
        }
        statsDouble();

        System.out.print("\nDo you want to repeat the process? ");
        String pick = s.nextLine();
        if(pick.equalsIgnoreCase("yes"))
            repeatStatistics();
    }

    public void statsString() {
        System.out.println("\n----------Statistics----------\n");
        System.out.println("Mode: " + modeString());
        askImputers();
    }

    public void statsDouble() {

        System.out.println("\n----------Statistics----------\n");
        System.out.print("The values: ");
        for (double x : valueDbl)
            System.out.print(x + " ");

        System.out.println("\nMinimum value: " + minValue());
        System.out.println("Maximum value: " + maxValue());
        range();
        System.out.printf("Mean: %.2f\n", mean());
        modeDouble();
        System.out.println("Median: " + median());
        standardDeviation();
        System.out.printf("\nVariance: %.2f\n", variance());
        askImputers();
    }

    public void repeatStatistics() {
        statistics();
    }

//--------------------------------------------------------------IMPUTERS--------------------------------------------------------------------------------------------------------

    public void askImputers(){
        Scanner s = new Scanner(System.in);
        System.out.println("Do you want to fill in the missing value?");
        System.out.println("1 : YES ");
        System.out.println("2 : NO ");
        int num = s.nextInt();
        if (num==1){
            imputers();
        }
        else
        {
            System.out.println("process ended");
            System.out.print("\nDo you want to repeat the process? ");
            String pick = s.nextLine();

            if(pick.equalsIgnoreCase("yes"))
                repeatStatistics();
        }
    }

    public void imputers() {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the name of the file.\nIf you have gone through this process before, please select the latest file with modified data: ");
        String filename = s.nextLine()+".csv";
        System.out.print("Enter new file name: ");
        newfile = s.nextLine()+".csv";

        System.out.println("Choose one column.");
        System.out.println("Does the column contain non-numeric(string) values?");
        System.out.println("1 : YES ");
        System.out.println("2 : NO ");
        int answer = s.nextInt();

        System.out.print("Enter column header's title: ");
        String header = s.next();

        String[][] value=new String[countRow(filename)][countColumn(filename)];

        typeOfColumnImputers(answer,filename,header,value);
    }

    public void typeOfColumnImputers(int answer, String filename, String header,String[][] value) {

        try{
            //read file and store data into array String
            Scanner sn=new Scanner(new FileInputStream(filename));
            int i=0;
            while(sn.hasNextLine()){
                String[] line=sn.nextLine().split(",");
                for(int j=0;j<line.length;j++){
                    value[i][j]=line[j];
                }
                i++;
            }
            sn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        switch(answer) {
            case 1-> imputersNonNumericValue(filename,value,header);
            case 2-> imputersNumericValue(filename,value,header);
        }
    }

    public void imputersNonNumericValue(String filename, String[][] value, String header) {

        Scanner s = new Scanner(System.in);

        //store the data of selected column in new array String
        String[] valueStr = new String[countRow(filename)-1];
        int f=0;
        while(f<countColumn(filename)) {
            String check = value[0][f];
            if(check.equalsIgnoreCase(header)) {
                for(int k=1, y=0; y < countRow(filename)-1; k++, y++){
                    valueStr[y] = value[k][f];
                }
            }
            f++;
        }
        //check for number of cells with null value
        int count = 0;
        for(String x : valueStr) {
            if(x==null||x.isBlank())
                count++;
        }
        imputersString(count,filename,value,header,valueStr);
        System.out.print("\nDo you want to repeat the process? ");
        String pick = s.nextLine();

        if(pick.equalsIgnoreCase("yes"))
            repeatImputers();
    }

    public void imputersNumericValue(String filename, String[][] value, String header) {

        Scanner s = new Scanner(System.in);
        double[] valueDbl = new double[countRow(filename)-1];

        //convert array string(value[][]) to array double(valueDbl[]) for computation process
        int f = 0, count = 0;
        while(f<countColumn(filename)) {
            String check = value[0][f];
            if(check.equalsIgnoreCase(header)) {
                for(int k=1, y=0; y<countRow(filename)-1; k++, y++){
                    if(value[k][f]!=null)
                        valueDbl[y] = Double.parseDouble(value[k][f]);
                }
                count++;
            }
            f++;
        }
        imputersDouble(count,filename,value,header);

        System.out.print("\nDo you want to repeat the process? ");
        String pick = s.nextLine();
        if(pick.equalsIgnoreCase("yes"))
            repeatImputers();
    }

    public void imputersString(int count, String filename, String[][] value, String header, String[] valueStr) {

        Scanner s = new Scanner(System.in);

        System.out.println("\n----------Imputation----------\n");
        System.out.println("Number of cell(s) with missing/null data: " + count);

        if(count!=0) {

            fillInByModeString(filename,header,value);
            writeToNewfile(filename,value);
            System.out.println("Done!");
        }
        else
            System.out.println("There is no cell with missing data! Imputation process is not needed!\n");
    }

    public void imputersDouble(int count, String filename, String[][] value, String header) {

        Scanner s = new Scanner(System.in);

        System.out.println("\n----------Imputation----------\n");
        System.out.println("Number of cell(s) with missing/null data: " + count);

        if(count!=0) {

            System.out.print("Choose one value to fill in the missing data. Select-> (1: Mode or 2: Median or 3: Mean): ");
            int choose = s.nextInt();
            switch(choose) {
                case 1-> fillInByModeDouble(filename,header,value);
                case 2-> fillInByMedian(filename,header,value);
                case 3-> fillInByMean(filename,header,value);
            }
            writeToNewfile(filename,value);
            System.out.println("Done!");

        }
        else
            System.out.println("There is no cell with missing data! Imputation process is not needed!\n");
    }

    //method to repeat the whole process
    public void repeatImputers() {
        imputers();
    }
    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void writeToNewfile(String filename, String[][] value) {

        //write to new file
        try{
            PrintWriter pw=new PrintWriter(new FileOutputStream(newfile));
            for(int k=0;k<countRow(filename);k++){
                for(int j=0;j<countColumn(filename)-1;j++){
                    pw.print(value[k][j]+",");
                }
                pw.println(value[k][countColumn(filename)-1]);
            }
            pw.close();
        }catch(IOException e){}
    }

    public int countColumn(String filename){

        Scanner s = new Scanner(System.in);
        try{
            Scanner out = new Scanner(new FileInputStream(filename));
            String[] line=out.nextLine().split(",");
            return line.length;
        } catch (FileNotFoundException e) {
            return 0;
        }
    }

    public int countRow(String filename){

        Scanner s = new Scanner(System.in);
        try{
            Scanner out = new Scanner(new FileInputStream(filename));
            int countrow=0;
            while(out.hasNextLine()){
                out.nextLine();
                countrow++;
            }
            return countrow;
        } catch (FileNotFoundException e) {
            return 0;
        }
    }
//--------------------------------------------------------------Computation for statistics--------------------------------------------------------------------------------------------------------

    public String modeString() {
        String mode = null;
        int maxCount = 0, h, j;

        for (h = 0; h < valueStr.length; ++h) {

            int count = 0;
            for (j = 0; j < valueStr.length; ++j) {
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

    public double modeDouble() {
        int mode = 0, maxCount = 0, i, j;

        for (i = 0; i < valueDbl.length; ++i) {

            int count = 0;
            for (j = 0; j < valueDbl.length; ++j) {
                if (valueDbl[j] == valueDbl[i])
                    ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                mode = (int)valueDbl[i];
            }
        }
        return mode;
    }

    public double median() {
        if(valueDbl.length%2==1)
            return valueDbl[(valueDbl.length+1)/2-1];
        else
            return (valueDbl[valueDbl.length/2-1]+valueDbl[valueDbl.length/2])/2;
    }

    public double mean() {
        // Compute mean (average
        // of elements)
        double sum = 0;

        for (int i = 0; i < valueDbl.length; i++)
            sum += valueDbl[i];
        double mean = (double)sum / (double)valueDbl.length;
        return mean;
    }

    public double variance() {
        // Compute sum squared differences with mean.
        double sqDiff = 0;
        for (int i = 0; i < valueDbl.length; i++)
            sqDiff += (valueDbl[i] - mean()) * (valueDbl[i] - mean());

        double variance = (double)sqDiff / valueDbl.length;
        return variance;
    }

    public void standardDeviation(){
        System.out.printf("Standard deviation: %.2f", Math.sqrt(variance()));
    }

    public double minValue() {

        double minValue = valueDbl[0];
        for(int i=1;i<valueDbl.length;i++){
            if(valueDbl[i] < minValue && valueDbl[i]!=0){
                minValue = valueDbl[i];
            }
        }
        return minValue;
    }

    public double maxValue(){
        double maxValue = valueDbl[0];
        for(int i=1;i < valueDbl.length;i++){
            if(valueDbl[i] > maxValue){
                maxValue = valueDbl[i];
            }
        }
        return maxValue;
    }

    public void range() {
        System.out.println("Range: " + (maxValue()-minValue()));
    }

//----------------------------------------------------------choices of input values for imputation----------------------------------------------------------------------------

    public void fillInByModeString(String filename, String header, String[][] value) {

        for(int m=0; m<countColumn(filename);m++){
            if(value[0][m].equalsIgnoreCase(header)) {
                for(int c=1; c<countRow(filename);c++){
                    if(value[c][m]==null||value[c][m].equalsIgnoreCase(" ")||value[c][m].isBlank()||value[c][m].isEmpty())
                        value[c][m] = modeString();
                }
            }
        }
    }

    public void fillInByModeDouble(String filename, String header, String[][] value) {

        for(int i=0; i<countColumn(filename);i++){
            if(value[0][i].equalsIgnoreCase(header)) {
                for(int c=1; c<countRow(filename);c++){
                    //check for cells in the column that contain no value
                    if(value[c][i]==null||value[c][i].equalsIgnoreCase(" ")||value[c][i].isBlank()||value[c][i].isEmpty()) {
                        //fill in the blank
                        value[c][i] = String.valueOf(modeDouble());
                    }
                }
            }
        }
    }

    public void fillInByMedian(String filename, String header, String[][] value) {

        for(int i=0; i<countColumn(filename);i++){
            if(value[0][i].equalsIgnoreCase(header)) {
                for(int c=1; c<countRow(filename);c++){
                    //check for cells in the column that contain no value
                    if(value[c][i]==null||value[c][i].equalsIgnoreCase(" ")||value[c][i].isBlank()||value[c][i].isEmpty()) {
                        //fill in the blank
                        value[c][i] = String.valueOf(median());
                    }
                }
            }
        }
    }

    public void fillInByMean(String filename, String header, String[][] value) {

        for(int i=0; i<countColumn(filename);i++){
            if(value[0][i].equalsIgnoreCase(header)) {
                for(int c=1; c<countRow(filename);c++){
                    //check for cells in the column that contain no value
                    if(value[c][i]==null||value[c][i].equalsIgnoreCase(" ")||value[c][i].isBlank()||value[c][i].isEmpty()) {
                        //fill in the blank
                        value[c][i] = String.valueOf(mean());
                    }
                }
            }
        }
    }
}
