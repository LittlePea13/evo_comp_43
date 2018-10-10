import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        boolean isMultimodal
                = Boolean.parseBoolean(props.getProperty("Multimodal"));
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
        return rnd_.nextDouble();
    }

    public ArrayList<Individual> tournament(ArrayList<Individual> population,
                              double naturalFitnessScores,
                              int selectionSize)
    {
        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            // Pick two candidates at random.
            Individual candidate1 = population.get(rnd_.nextInt(population.size()));
            Individual candidate2 = population.get(rnd_.nextInt(population.size()));

            // Use a random value to decide wether to select the fitter individual or the weaker one.
            double selectFitter = randUniformPositive();
            if (selectFitter > naturalFitnessScores)
            {
                // Select the fitter candidate.
                selection.add(candidate2.fitness > candidate1.fitness
                              ? candidate2
                              : candidate1);
            }
            else
            {
                // Select the less fit candidate.
                selection.add(candidate2.fitness > candidate1.fitness
                              ? candidate1
                              : candidate2);
            }
        }
        return selection;
    }


    public ArrayList<Individual> StochasticUniversalSamplingFitness(ArrayList<Individual> population,
                              int selectionSize)
    {
        // Calculate the sum of all fitness values.
        double aggregateFitness = 0;
        for (Individual candidate : population)
        {
            aggregateFitness += candidate.fitness; //should change this with function result?
        }

        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        // Pick a random offset between 0 and 1 as the starting point for selection.
        double startOffset = rnd_.nextDouble();
        double cumulativeExpectation = 0;
        int index = 0;
        for (Individual candidate : population)
        {
            // Calculate the number of times this candidate is expected to
            // be selected on average and add it to the cumulative total
            // of expected frequencies.
            cumulativeExpectation += candidate.fitness/ aggregateFitness * selectionSize;

            // If f is the expected frequency, the candidate will be selected at
            // least as often as floor(f) and at most as often as ceil(f). The
            // actual count depends on the random starting offset.
            while (cumulativeExpectation > startOffset + index)
            {
                selection.add(candidate);
                index++;
            }
        }
        return selection;
    }

    public ArrayList<Individual> StochasticUniversalSampling(ArrayList<Individual> population,
                              int selectionSize)
    {
        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        // Pick a random offset between 0 and 1 as the starting point for selection.
        double startOffset = rnd_.nextDouble();
        double cumulativeExpectation = 0;
        int index = 0;
        int rank = 0;
        int populationSize = population.size();
        for (Individual candidate : population)
        {
            // Calculate the number of times this candidate is expected to
            // be selected on average and add it to the cumulative total
            // of expected frequencies.
            cumulativeExpectation += LinearRankInd(rank,populationSize, 1.5) * selectionSize;

            // If f is the expected frequency, the candidate will be selected at
            // least as often as floor(f) and at most as often as ceil(f). The
            // actual count depends on the random starting offset.
            while (cumulativeExpectation > index)
            {
                selection.add(candidate);
                index++;
            }
            rank++;
        }
        return selection;
    }

    public ArrayList<Individual> RouletteSampling(ArrayList<Individual> population,
                              int selectionSize)
    {
        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        // Calculate the weights for whole population
        double[] weights = LinearRankPop(population.size(), 1.5);
        for (int i = 0; i < selectionSize; i++)
        {
            selection.add(population.get(rouletteSelection(weights)));
        }
        return selection;
    }

	public double[] LinearRankPop(int populationSize, double s){
      double[] p_linrank = new double[populationSize];
      for(int i=0; i<populationSize; i++){
        double prob = LinearRankInd(i, populationSize, s);
        p_linrank[i] = prob;
      }
      return p_linrank;
    }

	public double LinearRankInd(int rank, int populationSize, double s){
		return (2 - s)/(populationSize) + 2*rank*(s - 1)/(populationSize*(populationSize - 1));
	}



	public void run()
	{
	    // init properties
        int evals = 0;
        int populationSize = 15;
        int childrenFactor = 7;
        WholeArithRecombination crossover = new WholeArithRecombination();
        SingleNonUniformMutation mutation = new SingleNonUniformMutation();
        // init population

        ArrayList<Individual> population = new ArrayList<>();
        for(int i=0; i<populationSize; i++){
            Individual ind = new Individual();
            population.add(ind);
        }


        for(Individual ind : population){
            ind.fitness = (double) evaluation_.evaluate(ind.genome);
            evals++;
        }

        evaluations_limit_ = evaluations_limit_ - populationSize*childrenFactor;
        // - populationSize to make sure it doesn't go over limit during while loop
        int runs = 0;
        while(evals<evaluations_limit_){
            runs++;
            // parent selection

            // sort population based on fitness
            Collections.sort(population,
                    Comparator.comparingDouble(Individual::getFitness));
            ArrayList<Individual> parents = StochasticUniversalSampling(population, populationSize*childrenFactor);
            // sort parents based on fitness
            Collections.sort(parents,
                    Comparator.comparingDouble(Individual::getFitness));
            double[] weights = LinearRankPop(parents.size(), 1.5);
            // Apply crossover / mutation operators

            ArrayList<Individual> children = new ArrayList<>();
            for(int i=0; i<populationSize*childrenFactor; i++) {
                int k = rnd_.nextInt(10);
                Individual child = new Individual();
                double[] child_genome;
                double[] parent1 = parents.get(i).genome;
                //double[] parent2 = parents.get(rouletteSelection(weights)).genome;
                int j = rnd_.nextInt(populationSize*childrenFactor);
//                int j = i+1;
//                if(j==populationSize*childrenFactor){
//                   j = 0;
//                }
                double[] parent2 = parents.get(j).genome;
                if(i%2 ==0) {
                    child_genome = crossover.recombine(parent1, parent2, k, rnd_);
                }
                else{
                    child_genome = crossover.recombine(parent2, parent1, k, rnd_);
                }
                child.setGenome(child_genome);
                child.setAllSigmas(parents.get(i).sigmas, parents.get(j).sigmas);
                child.setSigma(parents.get(i).sigma);
                if(evals==evaluations_limit_/6){
                    System.out.println("evaluations_limit_");
                    double sigma = 1;
                    child.setSigma(sigma);
                }
                if(evals==evaluations_limit_/5){
                    System.out.println("evaluations_limit_2");
                    double sigma = 0.5;
                    child.setSigma(sigma);
                }
                if(evals==evaluations_limit_/4){
                    System.out.println("evaluations_limit_3");
                    double sigma = 0.01;
                    child.setSigma(sigma);
                }
                if(evals==evaluations_limit_/3){
                    System.out.println("evaluations_limit_3");
                    double sigma = 0.001;
                    child.setSigma(sigma);
                }
                if(evals==evaluations_limit_/2){
                    System.out.println("evaluations_limit_3");
                    double sigma = 0.00001;
                    child.setSigma(sigma);
                }
                // mutate with prob
                if (rnd_.nextFloat() < 0.1) {
                    int l = rnd_.nextInt(10);
                    /*System.out.println("fitness before");
                    System.out.println(parents.get(i).fitness);
                    System.out.println("before");
                    System.out.println(Arrays.toString(child.sigmas));*/
                    //System.out.println(child.sigma);
                    mutation.mutate(child, l, rnd_);
                    //System.out.println(child.sigma);
                    //System.out.println(Arrays.toString(child.sigmas));
                }
                Double fitness = (double) evaluation_.evaluate(child.genome);
                evals++;
                child.setFitness(fitness);
                //System.out.println(fitness);
                children.add(child);
            }

            ArrayList<Individual> possibleSurvivors = new ArrayList<>();
            //possibleSurvivors.addAll(population);
            possibleSurvivors.addAll(children);

            Collections.sort(possibleSurvivors,
                    Comparator.comparingDouble(Individual::getFitness).reversed());

            population = new ArrayList<>(possibleSurvivors.subList(0, populationSize));

            
            /*
            Collections.sort(children,
                Comparator.comparingDouble(Individual::getFitness));

            population = tournament(children, 0.3,populationSize);
*/
        }
	}
}
