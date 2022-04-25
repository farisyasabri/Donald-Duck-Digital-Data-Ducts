package cookiesproject;

import java.io.*;
import java.util.Scanner;

public class DonaldData {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        //ask user to enter file name (either new or existing file)
        System.out.println("Enter file name");
        String filename = s.nextLine();

        //next step
        System.out.println("next step (enter number)");
        System.out.println("edit file : 1");
        System.out.println("display file : 2");
        System.out.println("merge file : 3");
        int num = s.nextInt();

        chooseStep(num, filename);

        // display working directory at the end of the output
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }

    public static void chooseStep(int num,String filename){
        switch (num){
            case 1 : editFile(filename);
                break;
            case 2 : readfile(filename);
                break;
            case 3 : mergefile(filename);
                break;
            default:
                System.out.println("process ended");
        }
    }

    public static void editFile(String filename){
        Scanner s = new Scanner(System.in);
        //to know the limit for the array (since we use normal array)
        System.out.println("Enter number of row and column:");
        System.out.print("Row:");
        int row = s.nextInt();
        System.out.print("Column:");
        int column = s.nextInt();

        createFile(filename,row,column); //create file

    }

    public static void createFile(String filename,int row, int column){

        Scanner s = new Scanner(System.in);
        //File file = new File(filename);
        try{
            //FileWriter output = new FileWriter(file);
            PrintWriter outData = new PrintWriter(new FileOutputStream(filename, true));
            PrintWriter outHeader = new PrintWriter(new FileOutputStream(filename,true));

            String[]  header = addHeader(column);
            for (int i=0; i<column;i++){
                outHeader.write(header[i]);
                outHeader.write(",");
            }
            outHeader.println();
            outHeader.close();

            String[][] data = addData(row,column);
            for (int i=0;i<row;i++){
                for (int j=0;j<column;j++){
                    outData.write(data[i][j]);
                    outData.write(",");
                }
                outData.println();
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

    public static void readfile(String filename){
        try {
            Scanner input = new Scanner(new FileInputStream(filename));
            while(input.hasNextLine())
            {
                System.out.println(input.nextLine());
            }

            input.close();
        }catch(FileNotFoundException e)
        {System.out.println("File not found.");}
    }

    //method to merge file (NOTE : IT IS NOT SAME AS STACKING ROW)
    //STACKING ROW : BOTH NUM OF COLUMN MUST SAME
    public static void mergefile(String file_name){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the second file name you want to merge :");
        String file_name2 = input.nextLine();

        System.out.println("Enter the new file name :");
        String file_name3 = input.nextLine();
        try {
            //write new file (merged)
            PrintWriter pw = new PrintWriter(file_name3);

            //Buffer original file
            BufferedReader br = new BufferedReader(new FileReader(file_name));

            String line = br.readLine();

            while(line != null)
            {
                pw.println(line);
                line = br.readLine();
            }
            //buffer second file
            br = new BufferedReader(new FileReader(file_name2));
            line = br.readLine();

            while(line != null)
            {
                pw.println(line);
                line = br.readLine();
            }
            pw.flush();
            pw.close();
            br.close();
        }catch(IOException e)
        {System.out.println("Problem with file output");}

        try {
            Scanner in = new Scanner(new FileInputStream(file_name3));
            while(in.hasNextLine())
            {
                System.out.println(in.nextLine());
            }
            in.close();

        }catch(FileNotFoundException e)
        {System.out.println("File not found");}
    }

}