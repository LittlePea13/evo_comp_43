import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class player43 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player43()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
    public int rouletteSelection(double[] weight) 
    {
       // calculate the total weight
        double weight_sum = 0;
        for(int i=0; i<weight.length; i++) {
            weight_sum += weight[i];
        }
        // get a random value
        double value = randUniformPositive() * weight_sum;	
        // locate the random value based on the weights
        for(int i=0; i<weight.length; i++) {		
            value -= weight[i];		
            if(value < 0) return i;
        }
        // when rounding errors occur, we return the last item's index 
        return weight.length - 1;
    }

    // Returns a uniformly distributed double value between 0.0 and 1.0
    public double randUniformPositive() {
        // easiest implementation
        return new Random().nextDouble();
    }
        
    
	public void run()
	{
		// Run your algorithm here

        // Currently this is just a toy algorithm, with values, crossover and mutation chosen arbitrarily
        
        int evals = 0;
        // init population
        int populationSize = 30;
        ArrayList<Individual> population = new ArrayList<>();
        for(int i=0; i<populationSize; i++){
            Individual ind = new Individual();
            population.add(ind);
        }

        for(Individual ind : population){
            ind.fitness = (double) evaluation_.evaluate(ind.genome);
            evals++;
        }

        evaluations_limit_ = evaluations_limit_ - populationSize;
        // - populationSize to make sure it doesn't go over limit during while loop
        while(evals<evaluations_limit_){
            // calculate fitness

            // sort population based on fitness
            // TODO: check if correct.
            Collections.sort(population,
                    Comparator.comparingDouble(Individual::getFitness).reversed());

            // select best two parents
            double best_fitness = population.get(0).fitness;
            double second_best = population.get(1).fitness;

           System.out.println(best_fitness + " " + second_best);

            double[] parent1 = population.get(0).genome;
            double[] parent2 = population.get(1).genome;

            // Apply crossover / mutation operators
            SimpleArithRecombination crossover = new SimpleArithRecombination();
            UniformMutation mutation = new UniformMutation();
            ArrayList<Individual> children = new ArrayList<>();

//            System.out.println("parents:");
//            System.out.println(Arrays.toString(parent1));
//            System.out.println(Arrays.toString(parent2));
//            System.out.println("Children");
            Random r = new Random();
            for(int i=0; i<populationSize; i++) {
                int k = r.nextInt(10);
                Individual child = new Individual();
                double[] child_genome;
                if(i%2 ==0) {
                    child_genome = crossover.recombine(parent1, parent2, k);
                }
                else{
                    child_genome = crossover.recombine(parent2, parent1, k);
                }

                // mutate with prob
                if (r.nextFloat() < 0.3) {
                    child_genome = mutation.mutate(child_genome, 2);
                }
                child.setGenome(child_genome);
                Double fitness = (double) evaluation_.evaluate(child_genome);
                evals++;
                child.setFitness(fitness);
                children.add(child);
            }
            //double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            // Double fitness = (double) evaluation_.evaluate(child);
            // Select survivors
            ArrayList<Individual> possibleSurvivors = new ArrayList<>();
            possibleSurvivors.addAll(population);
            possibleSurvivors.addAll(children);

            Collections.sort(possibleSurvivors,
                    Comparator.comparingDouble(Individual::getFitness).reversed());

            population = new ArrayList<>(possibleSurvivors.subList(0, populationSize));
        }

	}
}
