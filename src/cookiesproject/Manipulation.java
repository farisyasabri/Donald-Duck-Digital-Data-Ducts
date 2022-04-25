package cookiesproject;

import java.io.*;
import java.util.Scanner;

public class Manipulation {

//    public static void askQuestion(){
//        Part1And2 b = new Part1And2();
//        Scanner s = new Scanner(System.in);
//        System.out.print("Do you want to continue the process?: ");
//        String answer = s.next();
//        if (answer.equalsIgnoreCase("yes"))
//            b.chooseStep();
//        else
//            System.out.println("Process Ended");
//    }

    public int[] countColumnAndRow(String filename){
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

    public void ColumnConcatenation(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Enter another existing file name for stacking columns : ");
        String filename2 = s.nextLine()+".csv";
        int[] count1 = countColumnAndRow(filename);
        int[] count2 = countColumnAndRow(filename2);
        System.out.println("Column - "+count1[0]+" and "+count2[0]);    // to print column for each file
        System.out.println("Row - "+count1[1]+" and "+count2[1]);       // ro print row for each file
        if (count1[1]==count2[1]){      // check if the number of rows for each file is same
            System.out.print("New file name for the of column concatenation result: ");
            String newFilename = s.nextLine()+".csv";
            try{
                Scanner f1 = new Scanner(new FileInputStream(filename));
                Scanner f2 = new Scanner(new FileInputStream(filename2));
                try{        //create new file (result)
                    PrintWriter pw=new PrintWriter(new FileOutputStream(newFilename));      // create new file
                    while(f2.hasNextLine()){
                        pw.println(f1.nextLine()+","+f2.nextLine());  // column file 1 + column file 2
                    }
                    pw.close();
                }catch(IOException e){}
                f1.close();
                f2.close();
            }catch(FileNotFoundException e){
                System.out.println("File was not found");
            }
            try{
                Scanner f1 = new Scanner(new FileInputStream(newFilename));
                try{
                    PrintWriter pw=new PrintWriter(new FileOutputStream(filename));  // overwrite data from new file to old file (file 1)
                    while(f1.hasNextLine()){
                        pw.println(f1.nextLine());
                    }
                    pw.close();
                }catch(IOException e){}
                f1.close();
            }catch(FileNotFoundException e){
                System.out.println("File was not found");
            }
        }
        else
            System.out.println("Unequal number of rows");

        //askQuestion();
    }

    public void RowConcatenation(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Enter another existing file name for stacking rows : ");
        String filename2 = s.nextLine()+".csv";
        int[] count1 = countColumnAndRow(filename);
        int[] count2 = countColumnAndRow(filename2);
        System.out.println("Column - "+count1[0]+" and "+count2[0]);    //print column for each files
        System.out.println("Row - "+count1[1]+" and "+count2[1]);       //print row for each files
        if (count1[0]==count2[0]){      // check if the number of column for each file are same
            try{
                Scanner f2 = new Scanner(new FileInputStream(filename2));
                try{
                    PrintWriter pw=new PrintWriter(new FileOutputStream(filename,true));     //write data from file 2 to file 1
                    f2.nextLine();  // NEXT TIME TRY EXCLUDE NAK TENGOK APA JADI
                    while(f2.hasNextLine()){
                        pw.println();
                        pw.print(f2.nextLine());
                    }
                    pw.close();
                }catch(IOException e){}
                f2.close();
            }catch(FileNotFoundException e){
                System.out.println("File was not found");
            }
        }
        else
            System.out.println("Unequal number of columns");

        //askQuestion();
    }


    public void subsetRow(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name to obtain data from : ");
        String filename = s.nextLine()+".csv";
        System.out.print("New file name for the row Subset: ");
        String newfilename = s.nextLine()+".csv";
        System.out.print("Enter first row(inclusive) : ");
        int row1=s.nextInt();
        System.out.print("Enter last row(exclusive) : ");
        int row2=s.nextInt();


        int count=row1;
        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream(newfilename));
                pw.println(f2.nextLine());
                for(int i=1;i<row1;i++)
                    f2.nextLine();
                while(count<row2){
                    pw.println(f2.nextLine());
                    count++;
                }
                pw.close();
                f2.close();
            }catch(IOException e){
                System.out.println("File output error");
            }
        }catch(FileNotFoundException e){
            System.out.println("File was not found");
        }

        //askQuestion();
    }


    public void subsetColumn(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name to obtain data from : ");
        String filename = s.nextLine()+".csv";
        System.out.print("How many column(s) to be displayed? ");
        int cm=s.nextInt();
        int[] cl=new int[cm];
        for(int i=0;i<cm;i++){
            System.out.print("Column : ");
            cl[i]=s.nextInt()-1;
        }
        System.out.print("New file name for column subset: ");
        String newfilename = s.next()+".csv";   // KENAPA TAK LEH BUAT NEXTLINE???
        System.out.println();
        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream(newfilename));
                while(f2.hasNextLine()){
                    String line=f2.nextLine();
                    String[] col=line.split(",");
                    for(int i=0;i<cm-1;i++){
                        pw.print(col[cl[i]]+",");
                    }
                    pw.print(col[cl[cm-1]]);
                    pw.println();
                }
                pw.close();
                f2.close();
            }catch(IOException e){
                System.out.println("File output error");
            }
        }catch(FileNotFoundException e){
            System.out.println("File was not found");
        }

        //askQuestion();
    }


    public void sortByColumn(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Column to be sorted : ");
        int c=s.nextInt()-1;
        System.out.println("1: Alphabetical order\n2: Numerical order");
        int ans=s.nextInt();
        System.out.print("New filename for the sorted column: ");
        String newfilename = s.next()+".csv";  // KENAPA TAK LEH BUAT NEXT LINE??
        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream(newfilename));
                int[] cr=countColumnAndRow(filename);
                cr[1]--;
                String[][] sort=new String[cr[1]][cr[0]];
                int i=0;
                pw.println(f2.nextLine());
                while(f2.hasNextLine()){
                    String[] spl=f2.nextLine().split(",");
                    for(int j=0;j<cr[0];j++)
                        sort[i][j]=spl[j];
                    i++;
                }
                if(ans==1){
                    for(int m=0;m<cr[1];m++){
                        for(int n=0;n<cr[1]-1;n++){
                            if(sort[n][c].compareTo(sort[n+1][c])>0){
                                String[] temp=sort[n];
                                sort[n]=sort[n+1];
                                sort[n+1]=temp;
                            }
                        }
                    }
                }
                if(ans==2){
                    for(int m=0;m<cr[1];m++){
                        for(int n=0;n<cr[1]-1;n++){
                            if(Double.parseDouble(sort[n][c])>Double.parseDouble(sort[n+1][c])){
                                String[] temp=sort[n];
                                sort[n]=sort[n+1];
                                sort[n+1]=temp;
                            }
                        }
                    }
                }
                for(int b=0;b<cr[1];b++){
                    for(int d=0;d<cr[0]-1;d++)
                        pw.print(sort[b][d]+",");
                    pw.print(sort[b][cr[0]-1]);
                    pw.println();
                }
                pw.close();
                f2.close();
            }catch(IOException e){
                System.out.println("File output error");
            }
        }catch(FileNotFoundException e){
            System.out.println("File was not found");
        }
        try{
            Scanner f1 = new Scanner(new FileInputStream(newfilename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream(filename));
                while(f1.hasNextLine()){
                    pw.println(f1.nextLine());
                }
                pw.close();
            }catch(IOException e){}
            f1.close();
        }catch(FileNotFoundException e){
            System.out.println("File was not found");
        }

        //askQuestion();
    }

    public void duplicate(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("New file name: ");
        String newfilename = s.next()+".csv";  // KENAPA TAKLEH LETAK NEXTLINE??
        System.out.print("Column for the duplicate to be dropped? ");
        int ans=s.nextInt();
        int[] cr=countColumnAndRow(filename);
        int row=cr[1]-1;    //exclude header
        int column=cr[0];
        String[][] lines=new String[row][column];
        String[] rows=new String[row];
        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream(newfilename));
                pw.println(f2.nextLine());
                int i=0;
                while(f2.hasNextLine()){
                    String line=f2.nextLine();
                    rows[i]=line;
                    String[] det=line.split(",");
                    for(int j=0;j<det.length;j++)
                        lines[i][j]=det[j];
                    i++;
                }
                for(int c=0;c<row;c++){
                    for(int d=0;d<c;d++){
                        if(lines[d][ans].equals(lines[c][ans])){
                            if(rows[d]!=null){
                                System.out.println(d+" : "+rows[d]+"\n"+c+" : "+rows[c]);
                                System.out.print("Row to be dropped? ");
                                int drop=s.nextInt();
                                rows[drop]=null;
                            }
                        }
                    }
                }
                for(String a:rows){
                    if(a!=null)
                        pw.println(a);
                }
                pw.close();
                f2.close();
            }catch(IOException e){
                System.out.println("File output error");
            }
        }catch(FileNotFoundException e){
            System.out.println("File was not found");
        }

        //askQuestion();
    }
}
