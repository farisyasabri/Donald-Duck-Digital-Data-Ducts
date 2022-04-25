package cookiesproject;

import java.io.*;
import java.util.Scanner;

public class kNearestNeighbours {

    private String filename;
    private int x,y,emptyRow,k;
    private double[] saveDataX;
    private double[] saveDataY;
    private double[] saveEmptyData;
    private double[] distance;

    public void main(){
        Scanner s = new Scanner(System.in);
        System.out.print("File name: ");
        filename = s.nextLine()+".csv";
        setFilename(filename);
        axis();
        System.out.print("Enter empty row: ");
        emptyRow = s.nextInt();
        setEmptyRow(emptyRow);
        scanEmptyRow();
        EuclideanDistance();
        System.out.print("Enter k: ");
        k =s.nextInt();
        calcRegressor();
    }

    public void setSaveDataX(double[] saveDataX) {
        this.saveDataX = saveDataX;
    }

    public void setSaveDataY(double[] saveDataY) {
        this.saveDataY = saveDataY;
    }

    public void setSaveEmptyData(double[] saveEmptyData) {
        this.saveEmptyData = saveEmptyData;
    }

    public double[] getSaveDataX() {
        return saveDataX;
    }

    public double[] getSaveDataY() {
        return saveDataY;
    }

    public double[] getSaveEmptyData() {
        return saveEmptyData;
    }

    public double[] getDistance() {
        return distance;
    }

    public void axis(){
        Scanner s = new Scanner(System.in);
        System.out.print("Enter column for x-axis [column must not have empty space]: ");
        x = s.nextInt();
        setX(x);
        System.out.print("Enter column for y-axis [column must not have empty space]: ");
        y = s.nextInt();
        setY(y);
        scanX();
        scanY();
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setEmptyRow(int emptyRow) {
        this.emptyRow = emptyRow;
    }

    public void setDistance(double[] distance) {
        this.distance = distance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void scanX(){
        int[] count = countColumnAndRow();
        double[] saveDataX = new double[count[1]-1];    // index length = num of row
        int count1 =0;
        int i=0;
        String filename = getFilename();
        try{
            Scanner file = new Scanner(new FileInputStream(filename));
            while (file.hasNextLine()) {
                if (count1==0)
                    file.nextLine();

                String[] col = file.nextLine().split(",");
                saveDataX[i]=Double.parseDouble(col[x-1]);  //x-1
                i++;
                count1++;
            }
            setSaveDataX(saveDataX);
            file.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        displayX();
    }

    public void displayX(){
        int[] count = countColumnAndRow();
        for (int i=0;i<count[1]-1;i++) {    //i less than row
            System.out.print(saveDataX[i]+ "   ");
        }
        System.out.println();
    }

    public void scanY(){
        int[] count = countColumnAndRow();
        double[] saveDataY = new double[count[1]-1];    // index length = num of row
        int count1 =0;
        int i=0;
        String filename2 = getFilename();
        try{
            Scanner file2 = new Scanner(new FileInputStream(filename2));
            while (file2.hasNextLine()) {
                if (count1==0)
                    file2.nextLine();

                String[] col = file2.nextLine().split(",");
                saveDataY[i]=Double.parseDouble(col[y-1]);  //y-1
                i++;
                count1++;
            }
            setSaveDataY(saveDataY);
            file2.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        displayY();
    }

    public void displayY(){
        int[] count = countColumnAndRow();
        for (int i=0;i<count[1]-1;i++) {   //i less than row
            System.out.print(saveDataY[i]+ "   ");
        }
        System.out.println();
    }

    public void scanEmptyRow(){
        //int[] count = countColumnAndRow();
        double[] saveEmptyData = new double[2];   //[count[0]-1];    // count[0]= index length = num of column
        int count1 = 0;
        int x1 = getX();
        int y1 = getY();
        String filename2 = getFilename();
        try{
            Scanner file = new Scanner(new FileInputStream(filename2));
            while (file.hasNextLine()) {
                if (count1!=emptyRow-1)
                    file.nextLine();
                if (count1==emptyRow-1) {
                    String[] col = file.nextLine().split(",");
                    saveEmptyData[0] = Double.parseDouble(col[x1-1]);  //x-1
                    saveEmptyData[1] = Double.parseDouble(col[y1-1]);  //y-1
                    break;
                }
                count1++;
            }
            file.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        setSaveEmptyData(saveEmptyData);
        displayEmptyRow();
    }

    public void displayEmptyRow(){
        for (int i=0;i<2;i++) {   //less than 2 bcs 1 for x and 1 for y
            System.out.print(saveEmptyData[i] + "   ");
        }
        System.out.println();
    }

    public int[] countColumnAndRow(){
        int[] count=new int[2];
        int countRow=1;
        try{
            Scanner out = new Scanner(new FileInputStream(filename));
            String[] col=out.nextLine().split(",");  // how know?
            while (out.hasNextLine()){
                out.nextLine();
                countRow++;
            }
            count[0]=col.length;
            count[1]=countRow;
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return count;
    }

    public void EuclideanDistance(){
        double[] dataX = getSaveDataX();
        double[] dataY = getSaveDataY();
        double[] dataEmptyRow = getSaveEmptyData();
        int[] count = countColumnAndRow();
        double[] distance = new double[count[1]];
        for (int i=0;i<count[1]-1;i++){
            if (i==emptyRow)
                continue;
            double totalx = Math.pow(dataEmptyRow[0]-dataX[i],2);
            double totaly = Math.pow(dataEmptyRow[1]-dataY[i],2);
            distance[i] = Math.sqrt(totalx-totaly);
        }
        setDistance(distance);
        displayDistance(count[1]-1);
    }

    public void displayDistance(int row){
        double[] distanceObj =  getDistance();
        System.out.println("distance: ");
        for (int i=0;i<row; i++){
            if (i==emptyRow)
                continue;
            System.out.printf("%.3f   ",distanceObj[i]);
        }
        System.out.println();
    }

    public void calcRegressor(){
        int[] count = countColumnAndRow();
        //find k smallest value from the blank data set
        double[] smallest = new double[k];
        for (int i=0;i<count[1]-1;i++){

        }
    }

}
