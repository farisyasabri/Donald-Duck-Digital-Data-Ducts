package cookiesproject;

import java.io.*;
import java.util.Scanner;

public class masyi {
    private int row, column, column_predicted_value, row_predicted_value, numOfColumnToRefer, k, amountofDataUsed;
    private int[] columnToRefer;
    private String filename;
    private double[][] saveData;
    private double[] distance, copyDistance, sortedDistance;
    private double[] dataForRegressor;
    private String[] dataForClassifier;
    private int[] Index;
    protected String mod;

    public void mainKNN() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter filename");
        filename = s.nextLine() + ".csv";
        System.out.print("Enter which column do you want to get predicted value : ");
        column_predicted_value = s.nextInt();
        System.out.print("Enter which row of the predicted value (this include the header) : ");
        row_predicted_value = s.nextInt();
        System.out.print("How many column do you want to refer ? ");
        numOfColumnToRefer = s.nextInt();
        System.out.println("State the index for the column");
        columnToRefer = new int[numOfColumnToRefer];
        for (int i = 0; i < numOfColumnToRefer; i++) {
            System.out.print("Column " + (i + 1) + " : ");
            columnToRefer[i] = s.nextInt() - 1;
        }
        System.out.println("Enter k value : ");
        k = s.nextInt();
        row = CountRow();
        column = CountColumn();
        amountofDataUsed = (row - 1) * 7 / 10;
        System.out.println("This file has " + row + " row and " + column + " column");
        System.out.println("From " + (row - 1) + " lines of data, " + " we only used " + amountofDataUsed + " lines of data for knn");
        saveData();
        EuclideanDistance();
        System.out.println("Which method to use to predict the value?");
        System.out.println("1 : k-nn regressor ");
        System.out.println("2 : k-nn classifier ");
        int choose = s.nextInt();
        switch (choose) {
            case 1 -> getdataForRegressor();    // scan column that has blank space and store value that has the same value with getIndex array
            case 2 -> getdataForClassifier();
        }
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
        double[][] saveData = new double[row - 1][numOfColumnToRefer];
        int counterRow = 0; //row utk savedata
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                if (counterRow == 0) {
                    file.nextLine(); //skip header
                }
                String[] data = file.nextLine().split(",");
                for (int j = 0; j < numOfColumnToRefer; j++) { //utk column
                    saveData[counterRow][j] = Double.parseDouble(data[columnToRefer[j]]); //convert each row to double value and store it in array
                }
                counterRow++;
            }
            file.close();
            setSaveData(saveData);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void setSaveData(double[][] saveData) {
        this.saveData = saveData;
    }

    public void EuclideanDistance() {
        double[] distance = new double[amountofDataUsed]; //70% of total data is used
        System.out.println("Distance :-");
        for (int i = 0; i < amountofDataUsed; i++) { //ex = 7
            double totalSum = 0;
            for (int j = 0; j < numOfColumnToRefer; j++) {
                totalSum += Math.pow((saveData[row_predicted_value - 2][j]) - (saveData[i][j]), 2);
//                totalSum += Math.pow((saveData[j][row_predicted_value - 2]) - (saveData[j][i]), 2);
            }
            distance[i] = Math.sqrt(totalSum);
            System.out.printf("%.2f  ", distance[i]);
        }
        System.out.println();

        setDistance(distance);
        arrangeKnnDistance();
    }

    public void setDistance(double[] distance) {
        this.distance = distance;
    }

    public void arrangeKnnDistance() {
        double temp;

        if (row_predicted_value<amountofDataUsed) {
//            System.out.println("row predicted value"+ distance[row_predicted_value-2]);
            temp = distance[row_predicted_value-2];
            distance[row_predicted_value-2]=distance[amountofDataUsed-1];
            distance[amountofDataUsed-1]=temp;
//            System.out.println("row predicted value 2 : "+ distance[row_predicted_value-2]);
//            System.out.println("row amount of data used " +distance[amountofDataUsed-1]);
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

        displayArrangedKnnDistance();
    }


    public void displayArrangedKnnDistance() {
        System.out.println("sorted distance:- ");
        for (int i = 0; i < amountofDataUsed; i++) {
            System.out.printf("%.2f   ", distance[i]);
        }
        System.out.println();
        getIndexForKnn();
    }


    public void getIndexForKnn() {
        Index = new int[k+1]; //k+1
        int i = 0; //index = 0;
        if (row_predicted_value<amountofDataUsed){
            while(i <k) {
                for (int j = 0; j < distance.length; j++) {
                    if (distance[i] == copyDistance[j]) {
                        Index[i] = j + 1;
                        i++;
                   }
                 }
            }
        }
        else{
            Index = new int[k];
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

        System.out.println("print index");
        for (int p=0;p<k;p++){
            System.out.println(Index[p]);
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
    }

    public void getdataForRegressor() {
        dataForRegressor = new double[k];    // index length = num of row
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
                    dataForRegressor[i] = Double.parseDouble(str[column_predicted_value - 1]);
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
        double regressor, sum = 0; //to calculate sum for kNN
        for (int i = 0; i < k; i++) {
            sum += dataForRegressor[i];
        }
        regressor = sum / k; // regressor = mean
        System.out.printf("%d-nn regressor = %.2f\n", k, regressor);

    }
//
//    public void calculateRegressor_30() {
//        double[] predictedValue = new double[row-amountofDataUsed];
//        for (int j=0;j<row-amountofDataUsed;j++) {
//            double regressor, sum = 0; //to calculate sum for kNN
//            for (int i = 0; i < k; i++) {
//                sum += dataForRegressor[i];
//            }
//            regressor = sum / k; // regressor = mean
//            predictedValue[j] = regressor;
////            System.out.printf("%d-nn regressor = %.2f\n", k, regressor);
//        }
//    }

    public void getdataForClassifier() {
        dataForClassifier = new String[k];    // index length = num of row
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
        countClassifier();
    }

    public void displayDataForClassifier() {
        System.out.print(k + " nearest neighbours are ");
        for (int i = 0; i < k; i++) {
            System.out.print(dataForClassifier[i] + "\t");
        }
        System.out.println();
    }

    public void countClassifier() {
//        System.out.println();
//        Scanner s = new Scanner(System.in);
//        System.out.println("How many variable?");
//        int numOfVariable = s.nextInt();
//
//        String[] variable = new String[numOfVariable];
//        System.out.println("Enter variable: ");
//        for (int i = 0; i < numOfVariable; i++) {
//            System.out.print("variable " + (i + 1) + " ");
//            variable[i] = s.next();
//        }
//
//        int[] countMod = new int[numOfVariable];
//        mod = "";
//        for (int i = 0; i < numOfVariable; i++) {
////            int count=0;
//            for (int j = 0; j < k; j++) {
//                if (variable[i].equalsIgnoreCase(dataForClassifier[j])) {
//                    countMod[i]++;
//                }
//            }
//
//            for (int pass = 0; pass < numOfVariable; pass++) {
//                for (int j = 0; j < numOfVariable - pass - 1; j++) {
//                    if (countMod[j] > countMod[j + 1]) {
//                        this.mod = variable[j];
//                    }
//                }
//            }
//        }

//        int mod = 0, maxCount = 0, i, j;

//        for (i = 0; i < row; ++i) {
//
//            int count = 0;
//            for (j = 0; j < row; ++j) {
//                if (dataForClassifier[j] == dataForClassifier[i])
//                    ++count;
//            }
//            if (count > maxCount) {
//                maxCount = count;
//                mod = (int)dataForClassifier[i];
//            }
//        }
//
//        System.out.println("mod: " + mod);
//        writeInFile(mod);


        String mode = null;
        int maxCount = 0, h, j;

        for (h = 0; h < k; ++h) {

            int count = 0;
            for (j = 0; j < k; ++j) {
                if (dataForClassifier[j].equals(dataForClassifier[h]))
                    ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                mode = dataForClassifier[h];
            }
        }
        System.out.println("mod: " + mod);

    }

//    public void writeInFile(String input){
//        int counterRow=0;
//
//        int i =0;
//        try{
//            Scanner f1 = new Scanner(new FileInputStream(filename));
//            while(f1.hasNextLine()){
//                String[] splitRow = f1.nextLine().split(",");
//                for (int j=0;j<splitRow.length;j++)
//                    if (i==row_predicted_value-2&&j==column_predicted_value-2){
//                        System.out.println("masuk2");
//                        try{
//                            System.out.print("masuk3");
//                            PrintWriter pw=new PrintWriter(new FileOutputStream(filename,true));      // create new file
//                            pw.print(f1.next()+","+input);
//                            pw.close();
//                            break;
//                        }
//                        catch(IOException e){
//                            System.out.println("error input");
//                        }//f1.nextLine();
//                    }
//                i++;
//                System.out.println("masuk");
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
}
