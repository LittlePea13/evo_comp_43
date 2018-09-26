
import java.util.Random;

public class Individual {
    double[] genome = new double[10];
    double fitness;
    double sigma;
    double sigmas;

    public Individual() {
        int leftLimit = -5;
        int rightLimit = 5;
        Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                genome[i] = leftLimit + rand.nextDouble() * (rightLimit - leftLimit);
            }
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

    public void setSigmas(double sigmas){
        this.sigmas = sigmas;
    }


    public double getFitness(){
        return fitness;
    }
}

