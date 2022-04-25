package project6;

import java.io.*;
import java.util.Scanner;

public class Manipulation {
    //----------------------- to edit file ----------------------------------------------
    public void editFile(){
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";

        System.out.print("Enter number of row : ");
        int row = s.nextInt();

        System.out.print("Enter number of column : ");
        int column = s.nextInt();

        createFile(filename,row,column);
    }

    // ----------------------- to create file ----------------------------------------------
    public void createFile(String filename,int row, int column){
//        Scanner s = new Scanner(System.in);
        try{
            PrintWriter outData = new PrintWriter(new FileOutputStream(filename, true));
            PrintWriter outHeader = new PrintWriter(new FileOutputStream(filename,true));

            // call method to add header
            String[] header = addHeader(column);
            for (int i=0; i<column-1;i++)
            {outHeader.write(header[i]+",");}
            outHeader.write(header[column-1]);
            outHeader.println();
            outHeader.close();

            // call method to add data
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

    //------------------------- create the header for data ---------------------------------
    public String[] addHeader (int column){
        Scanner s = new Scanner(System.in);
        String[] header = new String[column];
        for (int i=0; i<column;i++){
            System.out.print("Header column " + (i+1) + " : ");
            header[i] = s.nextLine();
        }
        return header;
    }

    //----------------------------- add data in the csv file ------------------------------------
    public String[][] addData (int row, int column){
        Scanner s = new Scanner(System.in);
        String[][] data = new String[row][column];
        for (int i=0;i<row;i++) {
            for (int j=0;j<column;j++){
                System.out.print("Row " + (i+1) + " Column " + (j+1)  +" : ");
                data[i][j]= s.nextLine();}
        }
        return data;
    }

    // method to ask question to user
    public void askQuestion(){
        System.out.println();
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Do you want to continue the process? [yes or no] : ");
        String answer = s.next();
        if (answer.equalsIgnoreCase("yes"))
            Main.chooseStep();
        else
            System.out.println("Process Ended");
    }

    // -------------------------- read the file that has been created ---------------------------------
    public void readfile(){
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.println();
        System.out.println();
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

    // ------------------------------ count rows and columns ----------------------------------
    public int[] countColumnAndRow(String filename){
        int[] count=new int[2];
        int countRow=1;
        try{
            Scanner out = new Scanner(new FileInputStream(filename));
            String[] col=out.nextLine().split(",");
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

    // ------------------------------ data manipulation --------------------------------------------

    //------------------------------------ stack columns ------------------------------------------
    public void ColumnConcatenation(){
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";

        System.out.print("Enter another existing file name for stacking columns : ");
        String filename2 = s.nextLine()+".csv";

        int[] count1 = countColumnAndRow(filename);
        int[] count2 = countColumnAndRow(filename2);

        // to print how many columns and rows in both file
        System.out.println("File 1 column : "+count1[0]);
        System.out.println("File 2 column : "+count2[0]);
        System.out.println("File 1 row : "+count1[1]);
        System.out.println("File 2 row : "+count2[1]);

        // check if the number of rows for each file is same
        if (count1[1]==count2[1]){
            // make new file for concatenation
            System.out.print("New file name for the of column concatenation result: ");
            String newFilename = s.nextLine()+".csv";

            try{
                Scanner f1 = new Scanner(new FileInputStream(filename));
                Scanner f2 = new Scanner(new FileInputStream(filename2));

                try{
                    // create new file
                    PrintWriter pw = new PrintWriter(new FileOutputStream(newFilename));
                    while(f2.hasNextLine())
                    // column file 1 + column file 2
                    { pw.println(f1.nextLine()+","+f2.nextLine());}
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
                    // overwrite data from new file to old file (file 1)
                    PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
                    while(f1.hasNextLine())
                    {pw.println(f1.nextLine());}
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

    // ---------------------------------- stack rows ----------------------------------------------
    public void RowConcatenation(){

        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";
        System.out.print("Enter another existing file name for stacking rows : ");
        String filename2 = s.nextLine()+".csv";
        System.out.print("Enter filename to store the result of concatenation: ");
        String newfile = s.nextLine()+".csv";
        int[] count1 = countColumnAndRow(filename);
        int[] count2 = countColumnAndRow(filename2);
        System.out.println("Column - "+count1[0]+" and "+count2[0]);
        System.out.println("Row - "+count1[1]+" and "+count2[1]);
        if (count1[0]==count2[0]){
            try{
                Scanner f1 = new Scanner(new FileInputStream(filename));
                Scanner f2 = new Scanner(new FileInputStream(filename2));
                try{
                    PrintWriter pw=new PrintWriter(new FileOutputStream(newfile)); //file baru utk simpan data
                    pw.println(f1.nextLine());  //print header
                    f2.nextLine();   //exclude header
                    while(f1.hasNextLine()){
                        String line=f1.nextLine();
                        if(line.isBlank()){
                            break;
                        }
                        pw.println(line);
                    }
                    while(f2.hasNextLine())
                        pw.println(f2.nextLine());
                    pw.close();
                }catch(IOException e){}
                f2.close();
            }catch(FileNotFoundException e){
                System.out.println("File was not found");
            }
        }
        else
            System.out.println("Unequal number of columns");

        System.out.println();
        askQuestion();
    }

    // -------------------------------------- make row subset ------------------------------------------
    public void subsetRow(){
        System.out.println();
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
                // make new file for subset
                PrintWriter pw = new PrintWriter(new FileOutputStream(newfilename));
                pw.println(f2.nextLine());

                for(int i=1;i<row1;i++)
                    f2.nextLine();
                while(count<row2)
                {pw.println(f2.nextLine());
                    count++;}

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

    // --------------------------------- make column subset -------------------------------------
    public void subsetColumn(){
        System.out.println();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a file name to obtain data from : ");
        String filename = s.nextLine()+".csv";

        // cm -> column quantity
        System.out.print("How many column(s) to be displayed? ");
        int cm = s.nextInt();
        int[] cl = new int[cm];
        for(int i = 0; i<cm ; i++){
            // which column to show
            System.out.print("Column : ");
            cl[i] = s.nextInt()-1;
            s.nextLine();
        }

        System.out.print("New file name for column subset: ");
        String newfilename = s.nextLine()+".csv";
        System.out.println();

        try{
            Scanner f2 = new Scanner(new FileInputStream(filename));
            try{
                // make a new file with column subset
                PrintWriter pw=new PrintWriter(new FileOutputStream(newfilename));
                while(f2.hasNextLine())
                {String line=f2.nextLine();
                    String[] col=line.split(",");

                    for(int i=0;i<cm-1;i++)
                    {pw.print(col[cl[i]]+",");}

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

    // -------------------------- sort column (alphabetical or numerical) --------------------------------------
    public void sortByColumn(){
        System.out.println();
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";

        // input which column to sort
        System.out.print("Column to be sorted : ");
        int c=s.nextInt()-1;

        // choose to sort alphabetical or numerical
        System.out.println("1: Alphabetical order\n2: Numerical order\nEnter Number : ");
        int ans=s.nextInt();
        s.nextLine();

        // make a new file for the sorted column (tak guna pun)
        System.out.print("New filename for the sorted column: ");
        String newfilename = s.nextLine()+".csv";
        System.out.println();
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

        //
        try{
            Scanner f1 = new Scanner(new FileInputStream(newfilename)); //filename
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

        askQuestion();
    }

    // --------------------------- remove duplicate data -----------------------------------------
    public void duplicate(){
        System.out.println();
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";

        // make new file for the new version
        System.out.print("New file name: ");
        String newfilename = s.nextLine()+".csv";

        // which column to remove
        System.out.print("Column for the duplicate to be dropped? [Enter number] : ");
        int ans=s.nextInt()-1;
        int[] cr=countColumnAndRow(filename);
        int row=cr[1]-1;    // to exclude header
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
                                // which row to remove
                                System.out.print("Row to be dropped? [Enter number] : ");
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

    //---------------------------- remove rows that is empty from chosen column(s) ---------------------------------
    public void dropNull(){
        System.out.println();
        Scanner s=new Scanner(System.in);
        System.out.print("Enter a file name : ");
        String filename = s.nextLine()+".csv";

        System.out.print("New file name: ");
        String newfilename = s.nextLine()+".csv";

        System.out.print("Number of columns for missing data : ");
        int mis=s.nextInt();
        int[] num=new int[mis];
        for(int p=0;p<mis;p++){
            System.out.print("Which column? [Enter Number] : ");
            num[p]=s.nextInt()-1;
        }
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
        System.out.println("File saved!");
        System.out.println();
        askQuestion();
    }
}
