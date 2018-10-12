
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;

public class Individual {
    double[] genome = new double[10];
    double fitness;
    double sigma;
    double[] sigmas = new double[10];
    boolean evaluated = false;
    public Individual[] parents = new Individual[4];
    private int[] race = new int[10];

    public Individual(double startingSigma) {
        double leftLimit = -0.5;
        double rightLimit = 1.5;
        //NormalDistribution dist = new NormalDistribution(0, rand.nextDouble() * 0.5);
        Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                //this.genome[i] = NormalDistribution.sample();
                this.genome[i] = leftLimit + rand.nextDouble() * (rightLimit - leftLimit);
            }
            for (int i = 0; i < 10; i++) {
                this.sigmas[i] = startingSigma*(rand.nextDouble());
            }
            this.sigma = startingSigma*(rand.nextDouble());
        this.race = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
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
    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }
    public boolean isEvaluated() {
        return evaluated;
    }
    public double getFitness(){
        return fitness;
    }
    public double[] getGenome(){
        return genome;
    }
    public int[] getRace() {
        return race;
    }

    public void setRace(int[] race) {
        this.race = race;
    }
}

