import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by haonantian on 13/12/2017.
 */
public class nnregression {
    public static int upperK = 20; //Maximal number of K
    public static int totalClasses = 10; //Define total numbers of classes
    public static double[] lambdaList = {0.01,0.05,0.1,0.5,1.0,2.0,5.0}; //Define global lambda list
    public static double threshold = 10e-5;

    public static class DataSet{
        private double[][] dataSet;
        private int wide;
        private int length;

        public DataSet(){}

        public DataSet(int wide, int length){
            this.length = length;
            this.wide = wide;
            dataSet = new double[length][wide];
            for (int i = 0; i < length; i++){
                for (int j = 0; j < wide; j++){
                    dataSet[i][j] = -1;
                }
            }
        }

        public int getWide(){
            return this.wide;
        }

        public int getLength(){
            return this.length;
        }

        public void addInRow(int row, int column, double num){
            dataSet[row][column] = num;
        }

        public double getValueOf(int row, int column){
            return dataSet[row][column];
        }

        public void printOut(){
            for (int i = 0; i < length; i++){
                String line = "";
                for(int j = 0; j < wide; j++){
                    line = line.concat(String.valueOf(getValueOf(i,j)));
                }
                line = line.concat("\n");
                System.out.print(line);
            }
        }

        public int getClassLabel(int idx){
            return (int)this.dataSet[idx][0];
        }

        public double[] getRow(int idx){
            double[] outputRow = new double[this.wide-1];
            for (int i = 0; i < outputRow.length; i++){
                outputRow[i] = this.dataSet[idx][i+1];
            }
            return outputRow;
        }

        public double[] getColumn(int idx){
            double[] outputColumn = new double[this.length];
            for(int i = 0; i < this.length; i++){
                outputColumn[i] = this.dataSet[i][idx+1];
            }
            return outputColumn;
        }

        public double[] getBenchMark(int classLabel){
            double[] outputList = new double[this.length];
            for(int i = 0; i < this.length; i ++){
                if ((int)(this.dataSet[i][0]) == classLabel){
                    outputList[i] = 1;
                } else {
                    outputList[i] = 0;
                }
            }
            return outputList;
        }

        public double[][] getBenchSet(){
            double[][] outputSet = new double[10][this.getLength()];
            for (int i = 0; i < 10 ; i++){
                outputSet[i] = this.getBenchMark(i);
            }
            return outputSet;
        }

        public DataSet standardize(){
            DataSet outputSet = new DataSet(this.wide,this.length);
            for (int i = 0; i < this.length; i++){
                outputSet.addInRow(i,0,this.getClassLabel(i));
                double total = 0;
                for (int j = 0; j < this.wide-1; j++){
                    total = total + Math.pow(this.getValueOf(i,j+1),2);
                }
                total = Math.sqrt(total);
                for (int k = 0; k < this.wide-1 ; k++){
                    outputSet.addInRow(i,k+1,this.getValueOf(i,k+1)/total);
                }
            }
            return outputSet;
        }
    }

    public static class RRModel{
        private double[][] modelSet;
        private int length;
        private int wide;

        public RRModel(){}

        public RRModel(int length, int wide){
            this.length = length;
            this.wide = wide;
            modelSet = new double[length][wide+1];
            for (int i = 0; i < this.length; i++){
                for (int j = 0; j < this.wide+1; j++){
                    modelSet[i][j] = -1;
                }
            }
        }

        public int getLength(){
            return this.length;
        }

        public int getWide(){
            return this.wide;
        }

        public int getActualWide(){
            return this.wide+1;
        }

        public void setLambda(double[] lambda){
            int counter = 0;
            while (counter < lambda.length){
                this.modelSet[counter*10][0] = lambda[counter];
                this.modelSet[counter*10+1][0] = lambda[counter];
                this.modelSet[counter*10+2][0] = lambda[counter];
                this.modelSet[counter*10+3][0] = lambda[counter];
                this.modelSet[counter*10+4][0] = lambda[counter];
                this.modelSet[counter*10+5][0] = lambda[counter];
                this.modelSet[counter*10+6][0] = lambda[counter];
                this.modelSet[counter*10+7][0] = lambda[counter];
                this.modelSet[counter*10+8][0] = lambda[counter];
                this.modelSet[counter*10+9][0] = lambda[counter];
                counter++;
            }
        }


        public void addClassW(int classLabel, double lambda, double[] inputW, double[] lambLis){
            int position = findIdx(lambda,lambLis);
            for (int i =0; i < inputW.length; i++){
                this.modelSet[10*position+classLabel][i+1] = inputW[i];
            }
        }

        public void printOut(){
            for(int i = 0; i < this.modelSet.length; i ++){
                String line = "Lambda: ".concat(String.valueOf(this.modelSet[i][0]));
                line = line.concat(" ");
                for (int j = 1; j < this.modelSet[0].length; j++){
                    line = line.concat(String.valueOf(modelSet[i][j]));
                    line = line.concat(" ");
                }
                line = line.concat("\n");
                System.out.print(line);
            }
        }

        public double[] getRow(int idx){
            double[] outputList = new double[this.wide];
            for (int i = 0; i < this.wide; i++){
                outputList[i] = this.modelSet[idx][i+1];
            }
            return outputList;
        }
    }

    public static int findIdx(double item, double[] list){
        for (int i = 0; i < list.length; i++){
            if (item == list[i]){
                return i;
            }
        }
        return -1;
    }

    public static DataSet combineSets(DataSet set1, DataSet set2){ //Combine two sets
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

    public static double[] getRandomW(int wide){ //Initialize W[] list
        Random r = new Random();
        double[] newW = new double[wide];
        for (int i = 0; i < wide; i++){
            newW[i] = 1;
        }
        return newW;
    }

    public static double[] vectorSubtraction(double[] vec1, double[] vec2, int length){ // Vectors subtraction
        double[] outputList = new double[length];
        for (int i = 0; i < length; i++){
            outputList[i] = vec1[i] - vec2[i];
        }
        return outputList;
    }

    public static double dim1VectorMultiplication(double[] vec1, double[] vec2){ //1-d vectors multiplication
        int len = vec1.length;
        double result = 0;
        for (int i = 0; i < len; i++){
            result = result + vec1[i] * vec2[i];
        }
        return result;
    }

    public static double[] genVectorMultiplication(DataSet set, double[] recentW){ //Vectors multiplication with different dimensions
        double[] result = new double[set.getLength()];
        for (int i = 0; i < set.getLength(); i++){
            double[] tempList = set.getRow(i);
            result[i] = dim1VectorMultiplication(tempList,recentW);
        }
        return result;
    }

    public static double vectorNorm(double[] vec){ //Calculate vector norm
        return dim1VectorMultiplication(vec,vec);
    }

    public static double calculateObjFunc(DataSet workSet, double[] recentW, double[] benchMarch, double lambda){
        double[] result = genVectorMultiplication(workSet,recentW);
        result = vectorSubtraction(result,benchMarch,benchMarch.length);
        double total1 = vectorNorm(result);
        double total2 = vectorNorm(recentW);
        return total1 + total2 * lambda;
    }

    public static boolean checkToContinue(double oldVal, double newVal){ // Check if the update process should continue
        if (((oldVal-newVal)/oldVal) <= threshold){
            return false;
        } else {
            return true;
        }
    }

    public static int assignClass(double[] lis){ // Assign each point to class given lambda
        double closest = Double.MAX_VALUE;
        int idx = -1;
        for (int i = 0; i < lis.length; i++){
            if (Math.abs(lis[i] - 1) < closest){
                closest = Math.abs(lis[i] - 1);
                idx = i;
            }
        }
        return idx;
    }

    public static int[] performAnalysis(RRModel RRSet, DataSet workSet, double lambda, double[] lambLis){ // Assign points to classes with various lambdas
        int position = findIdx(lambda,lambLis);
        int[] outputList = new int[workSet.getLength()];
        for (int i = 0; i < workSet.getLength(); i++){
            double[] tempResult = new double[totalClasses];
            double[] tempList2 = workSet.getRow(i);
            for (int j = 0; j < totalClasses; j++){
                double[] tempList1 = RRSet.getRow(totalClasses*position+j);
                tempResult[j] = dim1VectorMultiplication(tempList1,tempList2);
            }
            outputList[i] = assignClass(tempResult);
        }
        return outputList;
    }

    public static double RRAccuracy(DataSet workSet, int[] benchMark){ // Calculate the accuracy
        int counter = 0;
        for (int i = 0; i < workSet.getLength(); i++){
            if (workSet.getClassLabel(i)==benchMark[i]){
                counter ++;
            }
        }
        //System.out.println(counter);
        return ((double)counter/(double)workSet.getLength());
    }

    public static double[] storedLower(DataSet workSet, double lambda){ // Store (X-i^T * X-i + lambda)
        int len = workSet.getWide()-1;
        double[] outputResult = new double[len];
        for (int i = 0; i < len; i++){
            double[] opVec = workSet.getColumn(i);
            outputResult[i] = vectorNorm(opVec)+lambda;
        }
        return outputResult;
    }

    public static double[] storedXy(DataSet workSet, double[] benchMark){ // Store X-i^T * y part
        int len = workSet.getWide()-1;
        double[] outputList = new double[len];
        for (int i =0; i < len; i++){
            double[] tempList = workSet.getColumn(i);
            outputList[i] = dim1VectorMultiplication(tempList,benchMark);
        }
        return outputList;
    }


    public static double[][] storedUpper2(DataSet workSet){ //Store the X-i^T * Xi part
        int len = workSet.getWide()-1;
        double[][] outputSet = new double[len][len-1];
        for (int i =0; i < len; i++){
            double[] tempList = workSet.getColumn(i);
            int counter = 0;
            int idx = 0;
            while (idx < len-1){
                if (counter == i){
                    counter ++;
                } else {
                    double[] tempList2 = workSet.getColumn(counter);
                    outputSet[i][idx] = dim1VectorMultiplication(tempList,tempList2);
                    counter ++;
                    idx ++;
                }
            }
        }
        return outputSet;
    }

    public static double[] getRestFromLis(double[] lis, int idx){ //Get the rest of the list except Wi
        double[] outputList = new double[lis.length-1];
        int counter = 0;
        int inner = 0;
        while (inner < outputList.length){
            if (counter == idx){
                counter ++;
            } else {
                outputList[inner] = lis[counter];
                counter ++;
                inner++;
            }
        }
        return outputList;
    }
    //Main function to generate next Wi
    public static double[] updateWList2(double[] storedXy, double[] storedLow, double[] w, double[][] upp2Set){
        double[] newW = w;
        for (int i = 0; i < w.length; i ++){
            double upper1 = storedXy[i];
            double[] upper2 = upp2Set[i];
            double[] restW = getRestFromLis(newW,i);
            double resultUpp2 = dim1VectorMultiplication(upper2,restW);
            upper1 = upper1 - resultUpp2;
            double newVal = upper1/storedLow[i];
            newW[i] = newVal;
        }
        return newW;
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
                    wid ++;
                }
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

    public static int whichLambda(double[] accuracy){
        double output = 0;
        int idx = -1;
        for (int i = 0; i < accuracy.length; i ++){
            if (accuracy[i] > output){
                output = accuracy[i];
                idx = i;
            }
        }
        return idx;
    }

    public static void writeWeight(String fileName, int lambdaIdx, double accuracy, RRModel RRSet, double lambda){

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            //bw.write("These are the execution Ridge Regression analysis with Lambda = ");
            //bw.write(String.valueOf(lambda) + "\n");
            //bw.write("ACCURACY: " + String.valueOf(accuracy) + "\n");
            //bw.write("The following is the corresponding W[] lists: \n");
            for (int i = 0; i < totalClasses; i++){
                double[] tempList = RRSet.getRow(lambdaIdx*10+i);
                String line = "";
                for (int j = 0; j < tempList.length; j ++){
                    line = line.concat(String.valueOf(tempList[j]));
                    if (j != tempList.length-1){
                        line = line.concat(", ");
                    }
                }
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

    public static void writeOut(String fileName, double accuracy, DataSet workSet, double lambda, int[] classResult){

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write("These are the execution Ridge Regression analysis with Lambda = ");
            bw.write(String.valueOf(lambda) + "\n");
            bw.write("ACCURACY: " + String.valueOf(accuracy) + "\n");
            bw.write("The following is the corresponding class assignment results: \n");
            for (int i = 0; i < workSet.getLength(); i++){
                String line = "";
                line = line.concat("Point #" + String.valueOf(i) + ": ");
                line = line.concat("Class #" + String.valueOf(classResult[i])+"\n");
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

    public static void main(String args[]) throws IOException{
        String trainFile = args[0];
        String validationFile = args[1];
        String testFile = args[2];
        String outputFile = args[3];
        String weightFile = args[4];
        DataSet trainSet = new DataSet();
        trainSet = read(trainFile);
        trainSet = trainSet.standardize();
        DataSet valSet = new DataSet();
        valSet = read(validationFile);
        valSet = valSet.standardize();
        DataSet testSet = new DataSet();
        testSet = read(testFile);
        RRModel RRSet = new RRModel(totalClasses*lambdaList.length,trainSet.getWide()-1);
        RRSet.setLambda(lambdaList);
        double[][] benchSet = trainSet.getBenchSet();
        double[][] storedUpp2 = storedUpper2(trainSet);
        int counter1 = 0;
        while (counter1 < lambdaList.length){
            double lambda = lambdaList[counter1];
            double[] storedLow = storedLower(trainSet,lambda);
            for (int i = 0; i < totalClasses; i++){
                double[] storedXy = storedXy(trainSet,benchSet[i]);
                double[] w =getRandomW(trainSet.getWide()-1);
                double oldVal = calculateObjFunc(trainSet,w,benchSet[i],lambda);
                double newVal = 0;
                while(true){
                    //System.out.println("!!!"+String.valueOf(oldVal)+"!!!");
                    w = updateWList2(storedXy,storedLow,w,storedUpp2);
                    newVal = calculateObjFunc(trainSet,w,benchSet[i],lambda);
                    if (!checkToContinue(oldVal,newVal)){
                        break;
                    } else {
                        oldVal = newVal;
                    }
                }
                System.out.print("@@@Finish lambda = " + String.valueOf(lambda) + " class# " + String.valueOf(i)+"@@@\n");
                RRSet.addClassW(i,lambda,w,lambdaList);
            }
            counter1++;
        }
        int counter2 = 0;
        double[] accuracyList = new double[lambdaList.length];
        while (counter2 < lambdaList.length){
            double lambda = lambdaList[counter2];
            int[] classResult = performAnalysis(RRSet, valSet, lambda, lambdaList);
            accuracyList[counter2] = RRAccuracy(valSet,classResult);
            counter2 ++;
        }
        int lambdaIdx = whichLambda(accuracyList);
        System.out.println("From Validation File: ");
        System.out.println("THE LAMBDA WITH HIGHEST ACCURACY: " + String.valueOf(lambdaList[lambdaIdx]) + "\n");
        System.out.println("ACCURACY: " + String.valueOf(accuracyList[lambdaIdx]));
        System.out.println("Move on to test file...\n");


        double[] newLambdaLis = new double[2];
        newLambdaLis[0] = lambdaList[lambdaIdx];
        newLambdaLis[1] = lambdaList[lambdaIdx]*2;
        DataSet combinedSet = combineSets(trainSet,valSet);
        RRModel testRRSet = new RRModel(2*totalClasses,trainSet.getWide()-1);
        testRRSet.setLambda(newLambdaLis);
        double[][] benchSetT = combinedSet.getBenchSet();
        double[][] storedUpp2T = storedUpper2(combinedSet);
        int counter3 = 0;
        while (counter3 < newLambdaLis.length){
            double lambda = newLambdaLis[counter3];
            double[] storedLow = storedLower(combinedSet,lambda);
            for (int i = 0; i < totalClasses; i++){
                double[] storedXy = storedXy(combinedSet,benchSetT[i]);
                double[] w =getRandomW(combinedSet.getWide()-1);
                double oldVal = calculateObjFunc(combinedSet,w,benchSetT[i],lambda);
                double newVal = 0;
                while(true){
                    //System.out.println("!!!"+String.valueOf(oldVal)+"!!!");
                    w = updateWList2(storedXy,storedLow,w,storedUpp2T);
                    newVal = calculateObjFunc(combinedSet,w,benchSetT[i],lambda);
                    if (!checkToContinue(oldVal,newVal)){
                        break;
                    } else {
                        oldVal = newVal;
                    }
                }
                System.out.print("@@@Finish lambda = " + String.valueOf(lambda) + " class# " + String.valueOf(i)+"@@@\n");
                testRRSet.addClassW(i,lambda,w,newLambdaLis);
            }
            counter3++;
        }


        int counter4 = 0;
        double[] accuracyListT = new double[2];
        while (counter4 < 2){
            double lambda = newLambdaLis[counter4];
            int[] classResult = performAnalysis(testRRSet, testSet, lambda, newLambdaLis);
            accuracyListT[counter4] = RRAccuracy(testSet,classResult);
            counter4 ++;
        }
        int lambdaIdxT = whichLambda(accuracyListT);
        int[] classResult = performAnalysis(testRRSet, testSet, newLambdaLis[lambdaIdxT], newLambdaLis);
        double finalAccuracy = accuracyListT[lambdaIdxT];
        writeOut(outputFile,finalAccuracy,testSet,newLambdaLis[lambdaIdxT],classResult);
        for (int i = 0; i < accuracyListT.length; i++){
            System.out.print("Lambda: " + String.valueOf(newLambdaLis[i]) + ", ");
            System.out.print("ACCURACY: " + String.valueOf(accuracyListT[i]) + "\n");
        }
        System.out.println("THE LAMBDA WITH HIGHEST ACCURACY: " + String.valueOf(newLambdaLis[lambdaIdxT]) + "\n");
        System.out.println("ACCURACY: " + String.valueOf(accuracyListT[lambdaIdxT]));
        writeWeight(weightFile,lambdaIdxT,accuracyListT[lambdaIdxT],testRRSet,newLambdaLis[lambdaIdxT]);
        System.out.print("\n");
        System.out.print("Done\n");
    }


}
