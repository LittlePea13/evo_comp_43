
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
            sigma = 2;    
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
        if(sigma<3){
            this.sigmas[index] = sigma;
        } else {
            this.sigmas[index] = 2;
        }
    }

    public void setAllSigmas(double[] sigmas){
        this.sigmas = sigmas;
    }

    public double getFitness(){
        return fitness;
    }
}

