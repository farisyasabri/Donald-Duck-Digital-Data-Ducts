package project6;

import java.io.*;
import java.util.Scanner;

public class KNN {

    private int row, column, column_predicted_value, row_predicted_value, numOfColumnToRefer, k, amountofDataUsed;
    private int[] columnToRefer;
    private String filename;
    private float[][] saveData;
    private float[] distance, copyDistance;
    private float[] dataForRegressor;
    private String[] dataForClassifier;
    private int[] Index;
    private float[] meanColumn;
    private float[][] saveStandardScaling ;
    float[] sd;

    public void mainKNN() {
        Scanner s = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter filename : ");
        filename = s.nextLine() + ".csv";
        System.out.print("Enter which column do you want to get predicted value : ");
        column_predicted_value = s.nextInt();
        System.out.print("Enter which row of the predicted value (this include the header) : ");
        row_predicted_value = s.nextInt();
        System.out.print("How many column do you want to refer ? ");
        numOfColumnToRefer = s.nextInt();
        System.out.println("State the number of column");
        columnToRefer = new int[numOfColumnToRefer];
        for (int i = 0; i < numOfColumnToRefer; i++) {
            System.out.print("Column " + (i + 1) + " : ");
            columnToRefer[i] = s.nextInt()-1;// - 1;
        }

        System.out.print("Enter k value : ");
        k = s.nextInt();
        row = CountRow();
        column = CountColumn();
        amountofDataUsed = (row - 1) * 7 / 10;
        System.out.println("This file has " + row + " row and " + column + " column");
        System.out.println("From " + (row - 1) + " lines of data, " + " we only used " + amountofDataUsed + " lines of data for knn");
        saveData();

        System.out.print("Do you wish to use scaling before find the prediction? [yes or no] : " );
        String ansScaling = s.next();
        if (ansScaling.equalsIgnoreCase("yes")) {
            mean();
            standardDeviation();
            standardScaling();
        }
        EuclideanDistance(ansScaling);

        System.out.println("Which method to use to predict the value?");
        System.out.println("1 : k-nn regression ");
        System.out.println("2 : k-nn classification ");
        System.out.print("Number : ");
        int choose = s.nextInt();
        switch (choose) {
            case 1 -> getdataForRegressor();    // scan column that has blank space and store value that has the same value with getIndex array
            case 2 -> getdataForClassifier();
        }

        System.out.println();
        Manipulation ask = new Manipulation();
        ask.askQuestion();

    }

    public int CountRow() {
        int countRow = 0;
        try {
            Scanner out = new Scanner(new FileInputStream(filename));
            while (out.hasNextLine()) {
                out.nextLine();
                countRow++;
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return countRow;
    }

    public int CountColumn() {
        int countColumn = 0;
        try {
            Scanner out = new Scanner(new FileInputStream(filename));

            while (out.hasNextLine()) {
                String[] column = out.nextLine().split(",");
                countColumn = column.length + 1;
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return countColumn;
    }

    public void saveData() {
        float[][] saveData = new float[row - 1][numOfColumnToRefer];
        int counterRow = 0; //row utk savedata
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                if (counterRow == 0) {
                    file.nextLine(); //skip header
                }
                String[] data = file.nextLine().split(",");
                for (int j = 0; j < numOfColumnToRefer; j++) { //utk column
                    saveData[counterRow][j] = Float.parseFloat(data[columnToRefer[j]]); //convert each row to double value and store it in array
                }
                counterRow++;
            }
            file.close();
            setSaveData(saveData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

    }

    public void setSaveData(float[][] saveData) {
        this.saveData = saveData;
    }

    // -------------------------------------- perform scaling --------------------------------------------------------

    // method to calculate the mean
    public void mean(){
        float total=0;
        int cnt = 0;

        meanColumn = new float[numOfColumnToRefer];

        for (int i=0;i<numOfColumnToRefer;i++) {
            for (int j = 0; j < row-1; j++)    //-1
            {
                total += saveData[j][i];
                cnt++;
            }
            meanColumn[i] = total/ cnt;
        }

        for (int i = 0; i < numOfColumnToRefer; i++) {
                System.out.printf("The mean column " + columnToRefer[i] + " is : %.8f\n", meanColumn[i]);
        }
    }

    // method to calculate the standard deviation
    public void standardDeviation(){
        sd = new float[numOfColumnToRefer];
        float upper = 0, total, cnt = 0;

        for (int i=0;i<numOfColumnToRefer;i++) {
            for (int j = 0; j <row-1; j++) //-1
            {
                total = (float) Math.pow((saveData[j][i] - meanColumn[i]), 2);
                upper += total;
                cnt++;
            }
            sd[i] = (float) Math.sqrt(upper/cnt); // standard deviation formula
        }

        System.out.println();
        for (int j = 0; j < numOfColumnToRefer; j++) {
            System.out.printf("The standard deviation column " + columnToRefer[j] + " is : %.8f\n", sd[j]);
        }
        System.out.println();
    }

    // method to compute standard scaling
    public void standardScaling(){

        saveStandardScaling = new float[row][numOfColumnToRefer];
        System.out.println("Standard Scaling : ");
        for (int i=0;i<numOfColumnToRefer;i++) {
            System.out.println("Column "+ columnToRefer[i]);
            for (int j = 0; j < row-1; j++)    //-1
            {
                saveStandardScaling[j][i] = ((saveData[j][i] - meanColumn[i]) / sd[i]);
                System.out.printf("%.8f\n", saveStandardScaling[j][i]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //--------------------------------------- calculate distance ----------------------------------------------------

    public void EuclideanDistance(String ans) {
        float[] distance = new float[amountofDataUsed]; //70% of total data is used
        System.out.println("Distance :-");
        for (int i = 0; i < amountofDataUsed; i++) { //ex = 7
            float totalSum = 0;
            if (ans.equalsIgnoreCase("yes")) {
                for (int j = 0; j < numOfColumnToRefer; j++) {
                    totalSum += Math.pow((saveStandardScaling[row_predicted_value - 2][j]) - (saveStandardScaling[i][j]), 2);
                }
            }
            else {
                for (int j = 0; j < numOfColumnToRefer; j++) {
                    totalSum += Math.pow((saveData[row_predicted_value - 2][j]) - (saveData[i][j]), 2);
                }
            }
            distance[i] = (float) Math.sqrt(totalSum);
            System.out.printf("%.2f  ", distance[i]);
        }
        System.out.println();

        setDistance(distance);
        arrangeKnnDistance();
    }

    public void setDistance(float[] distance) {
        this.distance = distance;
    }

    public void arrangeKnnDistance() {
        float temp;

        if (row_predicted_value<amountofDataUsed) {
            temp = distance[row_predicted_value-2];
            distance[row_predicted_value-2]=distance[amountofDataUsed-1];
            distance[amountofDataUsed-1]=temp;
        }
        copyDistance = distance.clone();

        for (int pass = 1; pass < amountofDataUsed; pass++) {
            for (int i = 0; i < amountofDataUsed - 1; i++) {
                if (row_predicted_value<amountofDataUsed) {
                    if (i==amountofDataUsed-2)
                        break;
                }
                if (distance[i] > distance[i + 1]) {
                    temp = distance[i];
                    distance[i] = distance[i + 1];
                    distance[i + 1] = temp;
                }
            }
        }
        getIndexForKnn();

//        displayArrangedKnnDistance();
    }

//
//    public void displayArrangedKnnDistance() {
//        System.out.println("sorted distance:- ");
//        for (int i = 0; i < amountofDataUsed; i++) {
//            System.out.printf("%.2f   ", distance[i]);
//        }
//        System.out.println();
//        getIndexForKnn();
//    }


    public void getIndexForKnn() {
        Index = new int[k+1]; //k+1
        int i = 0; //index = 0;
        if (row_predicted_value<amountofDataUsed){ //&&row_predicted_value!=5)
            while(i <k) {
                for (int j = 0; j < distance.length; j++) {
                    if (distance[i] == copyDistance[j]) {
                        Index[i] = j + 1;
                        i++;
                        if(i==k){
                            break;
                        }
                    }
                }
            }
        }
        else{
            Index = new int[k];   //k
            while(i <k) {
                for (int j = 0; j < distance.length; j++) {
                    if (distance[i] == copyDistance[j]) {
                        Index[i] = j + 1;
                        if (Index[i] == row_predicted_value-1) {
                            i= i-1;
                        }
                        i++;
                        if(i==k){
                            break;
                        }
                    }
                }
            }
        }

        arrangeIndex();
    }

    public void arrangeIndex() {
        for (int pass = 1; pass < k ; pass++) {
            for (int i = 0; i < k - 1; i++) {
                if (Index[i] > Index[i + 1]) {
                    int temp = Index[i];
                    Index[i] = Index[i + 1];
                    Index[i + 1] = temp;
                }
            }
        }
        displayGetIndex();      // call method to display the index for row | p/s: will be use to get the value for k-nearest distance
    }

    public void displayGetIndex() {
        System.out.print(k + " nearest index : ");
        for (int i = 0; i < k; i++) {
            System.out.print(Index[i] + "\t");
        }
        System.out.println();
        System.out.println();
    }

    // ------------------------------------------- find regressor ---------------------------------------------------

    public void getdataForRegressor() {
        dataForRegressor = new float[k];    // index length = num of row
        int i = 0, counterRow = 0;
        boolean status = false;
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                for (int x = 0; x < k; x++) {
                    status = counterRow == Index[x];
                    if (status) {
                        break;
                    }
                }
                if (counterRow == 0) {
                    file.nextLine();
                } else if (status) {
                    String[] str = file.nextLine().split(",");
                    dataForRegressor[i] = Float.parseFloat(str[column_predicted_value - 1]);
                    i++;
                } else {
                    file.nextLine();
                }
                counterRow++;

                if (i == k) {
                    break;
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        displayDataForRegressor();
    }

    public void displayDataForRegressor() {
        System.out.print(k + " nearest neighbours are ");
        for (int i = 0; i < k; i++) {
            System.out.print(dataForRegressor[i] + "\t");
        }
        System.out.println();
        calculateRegressor();
    }

    public void calculateRegressor() {
        float regressor, sum = 0; //to calculate sum for kNN
        for (int i = 0; i < k; i++) {
            sum += dataForRegressor[i];
        }
        regressor = sum / k; // regressor = mean
        System.out.printf("%d-nn regressor = %.2f\n", k, regressor);

        writeInFile(Float.toString(regressor));
    }

    //------------------------------------------- find classifier ----------------------------------------------------
    public void getdataForClassifier() {
        dataForClassifier = new String[k];
        int i = 0, counterRow = 0;
        boolean status = false;
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                for (int x = 0; x < k; x++) {
                    status = counterRow == Index[x];  //Index
                    if (status) {
                        break;
                    }
                }
                if (counterRow == 0) {
                    file.nextLine();
                } else if (status) {
                    String[] str = file.nextLine().split(",");
                    dataForClassifier[i] = str[column_predicted_value - 1];
                    i++;
                } else {
                    file.nextLine();
                }
                counterRow++;
                if (i == k) {
                    break;
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        displayDataForClassifier();
        CountTotalString();
    }

    public void displayDataForClassifier() {
        System.out.print(k + " nearest neighbours are ");
        for (int i = 0; i < k; i++) {
            System.out.print(dataForClassifier[i] + "\t");
        }
        System.out.println();
    }

    public void CountTotalString(){
        int i, m=1, sum = 0;
        String[] str = new String[k];
        str[0] = dataForClassifier[0];
        for(i = 1; i<k ; i++){
            for(int j = i-1; j>=0; j--) {
                if (!dataForClassifier[i].equals(dataForClassifier[j])) {
                    sum += 0;
                }
                else{
                    sum+=1;
                }
            }
            if(sum==0){  //if sum=0, it means that the data for the current row is different with the rows before
                str[m] = dataForClassifier[i];
                m++;
            }
        }
        String[] NotRepeatedData = new String[m];
        for(int h = 0; h<m; h++){
            NotRepeatedData[h] = str[h];
        }
        CountFrequencyofData(NotRepeatedData);
    }

    public void CountFrequencyofData(String[] NotRepeatedData){
        int[] sum = new int[NotRepeatedData.length];
        for(int i = 0; i<NotRepeatedData.length; i++){
            for(int j = 0; j<dataForClassifier.length; j++){
                if(NotRepeatedData[i].equals(dataForClassifier[j])){
                    sum[i]+=1;
                }
            }
        }
        findMode(sum,NotRepeatedData);
    }

    public void findMode(int[] sum, String[] NotRepeatedData) {
        String mode;
        for (int pass = 1; pass < sum.length; pass++) {
            for (int i = 0; i < sum.length - 1; i++) {
                if (sum[i] < sum[i + 1]) {
                    int temp = sum[i];
                    String temp1 = NotRepeatedData[i];
                    sum[i] = sum[i + 1];
                    NotRepeatedData[i] = NotRepeatedData[i + 1];
                    sum[i + 1] = temp;
                    NotRepeatedData[i + 1] = temp1;
                }
            }
        }
        mode = NotRepeatedData[0];
        System.out.println("Mode : " + mode);
        System.out.println("Frequency of " + mode + " is " + sum[0] + " times.\n");

        writeInFile(mode);
    }


    //---------------------------------------- write predicted value in file ---------------------------------------------
    public void writeInFile(String input) {
        try {
            Scanner sn = new Scanner(new FileInputStream(filename));
            String[][] fill = new String[row][column];
            int i = 0;

            //scan all lines and split save in array
            while (sn.hasNextLine()) {
                String[] line = sn.nextLine().split(",");
                for (int j = 0; j < line.length; j++)
                    fill[i][j] = line[j];
                i++;
            }

            fill[row_predicted_value - 1][column_predicted_value - 1] = input;
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream("KNN prediction.csv"));
                for (int k = 0; k < row; k++) {
                    for (int j = 0; j < column - 1; j++) {
                        pw.print(fill[k][j] + ",");
                    }
                    pw.println();
                }
                pw.close();
            }
            catch (IOException e) {
                System.out.println("Input error");
            }
            sn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        System.out.println("File saved in KNN prediction!");
    }

}
