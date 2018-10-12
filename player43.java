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
import java.math.BigDecimal;
import java.math.RoundingMode;

public class player43 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private long theseed;

	public player43()
	{
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
        rnd_.setSeed(seed);
        this.theseed = seed;
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
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

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

    // Implementing Fisher–Yates shuffle
    private void shuffleArray(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd_.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void splitPopulation(int race_limit){

        int[] population_races = new int[population.size()];
        int n =0;
        for (int i= 0 ; i < population.size()/race_limit-1;i++){
            for (int j = 0 ; j < race_limit;j++){
                n++;
                population_races[n]=j;

            }
        }

        shuffleArray(population_races);

        n=0;
        for (Individual ind: population) {
            int[] r = {population_races[n]};
            ind.setRace(r);
            n++;
        }
    }
    private double punish(Individual ind, double rate){
        double score = 0 ;
        double[] genome = ind.getGenome();
        for (int i = 0 ; i < 10;i++){
            if (genome[i] > 5 || genome[i] < -5)  score += rate;
        }

	    return  score;
    }
    private double evaluate(){
        double best_fitness = 0;
        for (Individual ind : population) {
            // Check fitness of unknown fuction
            if (!ind.isEvaluated() && evals < evaluations_limit_ ) {
                double fitness = (double) evaluation_.evaluate(ind.getGenome());
//                if (fitness == 10.0) {System.out.println("Score: 10.0"); System.exit(10);}
                fitness += punish(ind,-1);
                //System.out.println(fitness);
                ind.setFitness(fitness);
                ind.setEvaluated(true);

                if (fitness > best_fitness) {
                    best_fitness = fitness;
                    bestGenome = ind.getGenome();

                }
                evals++;
            }
        }
    return best_fitness;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static double nextCauchy(Random rnd) {
        double p = rnd.nextDouble();

                while (p == 0. || p == 1.) {
                p = rnd.nextDouble();
            }

        return Math.tan(Math.PI * (rnd.nextDouble() - .5));
    }

    /*private boolean isRelated(int parents_position1 , int parents_position2){
        boolean related = true;
        if (population.get(parents_position1).parents[0] == null || population.get(parents_position2).parents[0] == null ) return related;
        if( ! (Arrays.deepEquals(population.get(parents_position1).parents,population.get(parents_position2).parents)) && !(Arrays.deepEquals(population.get(parents_position2).parents,population.get(parents_position1).parents))) return related;
        return false;
    }*/

    private boolean isRelated(Individual[] parent1,Individual[] parent2){
        boolean related = false;
        if (parent1[0] == null || parent2[0] == null ) return related;
        if( Arrays.deepEquals(parent1,parent2) ) related = true;
        return related;
    }

    private boolean isRelated(Individual[] parent1,Individual parent2){
        boolean related = false;
        if (parent1[0] == null || parent2 == null ) return related;
        if (parent1[0] == parent2 || parent1[1] == parent2 || parent1[2] == parent2 || parent1[3] == parent2 ) related = true;
        return related;
    }

    private boolean isRelated(int parents_position1 , int parents_position2){
        boolean related = true;
        if (
                !isRelated(population.get(parents_position1).parents, population.get(parents_position2).parents) &&
                        !isRelated(population.get(parents_position2).parents, population.get(parents_position1).parents)
                ) related = false;
        return related;
    }


    private ArrayList<Individual> tournamentSelection(int number_of_parents, int slice, boolean insest){
        int[] parents_positions = new int[number_of_parents];
        ArrayList<Individual> parents = new ArrayList<>();
        int parent_index = 0;
        int k =0;
        while( k < number_of_parents) {
            double[] fitnesses = new double[slice];
            int[] indexes = new int[slice];
            for (int i = 0; i < slice; i++) {
                indexes[i] = rnd_.nextInt(population.size());
                fitnesses[i] = population.get(indexes[i]).getFitness();
            }
            double best_fit = 0;
            for (int i = 0; i < slice; i++) {
                if (fitnesses[i] > best_fit && fitnesses[i] > 0) {
                    best_fit = fitnesses[i];
                    parent_index = indexes[i];
                }
            }
            boolean unique = true;
            for (int  i = 0 ; i < number_of_parents;i++){
                if(parents_positions[i] == parent_index) unique = false;
                if(isRelated(parents_positions[i],parent_index) && !insest) unique = false;
                //revisar isRelated
//                if(isRelated(parents_positions[i],parent_index) ) {System.out.print("*INSEST*" + insest_count);insest_count++;}
            }
            if (unique) {
                parents.add(population.get(parent_index));
                parents_positions[k] = parent_index;
                k++;
            }
        }
        return parents;
    }

    private ArrayList<Individual> tournamentSelection(int number_of_parents,int slice,int race,boolean insest){
        int[] parents_positions = new int[number_of_parents];
        ArrayList<Individual> parents = new ArrayList<>();
        int parent_index = 0;
        int k =0;
        while( k < number_of_parents) {
            double[] fitnesses = new double[slice];
            int[] indexes = new int[slice];
            for (int i = 0; i < slice; i++) {
                indexes[i] = rnd_.nextInt(population.size());
                fitnesses[i] = population.get(indexes[i]).getFitness();
            }
            /*
            System.out.println("index_after");
            System.out.println(Arrays.toString(indexes));
            System.out.println("fitness_after");
            System.out.println(Arrays.toString(fitnesses));
*/
            double best_fit = 0;
            for (int i = 0; i < slice; i++) {
                if (fitnesses[i] > best_fit && fitnesses[i] > 0) {
                    best_fit = fitnesses[i];
                    parent_index = indexes[i];
                }
            }

            boolean unique = true;
            for (int  i = 0 ; i < number_of_parents;i++){
               /* System.out.println("stuff");

                System.out.println(parents_positions[i]);
                System.out.println(parent_index);
                System.out.println(population.get(parent_index).getRace()[0]);
                System.out.println(race);
                System.out.println(parents_positions[i] == parent_index || population.get(parent_index).getRace()[0] != race);
                System.out.println(isRelated(parents_positions[i],parent_index) && !insest);
*/
                if(parents_positions[i] == parent_index || population.get(parent_index).getRace()[0] != race) unique = false;
                if(isRelated(parents_positions[i],parent_index) && !insest) unique = false;
//                if(isRelated(parents_positions[i],parent_index) ) {System.out.print("*INSEST*"+insest_count);insest_count++;}
            }

            if (unique) {
                parents.add(population.get(parent_index));
                parents_positions[k] = parent_index;
                k++;
            }
        }
        //System.out.println(Arrays.toString(parents_positions));
        return parents;
    }

    private int populationSize = 1003;
    private int childrenFactor = 40;
    private double mutationRate = 0.01;
    private double startingSigma = 3.0;
    private double mutation_step = 0.000000000000001;
    int tournamentSelection_slice = 30;
    private int race_limit = 6;


    private double[] bestGenome = new double[10];
    private int evals = 0;
    private ArrayList<Individual> population = new ArrayList<>();
    private ArrayList<Individual> children = new ArrayList<>();
    private double last_fitness=0;
    private int same_fitness = 0;
    int generations = 0;

    boolean isMultimodal ;
    boolean hasStructure ;
    boolean isSeparable ;


	public void run()
	{
        // Run your algorithm here
        // init population
        // Currently this is just a toy algorithm, with values, crossover and mutation chosen arbitrarily
        /*
        populationSize = Integer.parseInt(System.getProperty("ps"));
        childrenFactor = Integer.parseInt(System.getProperty("cf"));
        startingSigma = Double.parseDouble(System.getProperty("startingSigma"));
        mutationRate = Double.parseDouble(System.getProperty("mutationRate"));
        mutation_step = Double.parseDouble(System.getProperty("mutation_step"));
        tournamentSelection_slice = Integer.parseInt(System.getProperty("slice"));
        race_limit = Integer.parseInt(System.getProperty("race"));
        */

        System.out.println(populationSize + " , " + childrenFactor + " , " + startingSigma + " , " + mutationRate + " , " + mutation_step + " , " + tournamentSelection_slice + " , " + race_limit + " , "+ theseed );

        for(int i=0; i<populationSize; i++){
            Individual ind = new Individual(startingSigma);
            population.add(ind);
        }
        double best_fitness = evaluate();
        splitPopulation(race_limit);
        //System.out.println("best_fitness");
        //System.out.println(best_fitness);

        evaluations_limit_ = evaluations_limit_ - populationSize;
        // - populationSize to make sure it doesn't go over limit during while loop
        int runs = 0;
        while (evals < evaluations_limit_) {
            double best_inGen = evaluate();
            runs++;
            if (round(best_fitness,3) == last_fitness) same_fitness++;
            last_fitness = round(best_fitness,3);
            int counters[] = new int[10];
            //            int related_count = 0;
                        for (Individual g : population){
            //                for(Genome gg : population){
            //                    if (isRelated(g.parents,gg.parents))  related_count++;
            //                }
                            for(int i = 0 ; i < 10; i++){
                                if (g.getRace()[0] == i ) counters[i]++;
            
                            }
                        }
            // sort population based on fitness
            Collections.sort(population,
                    Comparator.comparingDouble(Individual::getFitness));

            population.subList(0,childrenFactor).clear();

            int race_ = 0;
            int[] parents;
            if (generations % 1000 == 0) {
                parents = tournament2Selection(childrenFactor, tournamentSelection_slice,true);
                same_fitness = 0;
            } else {
                parents = tournament2Selection(childrenFactor, tournamentSelection_slice, race_,true);
                if(race_ > race_limit)  race_ = 0;
                else race_++;
            }


            WholeArithRecombination crossover = new WholeArithRecombination();
            NStepNonUniformMutation mutation = new NStepNonUniformMutation();
            RouletteSampling sampling = new RouletteSampling();

            mutation.size = populationSize;

            int i = 0;
            while (i < childrenFactor) {
                int[] parents_positions = {parents[i + 0], parents[i + 1], parents[i + 2], parents[i + 3]};

                differentialCrossover(population.get(parents_positions[0]), 
                population.get(parents_positions[1]), 
                population.get(parents_positions[2]), 
                population.get(parents_positions[3]), 
                0.9);
                //child_genome = crossover.recombine(parent1, parent2, k);
                //child_genome2 = crossover.recombine(parent2, parent1, k);
                i += 4;
            }
            for (Individual ind : population) {
                if (rnd_.nextDouble() < mutationRate && !ind.isEvaluated()) {
                    for (int j =0;j < 10;j++){
                        double[] genome = ind.getGenome();
                        if (isMultimodal){
                            genome[j] += nextCauchy(rnd_) * mutation_step ;
                            ind.setGenome(genome);
                        }
                        else {
                            mutation.mutate(ind, j);
                        }
                    }
                    ind.setEvaluated(false);
                }
            }
/*
            Collections.sort(population,
                    Comparator.comparingDouble(Individual::getFitness).reversed());

            population = new ArrayList<>(population.subList(0, populationSize));*/
            generations ++;
            /*
            Collections.sort(children,
                Comparator.comparingDouble(Individual::getFitness));

            population = tournament(children, 0.3,populationSize);
*/
        }
        //System.out.println(runs);
    }
    private double[][] differentialCrossover(double[] parent0_array , double[] parent1_array,double scaling_factor){

        double[][] child_array = new double[2][10];

        for (int i = 0; i < 10; i++) {
            if (rnd_.nextBoolean()) {
                child_array[0][i] = parent0_array[i];
                child_array[1][i] = parent1_array[i];
            }
            else {
                child_array[0][i] = parent1_array[i];
                child_array[1][i] = parent0_array[i];

            }

            child_array[0][i] = child_array[0][i] + ((parent0_array[i] - parent1_array[i]) * scaling_factor);
            child_array[1][i] = child_array[1][i] + ((parent1_array[i] - parent0_array[i]) * scaling_factor);
        }
        return child_array;
    }
    private double[][] differentialCrossover(double[] parent0_array , double[] parent1_array, double[] parent2_array, double[] parent3_array,double scaling_factor){
        double[][] child_array = new double[4][10];

        for (int i = 0; i < 10; i++) {
            if (rnd_.nextBoolean()) {
                child_array[0][i] = parent0_array[i];
                child_array[1][i] = parent1_array[i];
                child_array[2][i] = parent0_array[i];
                child_array[3][i] = parent1_array[i];
            }
            else {
                child_array[0][i] = parent1_array[i];
                child_array[1][i] = parent0_array[i];
                child_array[2][i] = parent1_array[i];
                child_array[3][i] = parent0_array[i];

            }

            child_array[0][i] = child_array[0][i] + ((parent2_array[i] - parent3_array[i]) * scaling_factor);
            child_array[1][i] = child_array[1][i] + ((parent3_array[i] - parent2_array[i]) * scaling_factor);
            child_array[2][i] = child_array[2][i] + ((parent2_array[i] - parent3_array[i]) * scaling_factor);
            child_array[3][i] = child_array[3][i] + ((parent3_array[i] - parent2_array[i]) * scaling_factor);
        }
        return child_array;
    }
    private void differentialCrossover(Individual parent0 , Individual parent1,Individual parent2, Individual parent3,double scaling_factor){


        double[] parent0_array = parent0.getGenome();
        double[] parent1_array = parent1.getGenome();
        double[] parent2_array = parent2.getGenome();
        double[] parent3_array = parent3.getGenome();

        double[] child0_array = new double[10];
        double[] child1_array = new double[10];
        double[] child2_array = new double[10];
        double[] child3_array = new double[10];

        for (int i = 0; i < 10; i++) {
            if (rnd_.nextBoolean()) {
                child0_array[i] = parent0_array[i];
                child1_array[i] = parent1_array[i];
                child2_array[i] = parent0_array[i];
                child3_array[i] = parent1_array[i];
            }
            else {
                child0_array[i] = parent1_array[i];
                child1_array[i] = parent0_array[i];
                child2_array[i] = parent1_array[i];
                child3_array[i] = parent0_array[i];

            }

            child0_array[i] = child0_array[i] + ((parent2_array[i] - parent3_array[i]) * scaling_factor);
            child1_array[i] = child1_array[i] + ((parent3_array[i] - parent2_array[i]) * scaling_factor);
            child2_array[i] = child2_array[i] + ((parent2_array[i] - parent3_array[i]) * scaling_factor);
            child3_array[i] = child3_array[i] + ((parent3_array[i] - parent2_array[i]) * scaling_factor);
        }

        Individual child0 = new Individual(0);
        Individual child1 = new Individual(0);
        Individual child2 = new Individual(0);
        Individual child3 = new Individual(0);

        child0.setGenome(child0_array);
        child1.setGenome(child1_array);
        child2.setGenome(child2_array);
        child3.setGenome(child3_array);

        child0.setEvaluated(false);
        child1.setEvaluated(false);
        child2.setEvaluated(false);
        child3.setEvaluated(false);

        child0.setRace(parent0.getRace());
        child1.setRace(parent1.getRace());
        child2.setRace(parent2.getRace());
        child3.setRace(parent3.getRace());

        child0.parents[0]= parent0;
        child0.parents[1]= parent1;
        child0.parents[2]= parent2;
        child0.parents[3]= parent3;

        child1.parents[0]= parent0;
        child1.parents[1]= parent1;
        child1.parents[2]= parent2;
        child1.parents[3]= parent3;

        child2.parents[0]= parent0;
        child2.parents[1]= parent1;
        child2.parents[2]= parent2;
        child2.parents[3]= parent3;

        child3.parents[0]= parent0;
        child3.parents[1]= parent1;
        child3.parents[2]= parent2;
        child3.parents[3]= parent3;



        population.add(child0);
        population.add(child1);
        population.add(child2);
        population.add(child3);
       // System.out.println(Arrays.toString(child0.getAlleles()));

    }
    private int[] tournament2Selection(int number_of_parents,int slice,boolean insest){
        int[] parents_positions = new int[number_of_parents];

        int parent_index = 0;
        int k =0;
        while( k < number_of_parents) {
            double[] fitnesses = new double[slice];
            int[] indexes = new int[slice];
            for (int i = 0; i < slice; i++) {
                indexes[i] = randInt(0, population.size());
                fitnesses[i] = population.get(indexes[i]).getFitness();
            }
            double best_fit = 0;
            for (int i = 0; i < slice; i++) {
                if (fitnesses[i] > best_fit && fitnesses[i] > 0) {
                    best_fit = fitnesses[i];
                    parent_index = indexes[i];
                }
            }
            boolean unique = true;
            for (int  i = 0 ; i < number_of_parents;i++){
                if(parents_positions[i] == parent_index) unique = false;
                if(isRelated(parents_positions[i],parent_index) && !insest) unique = false;
            }
            if (unique) {
                parents_positions[k] = parent_index;
                k++;
            }
        }
        return parents_positions;
    }
    private int[] tournament2Selection(int number_of_parents,int slice,int race,boolean insest){
        int[] parents_positions = new int[number_of_parents];

        int parent_index = 0;
        int k =0;
        while( k < number_of_parents) {
            double[] fitnesses = new double[slice];
            int[] indexes = new int[slice];
            for (int i = 0; i < slice; i++) {
                indexes[i] = randInt(0, population.size());
                fitnesses[i] = population.get(indexes[i]).getFitness();
            }
            double best_fit = 0;
            for (int i = 0; i < slice; i++) {
                if (fitnesses[i] > best_fit && fitnesses[i] > 0) {
                    best_fit = fitnesses[i];
                    parent_index = indexes[i];
                }
            }

            boolean unique = true;
            for (int  i = 0 ; i < number_of_parents;i++){
                if(parents_positions[i] == parent_index || population.get(parent_index).getRace()[0] != race) unique = false;
                if(isRelated(parents_positions[i],parent_index) && !insest) unique = false;
            }

            if (unique) {
                parents_positions[k] = parent_index;
                k++;
            }
        }
        return parents_positions;
    }
    private int randInt(int low, int  high){
        int result = rnd_.nextInt(high-low) + low;
        return result;
    }
}
