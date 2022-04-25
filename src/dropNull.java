//import java.util.Scanner;
//
//public class dropNull {
//
//    Scanner s=new Scanner(System.in);
//    System.out.print("Enter a file name : ");
//    String filename = "C:\\Users\\User\\Desktop\\"+s.nextLine()+".csv";
//        System.out.print("Number of columns for missing data : ");
//    int mis=s.nextInt();
//    int[] num=new int[mis];
//        for(int p=0;p<mis;p++){
//        System.out.print("Column? ");
//        num[p]=s.nextInt();
//    }
//    int[] cr=countColumnAndRow(filename);
//    int row=cr[1]-1;    //exclude header
//    int column=cr[0];
//    String[][] lines=new String[row][column];
//    String[] rows=new String[row];
//        try{
//        Scanner f2 = new Scanner(new FileInputStream(filename));
//        try{
//            PrintWriter pw=new PrintWriter(new FileOutputStream("C:\\Users\\User\\Desktop\\new.csv"));
//            pw.println(f2.nextLine());
//            int i=0;
//            while(f2.hasNextLine()){
//                String line=f2.nextLine();
//                rows[i]=line;
//                String[] det=line.split(",");
//                for(int j=0;j<det.length;j++)
//                    lines[i][j]=det[j];
//                i++;
//            }
//            boolean drop=false;
//            for(int c=0;c<row;c++){
//                for(int p=0;p<mis;p++){
//                    if(lines[c][num[p]].isBlank()){
//                        drop=true;
//                    }
//                    else
//                        drop=false;
//                }
//                if(drop==true)
//                    rows[c]=null;
//            }
//            for(String a:rows){
//                if(a!=null)
//                    pw.println(a);
//            }
//            pw.close();
//            f2.close();
//        }catch(IOException e){
//            System.out.println("File output error");
//        }
//    }catch(FileNotFoundException e){
//        System.out.println("File was not found");
//    }
//}
