/**
 * Created by haonantian on 07/12/2017.
 */

public class DataSet {
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
                outputList[i] = -1;
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