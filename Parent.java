import java.util.Random;
import java.util.Arrays;
public class Parents
{
    int size = 100;
    int dim = 10;
    double[][] population;

    public Parents(int population_size, int individual_dim){
        size = population_size;
        dim = individual_dim;
        population = new double[population_size][individual_dim];

        for(int i = 0; i < population_size; i++) {
            population[i] = newParent(individual_dim);
        }
    }

    public double[] newParent(int size){
        Random rnd_ = new Random();
        double maxX = 5.0f;
        double minX = -5.0f;
        double[] parent = new double[size];
        for (int i = 0; i < parent.length; i++) {
        parent[i] = rnd_.nextDouble() * (maxX - minX) + minX;
        }
        return(parent);
     }
}