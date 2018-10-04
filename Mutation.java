import java.util.Random;
import java.util.Arrays;


public class Mutation{

    // test function
    public static void main(String[] args){
        UniformMutation UM = new UniformMutation();
//        UM.setUpperBound(5);
//        UM.setLowerBound(-5);
        Individual ind = new Individual();
        double[] oldgenes = ind.genome;
        UM.mutate(ind,3);
        double[] newgenes = ind.genome;
        System.out.println(Arrays.toString(newgenes));

        NonUniformMutation NUM = new NonUniformMutation();
        NUM.setUpperBound(5);
        NUM.setLowerBound(-5);
        NUM.mutate(ind,3);
        double[] newgenes2 = ind.genome;
        System.out.println(Arrays.toString(newgenes2));

        SingleNonUniformMutation NUMP = new SingleNonUniformMutation();
        NUMP.setPopulation(50);
        NUMP.mutate(ind,3);
        double[] newgenes3 = ind.genome;
        System.out.println(Arrays.toString(newgenes3));
        System.out.println(ind.sigma);

        NStepNonUniformMutation NSUMP = new NStepNonUniformMutation();
        NSUMP.mutate(ind,3);
        double[] newgenes4 = ind.genome;
        System.out.println(Arrays.toString(newgenes4));
        System.out.println(Arrays.toString(ind.sigmas));

    }

    // Stuff that is true for all mutations
    // lower and upper should be -5 and 5
    public int lowerBound = -5;
    public int upperBound = 5;
    public int size = 50;


    public void setLowerBound(int numLow){
        lowerBound = numLow;
    }

    public void setUpperBound(int numHigh){
        upperBound = numHigh;
    }

    public void setPopulation(int numSize){
        size = numSize;
    }
}

class UniformMutation extends Mutation{
    // Discuss if float is correct, or it should be double or something else
    public void mutate(Individual individual, int index){
        double[] oldgenes = individual.genome;
        Random r = new Random();
        // Does this include the lower and upperbounds? Should it?

        double random = lowerBound + r.nextDouble() * (upperBound - lowerBound);

        // cloning might not be needed if non-mutated child is removed anyway
        double[] newgenes = oldgenes.clone();
        newgenes[index] = random;
        individual.setGenome(newgenes);
    }
}

class NonUniformMutation extends Mutation{
    public void mutate(Individual individual, int index){
        double[] oldgenes = individual.genome;
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
        individual.setGenome(newgenes);
    }
}
class SingleNonUniformMutation extends Mutation{
    public void mutate(Individual individual, int index){
        double[] oldgenes = individual.genome;
        double sigma = individual.sigma;
        Random r = new Random();
        double tau = 1/Math.sqrt(2*size);
        double gamma = Math.exp(tau*r.nextGaussian());
        sigma = sigma*gamma;
        // set sigma as new sigma for the individual
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
        individual.setGenome(newgenes);
        individual.setSigma(sigma);
    }
}

class NStepNonUniformMutation extends Mutation{
    public void mutate(Individual individual, int index){
        double[] oldgenes = individual.genome;
        double sigma = individual.sigmas[index];
        Random r = new Random();
        double tau = 1/Math.sqrt(2*size);
        double gamma = Math.exp(tau*r.nextGaussian());
        sigma = sigma*gamma*Math.exp((1/Math.sqrt(2*Math.sqrt(size)))*r.nextGaussian());
        if(sigma<0.01){
            sigma = 0.01;
        }

        // set sigma as new sigma for the individual for gene i

        double gaussianValue = r.nextGaussian()*sigma;

        double[] newgenes = oldgenes.clone();
        newgenes[index] += gaussianValue;
        // new value must not extend bounds
        if(newgenes[index] < lowerBound){
            newgenes[index] = lowerBound;
        }
        else if(newgenes[index] > upperBound){
            newgenes[index] = upperBound;
        }
        individual.setGenome(newgenes);
        individual.setSigmas(sigma, index);
    }
}