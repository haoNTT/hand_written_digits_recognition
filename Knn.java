import java.io.IOException;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by haonantian on 06/12/2017.
 */

public class Knn {
    private int[][] indexList;
    private int length;
    private int k;

    public static int upperK = 20; //Maximal number of K
    public static double[] lambdaList = {0.01,0.05,0.1,0.5,1.0,2.0,5.0}; //Define global lambda list
    public static int totalClasses = 10; //Define total numbers of classes
    public static double threshold = 10e-5;
    public Knn(){}
    public Knn(int numK, int length){ //Knn indexList, format is index#, label#
        this.k = numK;
        this.length = length;
        indexList = new int[length][2];
        for (int i = 0; i < length; i++){
            indexList[i][0] = -1;
            indexList[i][1] = -1;
        }
    }

    public int getLabel(int idx){
        return this.indexList[idx][1];
    }

    public int getK(){
        return this.k;
    }

    public static void listPrintOut(double[] lis){
        String line = "";
        for (int i =0; i < lis.length; i ++){
            line = line.concat(String.valueOf(lis[i]));
            line = line.concat(" ");
        }
        line = line.concat("\n");
        System.out.println(line);
    }

    public static int getPopularElement(int[] a) { //Find the most popular item in an int[]
        int count = 1, tempCount;
        int popular = a[0];
        int temp = 0;
        for (int i = 0; i < (a.length - 1); i++) {
            temp = a[i];
            tempCount = 0;
            for (int j = 1; j < a.length; j++) {
                if (temp == a[j])
                    tempCount++;
            }
            if (tempCount > count) {
                popular = temp;
                count = tempCount;
            }
        }
        return popular;
    }

    public void make(Distance DisList, DataSet inputSet, int idxInDataSet){ //Assign a new class to a index value in the DataSet
        int len = DisList.getLength();
        double[] tempList = new double[len];
        for (int i = 0; i < len; i ++){
            tempList[i] = DisList.getDistance(i);
        }
        int [] result = new int[k];
        int counter = 0;
        while (counter < k){
            double smallestDis = Double.MAX_VALUE;
            int smallestIdx = -1;
            for (int i = 0; i < len; i ++){
                if (tempList[i] < smallestDis){
                    smallestDis = tempList[i];
                    smallestIdx = i;
                }
            }
            result[counter] = inputSet.getClassLabel(smallestIdx);
            tempList[smallestIdx] = Double.MAX_VALUE;
            counter ++;
        }
        int mostPopular = getPopularElement(result);
        //System.out.println("@@@"+String.valueOf(mostPopular)+"@@@");
        indexList[idxInDataSet][0] = idxInDataSet;
        indexList[idxInDataSet][1] = mostPopular;
    }

    public void makeCosine(Distance DisList, DataSet inputSet, int idxInDataSet){ //Assign a new class to a index value in the DataSet
        int len = DisList.getLength();
        double[] tempList = new double[len];
        for (int i = 0; i < len; i ++){
            tempList[i] = DisList.getDistance(i);
        }
        int [] result = new int[k];
        int counter = 0;
        while (counter < k){
            double largestSim = -1;
            int largestIdx = -1;
            for (int i = 0; i < len; i ++){
                if (tempList[i] > largestSim){
                    largestSim = tempList[i];
                    largestIdx = i;
                }
            }
            result[counter] = inputSet.getClassLabel(largestIdx);
            tempList[largestIdx] = -1;
            counter ++;
        }
        int mostPopular = getPopularElement(result);
        //System.out.println("@@@"+String.valueOf(mostPopular)+"@@@");
        indexList[idxInDataSet][0] = idxInDataSet;
        indexList[idxInDataSet][1] = mostPopular;
    }

    public Distance calculateDist(DataSet workSet, int idx, DataSet backgroundSet){ //Calculate distances between two intArray
        Distance outputDist = new Distance(backgroundSet.getLength());
        double[] workIntArr = workSet.getRow(idx);
        for (int i = 0; i < backgroundSet.getLength(); i++){
            double[] backIntArr = backgroundSet.getRow(i);
            double total = 0;
            for (int j = 0; j < backgroundSet.getWide()-1; j++){
                total = total + Math.pow((workIntArr[j] - backIntArr[j]),2);
            }
            total = Math.sqrt(total);
            outputDist.set(i,total);
        }
        return outputDist;
    }
    public Distance calculateCosine(DataSet workSet, int idx, DataSet backgroundSet){ //Calculate cosine similarity between two intArray
        Distance outputDist = new Distance(backgroundSet.getLength());
        double[] workIntArr = workSet.getRow(idx);
        for (int i = 0; i < backgroundSet.getLength(); i++){
            double[] backIntArr = backgroundSet.getRow(i);
            double total = 0;
            double len1 = 0;
            double len2 = 0;
            for (int j = 0; j < backgroundSet.getWide()-1; j++){
                total = total + workIntArr[j]*backIntArr[j];
                len1 = len1 + Math.pow(workIntArr[j],2);
                len2 = len2 + Math.pow(backIntArr[j],2);
            }
            len1 = Math.sqrt(len1);
            len2 = Math.sqrt(len2);
            total = total/(len1*len2);
            outputDist.set(i,total);
        }
        return outputDist;
    }

    public void performKnn(DataSet workSet, DataSet backSet){ //function for perform Knn
        int len = workSet.getLength();
        for (int i = 0; i < len; i ++){
            //Distance tempDist = calculateDist(workSet,i,backSet);
            //make(tempDist,backSet,i);
            Distance tempDist = calculateCosine(workSet,i,backSet);
            makeCosine(tempDist,backSet,i);
        }
    }

    public double getAccuracy(DataSet set){ //Calculate the accuracy
        int matchNum = 0;
        for (int i = 0; i < this.length; i++){
            if (getLabel(i)==set.getClassLabel(i)){
                matchNum ++;
            }
        }
        double result = (double)(matchNum)/(double)(set.getLength());
        System.out.println("@@@ ACCURACY : "+String.valueOf(result)+"@@@");
        return result;
    }

    public static DataSet combineSets(DataSet set1, DataSet set2){
        int len1 = set1.getLength();
        int len2 = set2.getLength();
        int newLen = len1 + len2;
        DataSet newSet = new DataSet(set1.getWide(),newLen);
        for (int i = 0; i < len1; i ++){
            for (int i2 = 0; i2 < set1.getWide(); i2 ++){
                newSet.addInRow(i,i2,set1.getValueOf(i,i2));
            }
        }
        for (int j = len1; j < newLen; j++){
            for (int j2 = 0; j2 < set1.getWide(); j2++){
                newSet.addInRow(j,j2,set2.getValueOf(j-len1,j2));
            }
        }
        return newSet;
    }

    public static DataSet read(String fileName){ //Read in CSV file to a int[][] dataSet
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = "";

            String[] tempArr;
            int length = 0;
            int wide = 0;
            while ((line = br.readLine()) != null) {
                tempArr = line.split(",");
                int wid = 0;
                for (String tempStr : tempArr) {
                    //System.out.print(tempStr + " ");
                    wid ++;
                }
                //System.out.println();
                wide = wid;
                length ++;
            }
            br.close();
            DataSet newSet = new DataSet(wide,length);
            FileReader fr2 = new FileReader(file);
            BufferedReader br2 = new BufferedReader(fr2);
            String wLine = "";
            String[] tempArr2;
            int counter = 0;
            while((wLine = br2.readLine())!=null){
                tempArr2 = wLine.split(",");
                int counter2 = 0;
                while (counter2 < tempArr2.length) {
                    newSet.addInRow(counter,counter2,Double.parseDouble(tempArr2[counter2]));
                    counter2 ++;
                }
                counter++;
            }
            br2.close();
            return newSet;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void write(String fileName, Knn newKnn, double accuracy) {

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("These are the execution results for test set with K value = ");
            bw.write(String.valueOf(newKnn.getK()) + "\n");
            bw.write("ACCURACY: ");
            bw.write(String.valueOf(accuracy)+"\n");
            for (int i = 0; i < newKnn.length; i++){
                String line = "Point #";
                line = line.concat(String.valueOf(i));
                line = line.concat(": class #");
                line = line.concat(String.valueOf(newKnn.getLabel(i)));
                line = line.concat("\n");
                bw.write(line);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }

    public static void main(String args[]) throws IOException{ //main function
        double startTime = System.currentTimeMillis();
        System.out.println("The preparation process may be bit long, be patient...");
        String trainFile = args[0];
        String validationFile = args[1];
        String testFile = args[2];
        String outputFile = args[3];
        //KNN section
        DataSet trainSet = new DataSet();
        trainSet = read(trainFile);
        DataSet valSet = new DataSet();
        valSet = read(validationFile);
        DataSet testSet = new DataSet();
        testSet = read(testFile);
        int K = 1;
        double[] accuracyList = new double[upperK];
        while (K <= upperK){
            Knn result = new Knn(K,valSet.getLength());
            result.performKnn(valSet,trainSet);
            accuracyList[K-1] = result.getAccuracy(valSet);
            //System.out.println("@@@"+String.valueOf(accuracyList[K-1])+"@@@");
            System.out.println("!!!Finish K = "+String.valueOf(K)+"!!!");
            //System.out.println("Accuracy = " + String.valueOf(accuracyList[K-1]));
            K ++;
        }
        double maxAcc = 0;
        int maxIdx = -1;
        for (int i = 0; i < upperK; i++){
            if (maxAcc < accuracyList[i]){
                maxAcc = accuracyList[i];
                maxIdx = i;
            }
        }
        System.out.print("The K value with highest accuracy is ");
        System.out.print(String.valueOf(maxIdx+1) + "\n");
        System.out.println("Move on to test file...");


        DataSet combinedSet = combineSets(trainSet,valSet);
        Knn result2 = new Knn(maxIdx+1,testSet.getLength());
        result2.performKnn(testSet,combinedSet);
        double accuracy = result2.getAccuracy(testSet);
        write(outputFile,result2,accuracy);
        System.out.print("ACCURACY: "+String.valueOf(accuracy)+"\n");
        double endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + String.valueOf(endTime-startTime)+"\n\n");
        System.out.print("Done\n");
    }
}
