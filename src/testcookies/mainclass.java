package testcookies;

import java.util.Scanner;

public class mainclass {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        SaveDataFrame a = new SaveDataFrame();

        //ask user to enter file name (either new or existing file)
//        System.out.print("Enter File Name: ");
//        String filename = s.nextLine() + ".csv";
//        a.setFilename(filename);

        //next step
        System.out.println("next step (enter number)");
        System.out.println("edit file : 1");
        System.out.println("display file : 2");
        System.out.println("merge file : 3");
        int num = s.nextInt();

        chooseStep(num);

        // display working directory at the end of the output
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }

    public static void chooseStep(int num) {
        knn k = new knn();
        masyi m = new masyi();
        SaveDataFrame a = new SaveDataFrame();
        switch (num) {
//            case 1 : a.editFile(filename);
//                break;
//            case 2 : a.readfile(filename);
//                break;
            case 3 : k.mainKNN();
            default: m.mainKNN();
        }
    }

}
