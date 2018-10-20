import java.util.Random;
import java.util.Arrays;
import java.lang.Math.*;
import java.math.BigDecimal;


public class Mutation{
    // test function
    public static void main(String[] args){
        Random r = new Random();

        UniformMutation UM = new UniformMutation();
//        UM.setUpperBound(5);
//        UM.setLowerBound(-5);
        Individual ind = new Individual(5, r);
        double[] oldgenes = ind.genome;
        UM.mutate(ind,3, r);
        double[] newgenes = ind.genome;
        System.out.println(Arrays.toString(newgenes));

        NonUniformMutation NUM = new NonUniformMutation();
        NUM.setUpperBound(5);
        NUM.setLowerBound(-5);
        NUM.mutate(ind,3,r);
        double[] newgenes2 = ind.genome;
        System.out.println(Arrays.toString(newgenes2));

        SingleNonUniformMutation NUMP = new SingleNonUniformMutation();
        NUMP.setPopulation(50);
        NUMP.mutate(ind,3,r);
        double[] newgenes3 = ind.genome;
        System.out.println(Arrays.toString(newgenes3));
        System.out.println(ind.sigma);

        NStepNonUniformMutation NSUMP = new NStepNonUniformMutation();
        NSUMP.mutate(ind,3,r);
        double[] newgenes4 = ind.genome;
        System.out.println(Arrays.toString(newgenes4));
        System.out.println(Arrays.toString(ind.sigmas));

    }

    public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for(int i = 0; i < mRRowLength; i++) {         // rows from m1
            for(int j = 0; j < mRColLength; j++) {     // columns from m2
                for(int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }

    public static double nextCauchy(Random rnd) {
        double p = rnd.nextDouble();

                while (p == 0. || p == 1.) {
                p = rnd.nextDouble();
            }

        return Math.tan(Math.PI * (rnd.nextDouble() - .5));
    }

    // Stuff that is true for all mutations
    // lower and upper should be -5 and 5
    public int lowerBound = -5;
    public int upperBound = 5;
    public int size = 50;
    public double mutation_step = 0.000001;



    public void SetMutation_step(double mutation){
        mutation_step = mutation;
    }

    public void setLowerBound(int numLow){
        lowerBound = numLow;
    }

    public void setUpperBound(int numHigh){
        upperBound = numHigh;
    }

    public void setPopulation(int numSize){
        size = numSize;
    }

    public void mutate(Individual individual, int index, Random rnd){
        Random r = rnd;
    }
}

class CauchyMutation extends Mutation{
    // Discuss if float is correct, or it should be double or something else
    public void mutate(Individual individual, int index, Random r){
        double[] genome = individual.getGenome();
        // Does this include the lower and upperbounds? Should it?

        genome[index] += nextCauchy(r) * mutation_step;

        individual.setGenome(genome);
    }
}

class UniformMutation extends Mutation{
    // Discuss if float is correct, or it should be double or something else
    public void mutate(Individual individual, int index, Random r){
        double[] oldgenes = individual.genome;
        // Does this include the lower and upperbounds? Should it?

        double random = lowerBound + r.nextDouble() * (upperBound - lowerBound);

        // cloning might not be needed if non-mutated child is removed anyway
        double[] newgenes = oldgenes.clone();
        newgenes[index] = random;
        individual.setGenome(newgenes);
    }
}

class NonUniformMutation extends Mutation{
    public void mutate(Individual individual, int index, Random r){
        double[] oldgenes = individual.genome;
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
    public void mutate(Individual individual, int index, Random r){
        double[] oldgenes = individual.genome;
        double sigma = individual.sigma;
        double tau = 1/Math.sqrt(2*10);
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
    public void mutate(Individual individual, int index, Random r){
        double[] oldgenes = individual.genome;
        double sigma = individual.sigmas[index];
        double tau = 1/Math.sqrt(2*10);
        double gamma = Math.exp(tau*r.nextGaussian());
        sigma = sigma*gamma*Math.exp((1/Math.sqrt(2*Math.sqrt(10)))*r.nextGaussian());

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
class CorrelatedMutation extends Mutation{
    public void mutate(Individual individual, int index, Random r){
        double[] oldgenes = individual.genome;
        double[] sigmas = individual.sigmas;
        double[] alphas = individual.alphas;

        int aplhaind = r.nextInt(45);
        double tau = 1/Math.sqrt(2*10);
        double gamma = Math.exp(tau*r.nextGaussian());
        for(int i =0; i<10; i++){
            sigmas[i] = sigmas[i]*gamma*Math.exp((1/Math.sqrt(2*Math.sqrt(10)))*r.nextGaussian());
            if(sigmas[i] < 0.0001){
                sigmas[i] = 0.0001;
            }
        }

        for(int i =0; i < 45; i++){
            alphas[i] = alphas[i] + 0.0873*r.nextGaussian();
            if(alphas[i]>Math.PI || alphas[i] <- Math.PI){
                alphas[i] = alphas[i] - 2* Math.PI * Math.signum(alphas[i]);
            }
        }

        individual.setAllSigmas(sigmas, sigmas);
        individual.setAlphas(alphas);

        double[][] correlationMatrix = new double[10][10];
        double[][][] correlationMatrixes = new double[45][10][10];
        int k = 0;
        for(int i = 0; i< 10; i++){
            for(int j = 0; j < i; j++){
                double alphaij = alphas[k];
                for(int m = 0; m <10; m++){
                    correlationMatrixes[k][m][m]=1;
                }
                correlationMatrixes[k][i][i]=Math.cos(alphaij);
                correlationMatrixes[k][j][j]=Math.cos(alphaij);

                correlationMatrixes[k][j][i]=-Math.sin(alphaij);
                correlationMatrixes[k][i][j]=Math.sin(alphaij);
                k++;
            }
        }

        double[][] correlatedResult = correlationMatrixes[0];

        for(int n = 1; n< 45; n++){
            correlatedResult = multiplyByMatrix(correlatedResult, correlationMatrixes[n]);
        }

        double[] correlatedSigmas = new double[10];
        double[] gaussianValue = new double[10];


        for (int row = 0; row < 10; row++) {
            double sum = 0;
            for (int column = 0; column < 10; column++) {
                sum += correlatedResult[row][column]
                        * sigmas[column];
            }
            correlatedSigmas[row] = sum;
        }

        individual.setAllSigmas(correlatedSigmas,correlatedSigmas);

        for(int i = 0; i<10; i++){
            gaussianValue[i] = correlatedSigmas[i]*r.nextGaussian();
        }
    /*    System.out.println("new");
        System.out.println(Arrays.toString(alphas));

        System.out.println(Arrays.toString(sigmas));

        System.out.println(Arrays.toString(gaussianValue));

        System.out.println(Arrays.toString(oldgenes));
*/
        double[] newgenes = oldgenes.clone();

        for(int i = 0; i< 10; i++){
            newgenes[i]+=gaussianValue[i];
        }

        individual.setGenome(newgenes);
    }
}