package cookiesproject;

import testcookies.StatisticsAndImputers;

import java.io.*;
import java.util.Scanner;

public class Part1And2 {

    public static void main(String[] args) {
        System.out.println("***WELCOME****");
        chooseStep();
    }

    public static void askQuestion(){
        Part1And2 b = new Part1And2();
        Scanner s = new Scanner(System.in);
        System.out.print("Do you want to continue the process?: ");
        String answer = s.next();
        if (answer.equalsIgnoreCase("yes"))
            b.chooseStep();
        else
            System.out.println("Process Ended");
    }

    public static void chooseStep(){
        //Manipulation a = new Manipulation();
//        kNearestNeighbours a = new kNearestNeighbours();
        kNnORI a = new kNnORI();
        Scalers b = new Scalers();
        masyi c = new masyi();
        ErrorMetric d = new ErrorMetric();
        StatisticsAndImputers im = new StatisticsAndImputers();
        Scanner s = new Scanner(System.in);
        Statistics st = new Statistics();
        System.out.println("1: Edit file");
        System.out.println("2: Display file");
        System.out.println("3: Column concatenation");
        System.out.println("4: Row concatenation");
        System.out.println("5: Subset Row");
        System.out.println("6: Subset Column");
        System.out.println("7: Sort by column");
        System.out.println("8: Drop duplicate rows");
        System.out.println("9: k Nearest Neighbour");
        System.out.print("Enter number : ");
        int num = s.nextInt();
        switch(num){
            case 1 -> editFile();
            case 2 -> readfile();
            case 3 -> ColumnConcatenation();
            case 4 -> RowConcatenation();
            case 5 -> subsetRow();
            case 6 -> subsetColumn();
            case 7 -> sortByColumn();
            case 8 -> duplicate();
            case 9 -> a.mainkNNORI();
            case 10 -> b.mainScalers();
            case 11 -> c.mainKNN();
            case 12 -> dropNull();
            case 13 -> im.statistics();
            case 14 -> d.mainKNN();
            default -> System.out.println("Process Ended");
        }
    }

    public static void editFile(){
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Enter number of row : ");
        int row = s.nextInt();
        System.out.print("Enter number of column : ");
        int column = s.nextInt();

        createFile(filename,row,column);
    }

    public static void createFile(String filename,int row, int column){
        //Manipulation a = new Manipulation();
        try{
            PrintWriter outData = new PrintWriter(new FileOutputStream(filename, true));
            PrintWriter outHeader = new PrintWriter(new FileOutputStream(filename,true));

            String[] header = addHeader(column);
            for (int i=0; i<column-1;i++){
                outHeader.write(header[i]+",");
            }
            outHeader.write(header[column-1]);
            outHeader.println();
            outHeader.close();

            String[][] data = addData(row,column);
            for (int i=0;i<row;i++){
                for (int j=0;j<column-1;j++){
                    outData.write(data[i][j]+",");
                }
                outData.write(data[i][column-1]);
                outData.println();
            }outData.close();
        } catch (IOException e) {
            System.out.println("Problem with output file");
            e.printStackTrace();
        }

        askQuestion();
    }

    //method to create the header for data
    public static String[] addHeader (int column){
        Scanner s = new Scanner(System.in);
        String[] header = new String[column];
        for (int i=0; i<column;i++){
            System.out.print("Header column " + (i+1) + " : ");
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
                System.out.print("Row " + (i+1) + " Column " + (j+1)  +" : ");
                data[i][j]= s.nextLine();
            }
        }
        return data;
    }

    //method to read the file that has been created
    public static void readfile(){
        //Manipulation a = new Manipulation();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        try{
            Scanner sn = new Scanner(new FileInputStream(filename));
            while (sn.hasNextLine()){
                System.out.println(sn.nextLine());
            }
            sn.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        askQuestion();
    }


    public static int[] countColumnAndRow(String filename){
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

    public static void ColumnConcatenation(){
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

        askQuestion();
    }

    public static void RowConcatenation(){
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

        askQuestion();
    }


    public static void subsetRow(){
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

        askQuestion();
    }


    public static void subsetColumn(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name to obtain data from : ");
        String filename = s.nextLine()+".csv";
        System.out.print("How many column(s) to be displayed? ");
        int cm=s.nextInt();
        int[] cl=new int[cm];
        for(int i=0;i<cm;i++){
            System.out.print("Column : ");
            cl[i]=s.nextInt();
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

        askQuestion();
    }


//    public static void sortByColumn(){
//        Scanner s=new Scanner(System.in);
//        System.out.print("Enter a file name : ");
//        String filename = s.nextLine()+".csv";
//        System.out.print("Column to be sorted : ");
//        int c=s.nextInt();
//        System.out.println("1: Alphabetical order\n2: Numerical order");
//        int ans=s.nextInt();
//        System.out.print("New filename for the sorted column: ");
//        String newfilename = s.next()+".csv";  // KENAPA TAK LEH BUAT NEXT LINE??
//        try{
//            Scanner f2 = new Scanner(new FileInputStream(filename));
//            try{
//                PrintWriter pw=new PrintWriter(new FileOutputStream(newfilename));
//                int[] cr=countColumnAndRow(filename);
//                cr[1]--;
//                String[][] sort=new String[cr[1]][cr[0]];
//                int i=0;
//                pw.println(f2.nextLine());
//                while(f2.hasNextLine()){
//                    String[] spl=f2.nextLine().split(",");
//                    for(int j=0;j<cr[0];j++)
//                        sort[i][j]=spl[j];
//                    i++;
//                }
//                if(ans==1){
//                    for(int m=0;m<cr[1];m++){
//                        for(int n=0;n<cr[1]-1;n++){
//                            if(sort[n][c].compareTo(sort[n+1][c])>0){
//                                String[] temp=sort[n];
//                                sort[n]=sort[n+1];
//                                sort[n+1]=temp;
//                            }
//                        }
//                    }
//                }
//                if(ans==2){
//                    for(int m=0;m<cr[1];m++){
//                        for(int n=0;n<cr[1]-1;n++){
//                            if(Double.parseDouble(sort[n][c])>Double.parseDouble(sort[n+1][c])){
//                                String[] temp=sort[n];
//                                sort[n]=sort[n+1];
//                                sort[n+1]=temp;
//                            }
//                        }
//                    }
//                }
//                for(int b=0;b<cr[1];b++){
//                    for(int d=0;d<cr[0]-1;d++)
//                        pw.print(sort[b][d]+",");
//                    pw.print(sort[b][cr[0]-1]);
//                    pw.println();
//                }
//                pw.close();
//                f2.close();
//            }catch(IOException e){
//                System.out.println("File output error");
//            }
//        }catch(FileNotFoundException e){
//            System.out.println("File was not found");
//        }
//        try{
//            Scanner f1 = new Scanner(new FileInputStream(newfilename));
//            try{
//                PrintWriter pw=new PrintWriter(new FileOutputStream(filename));
//                while(f1.hasNextLine()){
//                    pw.println(f1.nextLine());
//                }
//                pw.close();
//            }catch(IOException e){}
//            f1.close();
//        }catch(FileNotFoundException e){
//            System.out.println("File was not found");
//        }
//
//        askQuestion();
//    }

    public static void sortByColumn(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Column to be sorted : ");
        int c=s.nextInt();
        System.out.println("Alphabetical order : 1\nNumerical order : 2");
        int ans=s.nextInt();
        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream("newtest.csv"));
                int[] cr=countColumnAndRow(filename);
                cr[1]--;    //number of row
                String[][] sort=new String[cr[1]][cr[0]]; // row column
                int i=0;
                pw.println(f2.nextLine());
                while(f2.hasNextLine()){
                    String[] spl=f2.nextLine().split(",");
                    for(int j=0;j<cr[0];j++)    //j less than number of column
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
            Scanner f1 = new Scanner(new FileInputStream("newtest.csv"));
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
    }

    public static void duplicate(){
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

        askQuestion();
    }

    public static void dropNull(){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Number of columns for missing data : ");
        int mis=s.nextInt();
        int[] num=new int[mis];
        for(int p=0;p<mis;p++){
            System.out.print("Column? ");
            num[p]=s.nextInt();
        }
        int[] cr=countColumnAndRow(filename);
        int row=cr[1]-1;    //exclude header
        int column=cr[0];
        String[][] lines=new String[row][column];
        String[] rows=new String[row];
        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                PrintWriter pw=new PrintWriter(new FileOutputStream("new.csv"));
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
                boolean drop=false;
                for(int c=0;c<row;c++){
                    for(int p=0;p<mis;p++){
                        if(lines[c][num[p]].isBlank()){
                            drop=true;
                        }
                        else
                            drop=false;
                    }
                    if(drop==true)
                        rows[c]=null;
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
    }

}
