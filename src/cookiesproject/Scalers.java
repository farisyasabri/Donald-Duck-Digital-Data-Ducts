package cookiesproject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Scalers {
    private String filename;
    private int column;
    private float[] saveDataColumn;
    private float saveStandardScaling , minmaxScaling;
    private float mean, standardDeviation , min , max, range;

    public float getSaveStandardScaling() {
        return saveStandardScaling;
    }

    public void setSaveStandardScaling(float saveStandardScaling) {
        this.saveStandardScaling = saveStandardScaling;
    }

    public float getMinmaxScaling() {
        return minmaxScaling;
    }

    public void setMinmaxScaling(float minmaxScaling) {
        this.minmaxScaling = minmaxScaling;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getStandardDeviation() {
        return standardDeviation;
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

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public float[] getSaveDataColumn() {
        return saveDataColumn;
    }

    public void setSaveDataColumn(float[] saveDataColumn) {
        this.saveDataColumn = saveDataColumn;
    }

    public void mainScalers(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter filename : ");
        filename = input.nextLine()+".csv";
        setFilename(filename);

    }

    public void chooseColumn(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a column : ");
        column = input.nextInt();
        setColumn(column);
        System.out.println();
        //scanColumn();
    }

    public void scanColumn(){
        int[] count = countColumnAndRow();
        float[] saveDataColumn = new float[count[1]-1];
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
        displayColumn();
    }
    public void displayColumn(){
        int[] count = countColumnAndRow();
        for(int i=0; i<count[1]-1;i++)
        {
            System.out.println(saveDataColumn[i]);
        }
        System.out.println();
    }

    //count total number of rows and columns
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
        //return total;
    }

    public void standardDeviation(){
        float sd = 0;
        float upper = 0;
        float total=0;
        float cnt = 0;
        int[] count = countColumnAndRow();
        for(int i=0; i<count[1]-1;i++)
        {
            total = (float) Math.pow((saveDataColumn[i] - getMean()),2);
            upper +=total;
            cnt++;
        }
        sd = (float) Math.sqrt(upper/cnt);
        setStandardDeviation(sd);
        System.out.printf("The standard deviation is : %.8f\n",sd);
        System.out.println();
    }

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
        max = -99999999;
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
        range = max-min;
        setRange(range);
        System.out.println("The minimum number is : "+min);
        System.out.println("The maximum number is : "+max);
        System.out.printf("The range is : %.8f\n",range);
        System.out.println();
    }

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
