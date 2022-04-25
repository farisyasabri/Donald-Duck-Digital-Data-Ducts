package cookiesproject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class kNnORI {

    private int numOfColumnToRefer,blankRow ,blankColumn,kValue;
    private int[] columnToRefer;
    private String filename;
    private double[][] saveLoadData;
//    private int[] cc = new int[2]; // column and row
    private double[] distance;
    private double[] copyDistance;
    private double[] dataForRegressor;
    private int[] getIndex;
    private String[] dataForClassifier;
    private int numOfRow, numOfColumn;

    public void mainkNNORI() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter filename : ");
        filename = s.nextLine()+".csv";
//        cc = countColumnAndRow();
        countColumnAndRow();
        System.out.print("Enter number of column to use : ");
        numOfColumnToRefer = s.nextInt();    // number of column to use to find the regressor
        columnToRefer = new int[numOfColumnToRefer];  // column number to use to find the regressor (dimension)
        for (int i = 0; i< numOfColumnToRefer; i++){
            System.out.print("Enter column number : ");
            columnToRefer[i] = s.nextInt()-1;
        }
        System.out.println("Enter row and column that have blank space:");
        System.out.print("row [include header] : ");
        blankRow = s.nextInt();      //row that has blank space
        System.out.print("column : ");
        blankColumn = s.nextInt();      // column that has blank space

        saveData();  // call method to load data from column that user choose
        EuclideanDistance();  // call method to find the distance from row that has blank space to each row
        System.out.print("Enter k value: ");
        kValue = s.nextInt();
        getIndexForKnn();     // call method to arrange distance in ascending order
        arrangeIndex();     // call method to arrange index that will use later in scan column that has blank space
        System.out.println("Which method to use to predict the value?");
        System.out.println("1 : k-nn regressor ");
        System.out.println("2 : k-nn classifier ");
        int choose = s.nextInt();
        switch (choose){
            case 1 : scanOneColumnRegressor();    // scan column that has blank space and store value that has the same value with getIndex array
                     countRegressor();   // method to count the regressor with k value
                     break;
            case 2 : scanOneColumnClassifier();
                     countClassifier();
                     break;
        }
    }


    //method to get number of column and row
    public void countColumnAndRow(){
//        int[] count=new int[2];
        int countRow=1;
        try{
            Scanner out = new Scanner(new FileInputStream(filename));
            String[] col=out.nextLine().split(",");
            while (out.hasNextLine()){
                out.nextLine();
                countRow++;
            }
            numOfColumn=col.length;    //store number of column
            numOfRow=countRow;      //store number of row
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
//        return count;
    }

    public void saveData(){

        // save the data in column that user has choose in 2D array
//        int row =
//        double[][] saveLoadData = new double[numColumnUse][cc[1]-1];    // index length = column | num of row  -minus 1 bsc user start count from 1
        double[][] saveLoadData = new double[numOfColumnToRefer][numOfRow-1];
        int count1 =0;
        int i=0;
        try{
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                if (count1==0)      // to skip first row that contain string - cant convert to double
                    file.nextLine();

                String[] col = file.nextLine().split(",");
                for (int j = 0; j< numOfColumnToRefer; j++) {
                    saveLoadData[j][i]=Double.parseDouble(col[columnToRefer[j]]);  //convert each row to double value and store it in array
//                    saveLoadData[i][j]=Double.parseDouble(col[columnToRefer[j]]);
                }
                i++;
                count1++;
            }
            setSaveLoadData(saveLoadData);
            file.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        displaySaveLoadData();      //call method to display the stored value
    }

    public void setSaveLoadData(double[][] saveLoadData) {
        this.saveLoadData = saveLoadData;
    }

    //method to display stored value
    public void displaySaveLoadData(){
        for (int i = 0; i< numOfRow-1; i++) {
            System.out.print("column "+ (i+1) +" : ");
//            for (int j=0;j<cc[1]-1;j++) {       //cc[1]-1 : user start count column from 1
            for (int j=0;j<numOfColumnToRefer;j++) {
                System.out.print(saveLoadData[j][i]+ "   ");        // display first column that second column
//                System.out.print(saveLoadData[j][i]+ "   ");
            }
            System.out.println();
        }
        System.out.println();
    }


    // find the distance
    public void EuclideanDistance(){
//        distance = new double[cc[1]-1];
        distance = new double[numOfRow-1];

//        for (int j = 0; j < cc[1] - 1; j++){

        for (int j = 0; j < numOfRow - 1; j++){
            double totalSum=0;
            for (int i = 0; i< numOfColumnToRefer; i++){
                totalSum += Math.pow((saveLoadData[i][blankRow-2])-(saveLoadData[i][j]),2);
            }
            distance[j]=Math.sqrt(totalSum);
        }
        copyDistance = distance.clone();
        displayDistance();
        arrangeDistance();
    }

    // display the distance
    public void displayDistance(){
        System.out.print("distance: ");
//        for (int i=0;i<cc[1]-1;i++){
        for (int i=0;i<numOfRow-1;i++){
            System.out.printf("%.2f   ",distance[i]);
        }
        System.out.println();
    }

    //arrange distance in ascending order
    //purpose : to get the k-nearest value
    public void arrangeDistance(){
        double temp;

        for(int pass=0; pass<numOfRow-3; pass++) {
            for (int i=0 ;i<numOfRow-3-pass; i++){
                if (distance[i]>distance[i+1]) {
                    temp = distance[i];
                    distance[i]=distance[i+1];
                    distance[i+1]=temp;
                }
            }
        }
        displayArrangeDistance();       // call method to display the arranged distance
    }

    public void displayArrangeDistance(){
        System.out.print("sorted distance: ");
        for (int i=0;i<numOfRow-1;i++){
            System.out.printf("%.2f   ",distance[i]);
        }
        System.out.println();
    }

    // method to compare the value arranged distance and the original distance : to get the index for row
    public void getIndexForKnn(){
        getIndex = new int[kValue];
        for (int i=0;i<kValue;i++){
            for (int j=0;j<distance.length;j++){
                if (distance[i]==copyDistance[j]){
                    getIndex[i]=j;      // save the index in array
                }
            }
        }
        displayGetIndex(getIndex);      // call method to display the index for row | p/s: will be use to get the value for k-nearest distance
    }

    // method to display the index
    public void displayGetIndex(int[] index){
        System.out.println("index: ");
        for (int i=0;i<kValue;i++){
            System.out.print(index[i]+"  ");
        }
        System.out.println();
    }

    //method to arrange the index in ascending order
    //purpose: to be use in scanning column and get the value for the row if the index equal to the index of the row
    public void arrangeIndex(){
        for(int pass=0; pass<kValue-1; pass++) {
            for (int i=0;i<kValue-pass-1;i++){
                if (getIndex[i]>getIndex[i+1]){
                    int temp = getIndex[i];
                    getIndex[i]=getIndex[i+1];
                    getIndex[i+1]=temp;
                }
            }
        }
        displayArrangeIndex();      // call method to display the arranged index
    }

    // method to display the index that has been arranged
    public void displayArrangeIndex(){
        System.out.print("arranged index: ");
        for (int i=0;i<kValue;i++){
            System.out.print(getIndex[i]);
        }
        System.out.println();
    }

    // method to scan column that has the blank space
    public void scanOneColumnRegressor(){
        dataForRegressor = new double[kValue];    // index length = num of row
        int forindex=0; // iteration for index getIndex array
        int count1 =0;
        int i=0;
        try{
            Scanner file2 = new Scanner(new FileInputStream(filename));
            while (file2.hasNextLine()) {
                if (count1==0)  // skip the first row
                    file2.nextLine();
                if (getIndex[forindex]==i){     //if iteration value in getIndex equal to i
                    String[] col = file2.nextLine().split(",");
                    dataForRegressor[forindex]=Double.parseDouble(col[blankColumn-1]);  // save the value in array
                    forindex++; // increment index in getIndex array                    // p/s: the value will be use for regressor calculation
                }
                else
                    file2.nextLine();   //else skip row
                if (forindex==kValue)
                    break;
                i++;
                count1++;

            }
            file2.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        displayDataForRegressor();      // call method to display the data that will use for regressor calculation
    }

    // method to display all value that will be use for regressor calculation
    public void displayDataForRegressor(){
        for (int i=0;i<kValue;i++){
//        for (String data : dataForClassifier){
            System.out.print(dataForRegressor[i]+ "   ");
//            System.out.print(data);
        }
    }

    // method to calculate the regressor
    public void countRegressor(){
        double sum=0;
        double regressor;
        for (int i=0;i<kValue;i++){
            sum+=dataForRegressor[i];       // sum all the value in the array
        }
        regressor=sum/kValue;
        System.out.printf("%d-nn regressor = %.2f",kValue, regressor);  //display the predict value
    }

    public void scanOneColumnClassifier(){
//        dataForClassifier = new String[kValue];    // index length = num of row
//        int forindex=0; // iteration for index getIndex array
//        int count1 =0;
//        int i=0;
//        try{
//            Scanner file2 = new Scanner(new FileInputStream(filename));
//            while (file2.hasNextLine()) {
//                if (count1==0)  // skip the first row
//                    file2.nextLine();
//                if (getIndex[forindex]==i){     //if iteration value in getIndex equal to i
//                    String[] col = file2.nextLine().split(",");
//                    dataForClassifier[forindex]=col[blankColumn-1];  // save the value in array
//                    forindex++; // increment index in getIndex array                    // p/s: the value will be use for regressor calculation
//                }
//                else
//                    file2.nextLine();   //else skip row
//                if (forindex==kValue)
//                    break;
//                i++;
//                count1++;
//            }
//            file2.close();
//        }catch (FileNotFoundException e){
//            System.out.println("File not found");
//        }

        dataForClassifier = new String[kValue];    // index length = num of row
        int i = 0, counterRow = 0;
        boolean status = false;
        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                for (int x = 0; x < kValue; x++) {
                    status = counterRow == getIndex[x];  //Index
                    if (status) {
                        break;
                    }
                }
                if (counterRow == 0) {
                    file.nextLine();
                } else if (status) {
                    String[] str = file.nextLine().split(",");
                    dataForClassifier[i] = str[blankColumn - 1];
                    i++;
                } else {
                    file.nextLine();
                }
                counterRow++;
                if (i == kValue) {
                    break;
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        displayDataForClassifier();
        displayDataForClassifier();      // call method to display the data that will use for regressor calculation
    }

    // method to display all value that will be use for regressor calculation
    public void displayDataForClassifier(){
        System.out.println("display data classifier: ");
        for (int i=0;i<kValue;i++){
            System.out.print(dataForClassifier[i]+ "   ");
        }
    }

    public void countClassifier(){
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.println("How many variable?" );
        int numOfVariable = s.nextInt();

        String[] variable=new String[numOfVariable];
        System.out.println("Enter variable: ");
        for (int i=0;i<numOfVariable; i++){
            System.out.print("variable " + (i+1)+ " ");
            variable[i] = s.next();
        }

        int[] countMod =new int[numOfVariable];
        String mod="";
        for (int i=0;i<numOfVariable;i++){
//            int count=0;
            for (int k=0; k<kValue;k++){
                if (variable[i].equalsIgnoreCase(dataForClassifier[k])){
                    countMod[i]++;
                }
            }

            for (int pass=0;pass<numOfVariable;pass++){
                for (int j=0;j<numOfVariable-pass-1;j++) {
                    if (countMod[j] > countMod[j+1]) {
                        mod = variable[j];
                    }
                }
            }
        }

        System.out.println("mod: " + mod);
    }
}
