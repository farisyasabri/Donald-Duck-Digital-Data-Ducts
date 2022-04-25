package testcookies;

import java.io.*;
import java.util.Scanner;

public class SaveDataFrame {
    private String filename;
    private int countRow,countColumn;

    public SaveDataFrame() {
        filename="";
        countRow=0;
        countColumn=0;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setCountRow(int countRow) {
        this.countRow = countRow;
    }

    public void setCountColumn(int countColumn) {
        this.countColumn = countColumn;
    }

    public int getCountRow() {
        return countRow;
    }

    public int getCountColumn() {
        return countColumn;
    }

    public  void editFile(String filename){
        Scanner s = new Scanner(System.in);
        //to know the limit for the array (since we use normal array)
        System.out.println("Enter number of row and column:");
        System.out.print("Row:");
        int row = s.nextInt();
        System.out.print("Column:");
        int column = s.nextInt();

         createFile(filename,row,column); //create file
    }

    public void createFile(String filename,int row, int column){

        Scanner s = new Scanner(System.in);
        //File file = new File(filename);
        try{
            //FileWriter output = new FileWriter(file);
            PrintWriter outData = new PrintWriter(new FileOutputStream(filename, true));
            PrintWriter outHeader = new PrintWriter(new FileOutputStream(filename,true));

            int numOfRow=0, numOfColumn=0;

            String[]  header = addHeader(column);
            for (int i=0; i<column;i++){
                outHeader.write(header[i]);
                outHeader.write(",");
                numOfColumn++;
            }
            numOfRow++;
            outHeader.println();
            outHeader.close();

            String[][] data = addData(row,column);
            for (int i=0;i<row;i++){
                for (int j=0;j<column;j++){
                    outData.write(data[i][j]);
                    //numOfColumn++;
                    outData.write(",");
                }
                numOfRow++;
                outData.println();
                setCountRow(numOfRow);
                setCountColumn(numOfColumn);
            }outData.close();
        } catch (IOException e) {
            System.out.println("problem with output file");
            e.printStackTrace();
        }
        System.out.print("do you want to read the file? ");
        String answer = s.next();

        //ask user if they want to read and display the file
        if (answer.equalsIgnoreCase("yes"))
//            readfile(filename,row,column);
            readfile(filename);
        else
            System.out.println("end");
    }

    //method to add header for data
    public static String[] addHeader (int column){
        Scanner s = new Scanner(System.in);
        String[] header = new String[column];
        for (int i=0; i<column;i++){
            System.out.print("header column " + (i+1) + " : ");
            header[i] = s.nextLine();
        }
        return header;
    }

    //method to add data in the csv file
    public static String[][] addData (int row, int column){
        Scanner s = new Scanner(System.in);

        String[][] data = new String[row][column];

        for (int i=0;i<row;i++) {
            for (int j=0;j<column;j++){
                System.out.print("row " + (i+1) + " column " + (j+1)  +" :");
                data[i][j]= s.nextLine();
            }
        }
        return data;
    }

    public void readfile(String filename){
        try {
            int numOfColumn=0, numOfRow=0;
            Scanner input = new Scanner(new FileInputStream(filename));
            while(input.hasNextLine())
            {
//                if (input.nextLine().equals(","))
//                    numOfColumn++;
                System.out.println(input.nextLine());
            }
            System.out.println("number of column: "+ getCountColumn());
            System.out.println("number of row: "+ getCountRow());
            input.close();
        }catch(FileNotFoundException e)
        {System.out.println("File not found.");}
    }
}
