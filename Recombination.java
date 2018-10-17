import java.util.Random;
import java.util.Arrays;


public class Recombination{
    // test
    public static void main(String[] args){
        double[] parent1 = new double[] {-1.60, -2.18, 3.2342, 0.234};
        double[] parent2 = new double[] {1.439, -0.238, 4.92, -2.55};

        DiscreteRecombination DR = new DiscreteRecombination();
        DR.setGenomeSize(parent1.length);
        double[] child = DR.recombine(parent1, parent2, 2);
        System.out.println("Discrete Recombination:");
        System.out.println(Arrays.toString(child));

        ////////////////////////////////////
        SimpleArithRecombination SimAR = new SimpleArithRecombination();
        SimAR.setGenomeSize(parent1.length);
        // child 1 gets first few genes from parent1, child2 from parent2
        double[] child1 = SimAR.recombine(parent1, parent2,2);
        double[] child2 = SimAR.recombine(parent2, parent1, 2);
        System.out.println("Simple Arithmetic Recombination:");
        System.out.println(Arrays.toString(child1));
        System.out.println(Arrays.toString(child2));

        //////////////////////////////////////
        SingleArithRecombination SinAR  = new SingleArithRecombination();
        SinAR.setGenomeSize(parent1.length);
        double[] child3 = SinAR.recombine(parent1, parent2,2);
        double[] child4 = SinAR.recombine(parent2, parent1, 2);
        System.out.println("Single Arithmetic Recombination:");
        System.out.println(Arrays.toString(child3));
        System.out.println(Arrays.toString(child4));

        ///////////////////////////////////////
        WholeArithRecombination WAR = new WholeArithRecombination();
        WAR.setGenomeSize(parent1.length);
        double[] child5 = WAR.recombine(parent1, parent2,2);
        System.out.println("Whole Arithmetic Recombination");
        System.out.println(Arrays.toString(child5));

    }


    // Stuff that is true for all recombinations
    public int genomeSize = 10;
    public double scaling_factor = 0.75;

    public void setGenomeSize(int size){
        genomeSize = size;
    }
    public void setScalingFactor(double factor){
        scaling_factor = factor;
    }

}

class DiscreteRecombination extends Recombination{
    public double[] recombine(double[] parentA, double[] parentB, int k){
        Random r = new Random();
        double[] child = new double[genomeSize];
        for(int i=0; i<genomeSize; i++){
            boolean p1 = r.nextBoolean();
            if (p1) {child[i] = parentA[i];}
            else    {child[i] = parentB[i];}
        }
        return child;
    }
}

class SimpleArithRecombination extends Recombination{
    public double[] recombine(double[] parentA, double[] parentB, int k){
        double[] child = new double[genomeSize];
        // first k same as one parent
        for(int i=0; i<k; i++){
            child[i] = parentA[i];
        }
        // rest average
        for(int i=k; i<genomeSize; i++){
            child[i] = (parentA[i]+parentB[i])/2;
        }
        return child;
    }
}

class SingleArithRecombination extends Recombination{
    public double[] recombine(double[] parentA, double[] parentB, int k){
        double[] child = parentA.clone();
        child[k] = (parentA[k] + parentB[k])/2;
        return child;
    }
}

class WholeArithRecombination extends Recombination{
    public double[] recombine(double[] parentA, double[] parentB, int k){
        double[] child = new double[genomeSize];
        for (int i=0; i<genomeSize; i++){
            child[i] = (parentA[i]+parentB[i])/2;
        }
        return child;
    }
}

class DiferentialRecombination extends Recombination{
    public double[] recombine(double[] parentA, double[] parentB, int k){
        Random r = new Random();
        double[] child = new double[genomeSize];
        for (int i = 0; i < genomeSize; i++) {
            if (r.nextBoolean()) {
                child[i] = parentA[i];
            }
            else {
                child[i] = parentB[i];
            }

            child[i] = child[i] + ((parentA[i] - parentB[i]) * scaling_factor);
        }
        return child;
    }
}
class DiferentialFourRecombination extends Recombination{
    public double[] recombine(double[] parentA, double[] parentB,double[] parentC,double[] parentD, int k){
        Random r = new Random();
        double[] child = new double[genomeSize];
        for (int i = 0; i < genomeSize; i++) {
            if (r.nextBoolean()) {
                child[i] = parentA[i];
            }
            else {
                child[i] = parentB[i];
            }

            child[i] = child[i] + ((parentA[i] - parentB[i]) * scaling_factor);
        }
        return child;
    }
}