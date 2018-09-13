/**
 * Created by haonantian on 09/12/2017.
 */
public class Distance {
    private double[] distance;
    private int length;

    public Distance(){}

    public Distance(int length){  //Initialization
        this.length = length;
        distance = new double[length];
        for (int i =0 ;i < length; i++){
            distance[i] = -1.0;
        }
    }

    public void set(int idx, double distance){ //Add new distance to the distance list
        if (idx >= length){
            System.out.println("Distance array index out of bound");
            System.exit(1);
        }
        this.distance[idx] = distance;
    }

    public double getDistance(int idx){//Get distance for certain index
        return this.distance[idx];
    }

    public void printOut(){ //Print out function
        for (int i = 0; i < length; i ++){
            System.out.println(this.distance[i]);
        }
    }

    public int getLength(){ //Get the length of the distance list
        return this.length;
    }
}
