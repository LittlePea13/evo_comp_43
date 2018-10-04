
import java.util.Random;

public class Individual {
    double[] genome = new double[10];
    double fitness;
    double sigma;
    double[] sigmas = new double[10];

    public Individual() {
        int leftLimit = -5;
        int rightLimit = 5;
        Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                genome[i] = leftLimit + rand.nextDouble() * (rightLimit - leftLimit);
            }
            for (int i = 0; i < 10; i++) {
                sigmas[i] = 2;
            }
            sigma = 1;
    }
    public void setGenome(double[] genome){
        this.genome = genome;
    }
    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public void setSigma(double sigma){
        this.sigma = sigma;
    }

    public void setSigmas(double sigma, int index){
        this.sigmas[index] = sigma;
    }

    public void setAllSigmas(double[] sigmas1, double[] sigmas2){
        for (int i = 0; i < 10; i++) {
            sigmas[i] = (sigmas1[i]+sigmas2[i])/2;
        }
    }

    public double getFitness(){
        return fitness;
    }
}

