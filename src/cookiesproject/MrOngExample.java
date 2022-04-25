package cookiesproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class MrOngExample {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Enter name of book");
        String[] book_name = new String[3];
        for (int i=0;i<book_name.length;i++) {
            book_name[i] = s.nextLine();
        }

        System.out.println("number of pages");
        int[] pages = new int[3];
        for (int i=0;i< pages.length;i++){
            pages[i] = s.nextInt();
        }
        //I suggest you dont let the user to pick extension by them.
        System.out.println("Enter file name :");
        String filepath = s.next();

        saverecord(book_name,pages,filepath);
    }

    public static void saverecord(String[] book_name,int[]pages,String filepath){

        try{
            //append the file extension
            filepath = filepath + ".csv";
            File file = new File(filepath);
            //check if file exists
            //if it is not, add the header to the new file created
            if(!file.exists()) {
                FileWriter fw = new FileWriter(filepath);
                BufferedWriter bw = new BufferedWriter(fw); // try to exclude next time
                PrintWriter pw = new PrintWriter(bw);
                pw.println("Book Name, Pages");
                pw.flush();
                pw.close();
            }

            FileWriter fw = new FileWriter(filepath,true);
            BufferedWriter bw = new BufferedWriter(fw); // try to exclude next time
            PrintWriter pw = new PrintWriter(bw);

            for (int i=0;i<book_name.length;i++){
                //book name and page are both considered as 1 record.
                //They should be in the same row
                pw.print(book_name[i] +",");
                pw.print(pages[i]);
                pw.println();
            }
            pw.flush();
            pw.close();
            System.out.println("file saved");

        }
        catch (Exception e){
            System.out.println("file not saved");
        }

    }
}
