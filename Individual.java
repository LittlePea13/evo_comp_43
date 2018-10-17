
import java.util.Random;
//import org.apache.commons.math3.distribution.NormalDistribution;

public class Individual {
    double[] genome = new double[10];
    double fitness;
    double sigma;
    double[] sigmas = new double[10];
    double[] alphas = new double[10*9/2];;
    boolean evaluated = false;
    double leftLimit = -0.5;
    double rightLimit = 1.5;
    public Individual[] parents = new Individual[4];

    public Individual(double startingSigma, Random rand) {
       //double leftLimit = -5;
       //double rightLimit = 5;

       //NormalDistribution dist = new NormalDistribution(0,2.5);
            for (int i = 0; i < 10; i++) {
                //this.genome[i] = dist.sample();
                //this.genome[i] = rand.nextGaussian()*2.5;
                this.genome[i] = leftLimit + rand.nextDouble() * (rightLimit - leftLimit);
            }
            for (int i = 0; i < 10; i++) {
                this.sigmas[i] = startingSigma*(rand.nextGaussian());
            }
            for (int i = 0; i < 10*(9)/2; i++) {
                this.alphas[i] = 0.0873*rand.nextGaussian();
            }
            this.sigma = startingSigma*(rand.nextGaussian());
    }
    public Individual(double startingSigma, Random rand, double leftLimit, double rightLimit) {
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
 
        // NormalDistribution dist = new NormalDistribution(0, rand.nextDouble() * 0.5);
             for (int i = 0; i < 10; i++) {
                 //this.genome[i] = dist.sample();
                 this.genome[i] = leftLimit + rand.nextDouble() * (rightLimit - leftLimit);
             }
             for (int i = 0; i < 10; i++) {
                 this.sigmas[i] = startingSigma*(rand.nextGaussian());
             }
             for (int i = 0; i < 10*(9)/2; i++) {
                 this.alphas[i] = 0.0873*rand.nextGaussian();
             }
             this.sigma = startingSigma*(rand.nextGaussian());
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
    public void setAlphas(double alpha, int index){
        this.alphas[index] = alpha;
    }
    public void setAlphas(double[] alphas){
        this.alphas = alphas;
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
    public Individual generateOpposite(double startingSigma, Random rnd){
        Individual op_ind = new Individual(startingSigma, rnd);
        double[] opposite_genome = new double[10];
        for (int i = 0; i < 10; i++) {
            double gene = this.genome[i];
            opposite_genome[i] = this.leftLimit + this.rightLimit - gene;
        }
        op_ind.setGenome(opposite_genome);
        return op_ind;
    }
}

