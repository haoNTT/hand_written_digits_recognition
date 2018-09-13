/**
 * Created by haonantian on 13/12/2017.
 */
public class RRModel {
    private double[][] modelSet;
    private int length;
    private int wide;

    public static int totalClasses = 10; //Define total numbers of classes
    public static double[] lambdaList = {0.01,0.05,0.1,0.5,1.0,2.0,5.0}; //Define global lambda list
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

    public static int findIdx(double item, double[] list){
        for (int i = 0; i < list.length; i++){
            if (item == list[i]){
                return i;
            }
        }
        return -1;
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
