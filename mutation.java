import java.util.Random;
import java.util.Arrays;


public class Mutation{

    // test function
    public static void main(String[] args){
        UniformMutation UM = new UniformMutation();
//        UM.setUpperBound(5);
//        UM.setLowerBound(-5);
        double[] oldgenes = new double[] {-1.60, -2.18, 3.2342, 0.234};
        double[] newgenes = UM.mutate(oldgenes, 2);
        System.out.println(Arrays.toString(newgenes));

        NonUniformMutation NUM = new NonUniformMutation();
        NUM.setUpperBound(5);
        NUM.setLowerBound(-5);
        double[] newgenes2 = NUM.mutate(oldgenes, 2);
        System.out.println(Arrays.toString(newgenes2));
    }

    // Stuff that is true for all mutations
    // lower and upper should be -5 and 5
    public int lowerBound = -5;
    public int upperBound = 5;

    public void setLowerBound(int numLow){
        lowerBound = numLow;
    }

    public void setUpperBound(int numHigh){
        upperBound = numHigh;
    }
}

class UniformMutation extends Mutation{
    // Discuss if float is correct, or it should be double or something else
    public double[] mutate(double[] oldgenes, int index){
        Random r = new Random();
        // Does this include the lower and upperbounds? Should it?

        double random = lowerBound + r.nextDouble() * (upperBound - lowerBound);

        // cloning might not be needed if non-mutated child is removed anyway
        double[] newgenes = oldgenes.clone();
        newgenes[index] = random;
        return newgenes;
    }
}

class NonUniformMutation extends Mutation{
    public double[] mutate(double[] oldgenes, int index){
        Random r = new Random();
        double gaussianValue = r.nextGaussian();
        // non-standard mean and std:
        // r.nextGaussain() * std + mean

        // ^ should prob. do this cause gaussian values can get pretty big rn

        double[] newgenes = oldgenes.clone();
        newgenes[index] += gaussianValue;
        // new value must not extend bounds
        if(newgenes[index] < lowerBound){
            newgenes[index] = lowerBound;
        }
        else if(newgenes[index] > upperBound){
            newgenes[index] = upperBound;
        }
        return newgenes;

    }
}
class NonUniformMutationPere extends Mutation{
    public double mutateSigma(int size, double sigma){
        Random r = new Random();
        double tau = 1/Math.sqrt(2*size);
        double gamma = Math.exp(tau*r.nextGaussian());
        sigma = sigma*gamma;
        return sigma;
    }
    public double[] mutate(double[] oldgenes, int index, float sigma){
        Random r = new Random();
        double gaussianValue = r.nextGaussian()*sigma;
        // non-standard mean and std:
        // r.nextGaussain() * std + mean

        // ^ should prob. do this cause gaussian values can get pretty big rn

        double[] newgenes = oldgenes.clone();
        newgenes[index] += gaussianValue;
        // new value must not extend bounds
        if(newgenes[index] < lowerBound){
            newgenes[index] = lowerBound;
        }
        else if(newgenes[index] > upperBound){
            newgenes[index] = upperBound;
        }
        return newgenes;
    }
}

class NStepNonUniformMutationPere extends Mutation{
    public double[] mutateSigmas(int size, double[] sigmas){
        Random r = new Random();
        double tau = 1/Math.sqrt(2*size);
        double gamma = Math.exp(tau*r.nextGaussian());
        for (int i = 0; i < size; i++) {
            sigmas[i] = sigmas[i]*gamma*Math.exp((1/Math.sqrt(2*Math.sqrt(size)))*r.nextGaussian());
        }
        return sigmas;
    }
    public double[] mutate(double[] oldgenes, int index, float sigma){
        Random r = new Random();
        double gaussianValue = r.nextGaussian()*sigma;
        // non-standard mean and std:
        // r.nextGaussain() * std + mean

        // ^ should prob. do this cause gaussian values can get pretty big rn

        double[] newgenes = oldgenes.clone();
        newgenes[index] += gaussianValue;
        // new value must not extend bounds
        if(newgenes[index] < lowerBound){
            newgenes[index] = lowerBound;
        }
        else if(newgenes[index] > upperBound){
            newgenes[index] = upperBound;
        }
        return newgenes;
    }
}