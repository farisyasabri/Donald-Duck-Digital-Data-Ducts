package cookiesproject;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ErrorM {

    private int row, column, column_predicted_value, row_predicted_value, numOfColumnToRefer, k, amountofDataUsedforCalculation, amountofDataUsedforErrorPrediction, falsePrediction, correctPrediction, truePositive, trueNegative, falsePositive,falseNegative;
    private int[] columnToRefer;
    private String filename;
    private double[][] saveData;
    private double[] predicted_valueR, actual_valueR, distance, copyDistance;
    private double[] dataForRegression;
    private String[] dataForClassification, actual_valueC, predicted_valueC;
    private int[] Index;


    public void mainError() {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter filename");
        filename = s.nextLine() + ".csv";
        System.out.print("Enter which column do you want to get predicted value : ");
        column_predicted_value = s.nextInt();
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
        amountofDataUsedforCalculation = (row - 1) * 7 / 10 ;
        amountofDataUsedforErrorPrediction = row - 1 - amountofDataUsedforCalculation;
        predicted_valueR = new double[amountofDataUsedforErrorPrediction];
        predicted_valueC = new String[amountofDataUsedforErrorPrediction];
        System.out.println("This file has " + row + " row and " + column + " column");
        System.out.println("From " + (row-1) + " lines of data, " + " we only used " + amountofDataUsedforCalculation + " lines of data for calculation");

        System.out.println("Which method to use to predict the value?");
        System.out.println("1 : k-nn regressor ");
        System.out.println("2 : k-nn classifier ");
        int choose = s.nextInt();
        if(choose == 1){
            for(int i = 0; i < amountofDataUsedforErrorPrediction; i++) {
                row_predicted_value = amountofDataUsedforCalculation + 2 + i;
                saveData();
                EuclideanDistance();
                getdataForRegression();
            }
            getActualValueRegression();
            displayPredictedValueR();
            calculateErrorR();
            calculateMAE();
            calculateMSE();
        }
        else{
            for(int i = 0; i < amountofDataUsedforErrorPrediction; i++) {
                row_predicted_value = amountofDataUsedforCalculation + 2 + i;
                saveData();
                EuclideanDistance();
                getdataForClassification();
            }
            getActualValueClassification();
            displayPredictedValueC();
            calculateErrorC();
            calculateAccuracy();
            calculateYesOrNo();
            calculatePrecision();
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

    public int CountColumn(){
        int countColumn = 0;
        try{
            Scanner out = new Scanner(new FileInputStream(filename));

            while (out.hasNextLine()){
                String[] column = out.nextLine().split(",");
                countColumn = column.length + 1;
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return countColumn;
    }

    public void saveData(){
        double[][] saveData = new double[row-1][numOfColumnToRefer];
        int counterRow=0; //row utk savedata
        try{
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                if (counterRow==0) {
                    file.nextLine(); //skip header
                }
                String[] data = file.nextLine().split(",");
                for (int j = 0; j<numOfColumnToRefer; j++) { //utk column
                    saveData[counterRow][j] = Double.parseDouble(data[columnToRefer[j]]); //convert each row to double value and store it in array
                }
                counterRow++;
            }
            file.close();
            setSaveData(saveData);
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    public void setSaveData(double[][] saveData) {
        this.saveData = saveData;
    }

    public void EuclideanDistance(){
        double[] distance = new double[amountofDataUsedforCalculation];
        System.out.println("Distance :-");
        for (int i = 0; i < amountofDataUsedforCalculation ; i++) {
            double totalSum = 0;
            for (int j = 0; j < numOfColumnToRefer; j++) {
                totalSum += Math.pow((saveData[row_predicted_value - 2][j]) - (saveData[i][j]), 2);
            }
            distance[i] = Math.sqrt(totalSum);
            System.out.printf("%.2f  ", distance[i]);
        }
        System.out.println();

        setDistance(distance);
        copyDistance = distance.clone();
        arrangeKnnDistance();
    }
    public void setDistance(double[] distance) {
        this.distance = distance;
    }

    public void arrangeKnnDistance(){
        for(int pass = 1; pass < amountofDataUsedforCalculation; pass++){
            for(int i = 0; i< amountofDataUsedforCalculation-1; i++){
                if(distance[i] > distance[i+1]){
                    double temp = distance[i];
                    distance[i]= distance[i+1];
                    distance[i+1] = temp;
                }
            }
        }
        getIndexForKnn();
    }

    public void getIndexForKnn(){
        Index = new int[k+1];
        int  i= 0;
        while(i < k) {
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
        arrangeIndex();
    }
    public void arrangeIndex(){
        for(int pass = 1; pass < k; pass++){
            for(int i = 0; i< k-1; i++){
                if(Index[i] > Index[i+1]){
                    int temp = Index[i];
                    Index[i]= Index[i+1];
                    Index[i+1] = temp;
                }
            }
        }
        displayGetIndex();      // call method to display the index for row | p/s: will be use to get the value for k-nearest distance

    }

    public void displayGetIndex(){
        System.out.print(k + " nearest index : ");
        for(int i = 0; i<k ; i++){
            System.out.print(Index[i] + "\t");
        }
        System.out.println();
    }

    public void getdataForRegression(){
        dataForRegression = new double[k];    // index length = num of row
        int i=0, counterRow = 0;
        boolean status = false;
        try{
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
                }
                else if (status) {
                    String[] str = file.nextLine().split(",");
                    dataForRegression[i] = Double.parseDouble(str[column_predicted_value - 1]);
                    i++;
                }
                else {
                    file.nextLine();
                }counterRow++;

                if (i == k) {
                    break;
                }
            }
            file.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        displayDataForRegression();
    }

    public void displayDataForRegression(){
        System.out.print(k + " nearest neighbours are ");
        for (int i=0;i<k;i++){
            System.out.print(dataForRegression[i]+ "\t");
        }
        System.out.println();
        calculateRegression();
    }

    public void calculateRegression(){
        double sum = 0; //to calculate sum for kNN
        int j = 0;
        for (int i=0 ; i<k ; i++){
            sum += dataForRegression[i];
        }
        predicted_valueR[row_predicted_value - amountofDataUsedforCalculation-2] = sum/(double)k; // regressor = mean
        System.out.printf("%d-nn regressor = %.2f\n",k, predicted_valueR[row_predicted_value - amountofDataUsedforCalculation-2]);
    }

    public void getdataForClassification(){
        dataForClassification = new String[k];    // index length = num of row
        int i=0, counterRow = 0;
        boolean status = false;
        try{
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
                }
                else if (status) {
                    String[] str = file.nextLine().split(",");
                    dataForClassification[i] = str[column_predicted_value - 1];
                    i++;
                }
                else {
                    file.nextLine();
                }
                counterRow++;
                if (i == k) {
                    break;
                }
            }
            file.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        displayDataForClassification();
    }

    public void displayDataForClassification(){
        System.out.print(k + " nearest neighbours are ");
        for (int i=0;i<k;i++){
            System.out.print(dataForClassification[i]+ "\t");
        }
        System.out.println();
        CountHowManyString();
    }

    public void CountHowManyString(){
        int i, m=1, sum = 0;
        String[] str = new String[k];
        str[0] = dataForClassification[0];
        for(i = 1; i<k ; i++){
            for(int j = i-1; j>=0; j--) {
                if (!dataForClassification[i].equals(dataForClassification[j])) {
                    sum += 0;
                }
                else{
                    sum+=1;
                }
            }
            if(sum==0){  //if sum=0, it means that the data for the current row is different with the rows before
                str[m] = dataForClassification[i];
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
        int sum[] = new int[NotRepeatedData.length];
        for(int i = 0; i<NotRepeatedData.length; i++){
            for(int j = 0; j<dataForClassification.length; j++){
                if(NotRepeatedData[i].equals(dataForClassification[j])){
                    sum[i]+=1;
                }
            }
        }
        findMode(sum,NotRepeatedData);
    }

    public void findMode(int[] sum, String[] NotRepeatedData){
        int highestFrequency = 0;
        String mode;
        for(int pass = 1; pass < sum.length; pass++){
            for(int i = 0; i< sum.length-1; i++){
                if(sum[i] < sum[i+1]){
                    int temp = sum[i];
                    String temp1 = NotRepeatedData[i];
                    sum[i]= sum[i+1];
                    NotRepeatedData[i] = NotRepeatedData[i+1];
                    sum[i+1] = temp;
                    NotRepeatedData[i+1] = temp1;
                }
            }
        }
        mode = NotRepeatedData[0];
        predicted_valueC[row_predicted_value - amountofDataUsedforCalculation-2] = mode;
        System.out.println("Mode : " + mode);
        System.out.println("Frequency of " + mode + " is " + sum[0] + " times.\n");

    }


    public void getActualValueRegression() {
        actual_valueR = new double[amountofDataUsedforErrorPrediction];    // index length = num of row
        int i = 0, counterRow = 1;
        boolean status = false;
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                for (int x = 0; x < amountofDataUsedforErrorPrediction; x++) {
                    row_predicted_value = amountofDataUsedforCalculation + 2 + i;
                    status = counterRow == row_predicted_value;
                    if (status) {
                        break;
                    }
                }
                if (counterRow == 1) {
                    file.nextLine();
                } else if (status) {
                    String[] str = file.nextLine().split(",");
                    actual_valueR[i] = Double.parseDouble(str[column_predicted_value - 1]);
                    i++;
                } else {
                    file.nextLine();
                }
                counterRow++;

                if (i == amountofDataUsedforErrorPrediction) {
                    break;
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        displayActualValueR();
    }
    public void displayActualValueR(){
        System.out.println("Actual Value : ");
        for(int i = 0; i < actual_valueR.length; i++ ){
            System.out.printf("%.2f \t", actual_valueR[i]);
        }
    }

    public void displayPredictedValueR(){
        System.out.println("Predicted value :- ");
        for(int i = 0; i < predicted_valueR.length; i++ ){
            System.out.printf("%.2f \t",predicted_valueR[i]);
        }
        System.out.println();
    }

    public void calculateErrorR(){
        falsePrediction = 0;
        correctPrediction = 0;
        for(int i = 0; i < actual_valueR.length; i++){
            if(actual_valueR[i] != predicted_valueR[i]){
                falsePrediction += 1;
            }
            else{
                correctPrediction += 1;
            }
        }
    }

    public void calculateMAE(){
        double MAE;
        double sum0fDiff = 0;
        for(int i = 0; i < actual_valueR.length; i++){
            if(actual_valueR[i]>predicted_valueR[i]) {
                sum0fDiff += actual_valueR[i] - predicted_valueR[i];
            }
            else{
                sum0fDiff += predicted_valueR[i] - actual_valueR[i];
            }
        }
        MAE = 1.0/amountofDataUsedforErrorPrediction*sum0fDiff;
        System.out.printf("Mean Absolute Error : %.2f" , MAE);
    }

    public void calculateMSE() {
        double MSE;
        double squareOfDiff = 0;
        for(int i = 0; i < actual_valueR.length; i++){
            squareOfDiff += Math.pow(actual_valueR[i] - predicted_valueR[i],2);
        }
        MSE = 1.0/amountofDataUsedforErrorPrediction*squareOfDiff;
        System.out.printf("Mean Square Error : %.2f" , MSE);
    }

    public void getActualValueClassification() {
        actual_valueC = new String[amountofDataUsedforErrorPrediction];    // index length = num of row
        int i = 0, counterRow = 1;
        boolean status = false;
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                for (int x = 0; x < amountofDataUsedforErrorPrediction; x++) {
                    row_predicted_value = amountofDataUsedforCalculation + 2 + i;
                    status = counterRow == row_predicted_value;
                    if (status) {
                        break;
                    }
                }
                if (counterRow == 1) {
                    file.nextLine();
                } else if (status) {
                    String[] str = file.nextLine().split(",");
                    actual_valueC[i] = str[column_predicted_value - 1];
                    i++;
                } else {
                    file.nextLine();
                }
                counterRow++;

                if (i == amountofDataUsedforErrorPrediction) {
                    break;
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        displayActualValueC();
    }
    public void displayActualValueC(){
        System.out.println("Actual Value : ");
        for(int i = 0; i < actual_valueC.length; i++ ){
            System.out.printf("%s \t", actual_valueC[i]);
        }
        System.out.println("\n");
    }

    public void displayPredictedValueC(){
        System.out.println("Predicted value :- ");
        for(int i = 0; i < predicted_valueC.length; i++ ){
            System.out.printf("%s \t", predicted_valueC[i]);
        }
        System.out.println("\n");
    }
    public void calculateErrorC(){
        falsePrediction = 0;
        correctPrediction = 0;
        for(int i = 0; i < actual_valueC.length; i++){
            if(!actual_valueC[i].equals(predicted_valueC[i])){
                falsePrediction += 1;
            }
            else{
                correctPrediction += 1;
            }
        }
    }

    public void calculateYesOrNo(){
        truePositive = 0;
        trueNegative = 0;
        falsePositive = 0;
        falseNegative = 0;
        for(int i = 0; i < actual_valueC.length; i++){
            if(actual_valueC[i].equals("YES")){
                if(predicted_valueC[i].equals("YES")) {
                    truePositive += 1;
                }
                if(predicted_valueC[i].equals("NO")){
                    falsePositive += 1;
                }
            }
            else{
                if(predicted_valueC[i].equals("NO")) {
                    trueNegative += 1;
                }
                if (predicted_valueC[i].equals("YES")){
                    falseNegative += 1;
                }
            }
        }
    }

    public void calculatePrecision(){
        double precisionYes = (double)truePositive/(truePositive + falsePositive)*100;
        double precisionNo = (double)trueNegative/(trueNegative + falseNegative)*100;
        if(truePositive == 0 && trueNegative == 0 && falsePositive == 0 && falseNegative == 0){
            System.out.println("No precision calculated for this type of data");
        }
        else {
            System.out.printf("The precision for YES : %.2f\n", precisionYes);
            System.out.printf("The precision for NO : %.2f\n", precisionNo);
        }

    }
    public void calculateAccuracy(){
        double accuracy = (double)correctPrediction / predicted_valueC.length*100;
        System.out.printf("Accuracy : %.2f\n" , accuracy);
    }

}
