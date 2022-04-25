package project6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Scalers {
    private String filename;
    private int column;
    private float[] saveDataColumn;
    private float saveStandardScaling , minmaxScaling;
    private float mean, standardDeviation , min , max, range;


    public void setMin(float min) {
        this.min = min;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getMean() {
        return mean;
    }

    public void setStandardDeviation(float standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setSaveDataColumn(float[] saveDataColumn) {
        this.saveDataColumn = saveDataColumn;
    }

    public void mainScalers(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter filename : ");
        filename = input.nextLine()+".csv";
        setFilename(filename);
        chooseColumn();
        scanColumn();
        mean();
        System.out.println("which method you want to do the scaling?");
        System.out.println("1: Standard Scaling");
        System.out.println("2: Min Max Scaling");
        int ans = input.nextInt();
        if (ans==1) {
            standardDeviation();
            standardScaling();
        }
        else {
            minmax();
            minmaxScaling();
        }
        Manipulation ask = new Manipulation();
        ask.askQuestion();
    }



    // method to choose which column to perform operation
    public void chooseColumn(){
        Scanner input = new Scanner(System.in);
        System.out.println("Which column do you want to perform Standard Scaling and Min Max Scaling? ");
        System.out.println("Enter number : ");
        column = input.nextInt();
        setColumn(column);
        System.out.println();
    }

    // method to scan data in column
    public void scanColumn(){
        int[] count = countColumnAndRow();
        float[] saveDataColumn = new float[count[1]-1]; // minus 1 because index start from 0
        int count1=0;
        int i = 0;
        String filename = getFilename();

        try {
            Scanner file = new Scanner(new FileInputStream(filename));
            while(file.hasNextLine())
            {
                if(count1==0)
                {
                    file.nextLine();
                }

                String[] col = file.nextLine().split(",");
                saveDataColumn[i]= Float.parseFloat(col[column-1]);
                i++;
                count1++;
            }
            setSaveDataColumn(saveDataColumn);
            file.close();
        }catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        // call method to display data in the column
        displayColumn();
    }

    // method to display the data in the column
    public void displayColumn(){
        int[] count = countColumnAndRow();
        for(int i=0; i<count[1]-1;i++)
        {
            System.out.println(saveDataColumn[i]);
        }
        System.out.println();
    }

    //count total number of rows and columns in original file
    public int[] countColumnAndRow(){
        int[] count = new int[2];
        int countRow=1;

        try {
            Scanner out = new Scanner(new FileInputStream(filename));
            String[] column = out.nextLine().split(",");
            while(out.hasNextLine())
            {
                out.nextLine();
                countRow++;
            }
            count[0]= column.length;
            count[1]= countRow;

        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return count;
    }
    // method to calculate the mean
    public void mean(){
        float total=0;
        float cnt = 0;
        int[] count = countColumnAndRow();
        for(int i=0; i<count[1]-1;i++)
        {
            total += saveDataColumn[i];
            cnt++;
        }
        total = total / cnt;
        setMean(total);
        System.out.printf("The mean is : %.8f\n", total);
    }

    // method to calculate the standard deviation
    public void standardDeviation(){
        float sd = 0, upper = 0, total=0, cnt = 0;
        int[] count = countColumnAndRow();

        for(int i=0; i<count[1]-1;i++)
        {
            total = (float) Math.pow((saveDataColumn[i] - getMean()),2);
            upper +=total;
            cnt++;
        }

        sd = (float) Math.sqrt(upper/cnt); // standard deviation formula
        setStandardDeviation(sd);
        System.out.printf("The standard deviation is : %.8f\n",sd);
        System.out.println();
    }

    // method to compute standard scaling
    public void standardScaling(){
        int[] count = countColumnAndRow();

        System.out.println("Standard Scaling : ");
        for(int i=0; i<count[1]-1;i++)
        {
            saveStandardScaling=((saveDataColumn[i]-mean)/standardDeviation);
            System.out.printf("%.8f\n",saveStandardScaling);
        }
        System.out.println();
    }

    public void minmax(){
        int[] count = countColumnAndRow();
        min = 999999999;
        max = -999999999;
        // to find minimum and maximum value in the column
        for(int i=0; i<count[1]-1;i++)
        {
            if(saveDataColumn[i]<min)
            {
                min = saveDataColumn[i];
            }
            setMin(min);

            if(saveDataColumn[i]>max)
            {
                max = saveDataColumn[i];
            }
            setMax(max);
        }
        // to find range
        range = max-min;
        setRange(range);
        // to check if min and max is right
        System.out.println("The minimum number is : "+min);
        System.out.println("The maximum number is : "+max);
        System.out.printf("The range is : %.8f\n",range);
        System.out.println();
    }

    // method to compute min max scaling
    public void minmaxScaling(){
        int[] count = countColumnAndRow();

        System.out.println("Min Max Scaling : ");
        for(int i=0; i<count[1]-1;i++)
        {
            minmaxScaling=((saveDataColumn[i]-min)/range);
            System.out.printf("%.8f\n",minmaxScaling);
        }
    }

}
