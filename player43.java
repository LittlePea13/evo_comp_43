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
    private long theseed;

	public player43()
	{
        System.setSecurityManager(null);
		rnd_ = new Random();
	}

	public void setSeed(long seed)
	{
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

    private int populationSize = 100;
    private int childrenFactor = 10;
    private double mutationRate = 0.01;
    private double startingSigma = 5.0;
    private double mutation_step = 0.000000000001;
    int tournamentSelection_cut = 1;

    private double[] bestGenome = new double[10];
    private int evals = 0;
    private ArrayList<Individual> population = new ArrayList<>();
    private ArrayList<Individual> children = new ArrayList<>();
    int generations = 0;
    Individual best_Individual;

    boolean isMultimodal ;
    boolean hasStructure ;
    boolean isSeparable ;
    boolean oppositeIndividuals = false;
    boolean adaptativeRandomness = false;
    private long previousTime;


	public void run()
	{

    previousTime = System.currentTimeMillis();
        // Run your algorithm here
        // init population
        // Currently this is just a toy algorithm, with values, crossover and mutation chosen arbitrarily    
    if(!isMultimodal){
        populationSize = 10;
        childrenFactor = 4;
        mutationRate = 0.8;
        startingSigma = 0.1;
        mutation_step = 0.1;
        tournamentSelection_cut = 4;
    } else {
        /*populationSize = 905;
        childrenFactor = 40;
        mutationRate = 0.01;
        startingSigma = 5.0;
        mutation_step = 0.000000000001;
        tournamentSelection_cut = 45;
*/      if(hasStructure){
            populationSize = 800;
            childrenFactor = 32;
            mutationRate = 0.9;
            startingSigma = 1;
            mutation_step = 0.0000000000000001;
            tournamentSelection_cut = 30;
        } else {
            populationSize = 1200;
            childrenFactor = 40;
            mutationRate = 1;
            startingSigma = 0.001;
            mutation_step = 0.00000000000001;
            tournamentSelection_cut = 30;
        }
    }
    if(Boolean.parseBoolean(System.getProperty("bulk"))){
        populationSize = Integer.parseInt(System.getProperty("ps"));
        childrenFactor = Integer.parseInt(System.getProperty("cf"));
        startingSigma = Double.parseDouble(System.getProperty("startingSigma"));
        mutationRate = Double.parseDouble(System.getProperty("mutationRate"));
        mutation_step = Double.parseDouble(System.getProperty("mutation_step"));
        tournamentSelection_cut = Integer.parseInt(System.getProperty("cut"));
    }

      // System.out.println(populationSize + " , " + childrenFactor + " , " + startingSigma + " , " + mutationRate + " , " + mutation_step + " , " + tournamentSelection_cut + " , " + theseed );
       System.out.println("generations" + " , " + "best_fitness" + " , " + "time_elapsed");
        if(oppositeIndividuals){
            for(int i=0; i<populationSize/2; i++){
                Individual ind = new Individual(startingSigma,rnd_, -5, 5);
                Individual op_ind = ind.generateOpposite(startingSigma, rnd_);
                population.add(ind);
                population.add(op_ind);
            }
        } else {
            if(adaptativeRandomness){
                for(int i=0; i<populationSize; i++){
                    Individual ind = new Individual(startingSigma,rnd_, -5, 5);
                    population.add(ind);
                }
                AdaptiveRandomness AR = new AdaptiveRandomness();
                population = AR.GeneratePopulation(population, populationSize, startingSigma, rnd_);
            } else {
                for(int i=0; i<populationSize; i++){
                    Individual ind = new Individual(startingSigma,rnd_);
                    population.add(ind);
                }
            }
        }
       // population = AR.GeneratePopulation(population, populationSize, startingSigma, rnd_);
      /*System.out.println("x1" + "," + "x2" + "," + "x3" + "," + "x4" + "," + "x5" + "," + "x6" + "," +"x7" + "," + "x8" + "," + "x9" + "," + "x10" + "," + "generation");
        for(Individual ind: population){
            System.out.println(ind.genome[0] + "," + ind.genome[1] + "," + ind.genome[2] + "," + ind.genome[3] + "," + ind.genome[4]+ "," + ind.genome[5] + "," +ind.genome[6] + "," + ind.genome[7] + "," + ind.genome[8] + "," + ind.genome[9]+ "," + generations);
        }
*/

        double best_fitness = 0;
        for (Individual ind : population) {
            if (!ind.isEvaluated()) {
                double fitness = (double) evaluation_.evaluate(ind.getGenome());
                double[] genome = ind.getGenome();
                for (int i = 0 ; i < 10;i++){
                    if (genome[i] > 5 || genome[i] < -5)  fitness += -1;
                }
                ind.setFitness(fitness);
                ind.setEvaluated(true);
                if (fitness > best_fitness) {
                    best_Individual = ind;
                    best_fitness = fitness;
                    bestGenome = ind.getGenome();
                }
                evals++;
            }
        }

        double best_overall = best_fitness;
        //System.out.println("best_fitness");
        //System.out.println(best_fitness);

        evaluations_limit_ = evaluations_limit_ - populationSize;
        // - populationSize to make sure it doesn't go over limit during while loop
        int runs = 0;
        int stuck_count = 0;
        while (evals < evaluations_limit_) {
            double best_inGen = 0;
            for (Individual ind : population) {
                if (!ind.isEvaluated()) {
                    double fitness = (double) evaluation_.evaluate(ind.getGenome());
                    double[] genome = ind.getGenome();
                    for (int i = 0 ; i < 10;i++){
                        if (genome[i] > 5 || genome[i] < -5)  fitness += -1;
                    }                    
                    ind.setFitness(fitness);
                    ind.setEvaluated(true);
    
                    if (fitness > best_inGen) {
                        best_Individual = ind;
                        best_inGen = fitness;
                        bestGenome = ind.getGenome();
                    }
                    evals++;
                }
            }
            if(best_inGen < best_overall){
                stuck_count++;
            } else {
                best_overall = best_inGen;
            }
           /* if(generations % 100 == 0){
                System.out.println(best_inGen);
                System.out.println(Arrays.toString(best_Individual.sigmas));
            }*/
            Collections.sort(population,
            Comparator.comparingDouble(Individual::getFitness));
            if(isMultimodal){
                if(hasStructure){
                    if(generations == 600){
                        population.subList(0,populationSize-650).clear();
                        populationSize = 650;
                        childrenFactor = 60;
                    }
                } else {
                    if(generations == 10000){
                        population.subList(0,populationSize-700).clear();
                        populationSize = 700;
                        childrenFactor = 40;
                    }
                }
            }

            runs++;
            // sort population based on fitness

            population.subList(0,childrenFactor).clear();
            RouletteSampling sampling = new RouletteSampling();

            int[] parents = new int[childrenFactor];

            if(!isMultimodal){
                ArrayList<Individual> parents_ind;
                parents_ind = sampling.sample(population, childrenFactor, rnd_);
                int j=0;
                for(Individual ind : parents_ind){
                    parents[j] = population.indexOf(ind);
                    j++;
                }
            } else {
                parents = tournamentInts(childrenFactor, tournamentSelection_cut);
            }

            WholeArithRecombination crossover = new WholeArithRecombination();
            SimpleArithRecombination crossover2 = new SimpleArithRecombination();
            Mutation mutation = null;
            Mutation mutation2 = null;


            if (isMultimodal){
                if(hasStructure){
                    mutation = new CauchyMutation();
                    mutation.SetMutation_step(mutation_step);
                } else {
                    mutation = new CauchyMutation();
                    mutation.SetMutation_step(mutation_step);
                }
            } else {
                mutation = new NStepNonUniformMutation();
                mutation.SetMutation_step(mutation_step);
            }

            int i = 0;
            while (i < childrenFactor) {
                if(!isMultimodal){
                    double[] child1_genome;
                    double[] child2_genome;
                    child1_genome = crossover.recombine(population.get(parents[i]).getGenome(), population.get(parents[i + 1]).getGenome(), rnd_.nextInt(8)+1);
                    child2_genome = crossover.recombine(population.get(parents[i + 1]).getGenome(), population.get(parents[i]).getGenome(), rnd_.nextInt(8)+1);
                    Individual child1 = new Individual(0,rnd_);
                    Individual child2 = new Individual(0, rnd_);
                    child1.setGenome(child1_genome);
                    child1.setEvaluated(false);
                    child1.setSigma(population.get(parents[i]).sigma);
                    child1.setAllSigmas(population.get(parents[i]).sigmas,population.get(parents[i]).sigmas);
                    child1.setAlphas(population.get(parents[i]).alphas);
                    population.add(child1);
                    child2.setGenome(child2_genome);
                    child2.setEvaluated(false);
                    child2.setAlphas(population.get(parents[i+1]).alphas);
                    child2.setSigma(population.get(parents[i + 1]).sigma);
                    child2.setAllSigmas(population.get(parents[i+1]).sigmas,population.get(parents[i+1]).sigmas);
                    population.add(child2);
                    i+=2;
                } else {
                    if(hasStructure){
                        diffCrossover(population.get(parents[i]), 
                        population.get(parents[i + 1]), 
                        population.get(parents[i + 2]), 
                        population.get(parents[i + 3]), 
                        0.7);
                        i += 4;
                    } else {
                        diffCrossover(population.get(parents[i]), 
                        population.get(parents[i + 1]), 
                        population.get(parents[i + 2]), 
                        population.get(parents[i + 3]), 
                        0.8);
                        i += 4;
                    }
                }
            }
            for (Individual ind : population) {
                if (rnd_.nextDouble() < mutationRate && !ind.isEvaluated()) {
                    if(isMultimodal){
                        if(hasStructure){
                            if(generations>1000){
                                for (int j =0;j < 10;j++){
                                    mutation.mutate(ind, j, rnd_);
                                }
                            }
                        } else {
                            //if(generations<3000){
                                for (int j =0;j < 10;j++){
                                    mutation.mutate(ind, j, rnd_);
                                }
                           // }
                        }
                    } else {
                        mutation.mutate(ind, rnd_.nextInt(10), rnd_);
                    }
                    ind.setEvaluated(false);
                }
            }
            generations ++;
/*
            if(generations%20==0 && generations<=200){
                for(Individual ind: population){
                    System.out.println(ind.genome[0] + "," + ind.genome[1] + "," + ind.genome[2] + "," + ind.genome[3] + "," + ind.genome[4]+ "," + ind.genome[5] + "," +ind.genome[6] + "," + ind.genome[7] + "," + ind.genome[8] + "," + ind.genome[9]+ "," + generations);
                }
            }
            if(generations%100==0 && generations>200){
                for(Individual ind: population){
                    System.out.println(ind.genome[0] + "," + ind.genome[1] + "," + ind.genome[2] + "," + ind.genome[3] + "," + ind.genome[4]+ "," + ind.genome[5] + "," +ind.genome[6] + "," + ind.genome[7] + "," + ind.genome[8] + "," + ind.genome[9]+ "," + generations);
                }
            }*/
            long currentTime = System.currentTimeMillis();
            double elapsedTime = (currentTime - previousTime) / 1000.0;
          System.out.println(generations + " , " + best_inGen + "," + elapsedTime);
           // System.out.println(stuck_count);
        }
        //System.out.println(generations);
    }

    // not in crossover class due to 4 parents, not 2
    private void diffCrossover(Individual parent0 , Individual parent1,Individual parent2, Individual parent3, double scaling_factor){
        double[] parent0_genome = parent0.getGenome();
        double[] parent1_genome = parent1.getGenome();
        double[] parent2_genome = parent2.getGenome();
        double[] parent3_genome = parent3.getGenome();

        double[][] child_genome = new double[4][10];
        double[][] parent_array = new double[4][10];

        for(int k = 0; k<4; k++){
            parent_array[k] = parent0.getGenome();
        }

        for (int i = 0; i < 10; i++) {
            if (rnd_.nextBoolean()) {
                child_genome[0][i] = parent0_genome[i];
                child_genome[1][i] = parent1_genome[i];
                child_genome[2][i] = parent0_genome[i];
                child_genome[3][i] = parent1_genome[i];
            }
            else {
                child_genome[0][i] = parent1_genome[i];
                child_genome[1][i] = parent0_genome[i];
                child_genome[2][i] = parent1_genome[i];
                child_genome[3][i] = parent0_genome[i];

            }

            child_genome[0][i] = child_genome[0][i] + ((parent2_genome[i] - parent3_genome[i]) * scaling_factor);
            child_genome[1][i] = child_genome[1][i] + ((parent3_genome[i] - parent2_genome[i]) * scaling_factor);
            child_genome[2][i] = child_genome[2][i] + ((parent2_genome[i] - parent3_genome[i]) * scaling_factor);
            child_genome[3][i] = child_genome[3][i] + ((parent3_genome[i] - parent2_genome[i]) * scaling_factor);
        }
        for(int k = 0; k<4; k++){
            Individual child = new Individual(0, rnd_);
            child.setGenome(child_genome[k]);
            child.setEvaluated(false);
            child.setAlphas(parent0.alphas);
            child.setAllSigmas(parent0.sigmas,parent0.sigmas);
            population.add(child);
        }
    }

    public ArrayList<Individual> tournament(ArrayList<Individual> population,
    double naturalFitnessScores,
    int selectionSize)
    {
        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            Individual candidate1 = population.get(rnd_.nextInt(population.size()));
            Individual candidate2 = population.get(rnd_.nextInt(population.size()));

            double selectFitter = rnd_.nextDouble();
            if (selectFitter > naturalFitnessScores)
            {
                selection.add(candidate2.fitness > candidate1.fitness ? candidate2: candidate1);
            }
            else
            {
                selection.add(candidate2.fitness > candidate1.fitness ? candidate1: candidate2);
            }
        }
    return selection;
    }


    //better to work with ints, tried direclty with Individuals, too slow
    private int[] tournamentInts(int children_number,int cut){
        int[] selected_parents = new int[children_number];
        int candidate = 0;
        int m =0;
        while( m < children_number) {
            double fitness = 0;
            int[] candidates = new int[cut];
            boolean selected = false;
            double best_ind = 0;
            for (int i = 0; i < cut; i++) {
                candidates[i] = rnd_.nextInt(population.size());
                fitness = population.get(candidates[i]).getFitness();
                if (fitness > best_ind && fitness > 0) {
                    best_ind = fitness;
                    candidate = candidates[i];
                }
            }
            for (int  i = 0 ; i < children_number;i++){
                if(selected_parents[i] == candidate) selected = true;
            }
            if (!selected) {
                selected_parents[m] = candidate;
                m++;
            }
        }
        return selected_parents;
    }
}
