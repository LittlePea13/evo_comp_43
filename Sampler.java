import java.util.ArrayList;
import java.util.Random;

public class Sampler {
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
    public int rouletteSelection(double[] weight, Random r)
    {
        // calculate the total weight
        double weight_sum = 0;
        for(int i=0; i<weight.length; i++) {
            weight_sum += weight[i];
        }
        // get a random value
        double value = r.nextDouble() * weight_sum;
        // locate the random value based on the weights
        for(int i=0; i<weight.length; i++) {
            value -= weight[i];
            if(value < 0) return i;
        }
        // when rounding errors occur, we return the last item's index
        return weight.length - 1;
    }

    // Returns a uniformly distributed double value between 0.0 and 1.0

}

class StochasticUniversalSamplingFitness extends Sampler {
    public ArrayList<Individual> sample(ArrayList<Individual> population,
                                        int selectionSize, Random r) {
        // Calculate the sum of all fitness values.
        double aggregateFitness = 0;
        for (Individual candidate : population) {
            aggregateFitness += candidate.fitness; //should change this with function result?
        }

        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        // Pick a random offset between 0 and 1 as the starting point for selection.
        double startOffset = r.nextDouble();
        double cumulativeExpectation = 0;
        int index = 0;
        for (Individual candidate : population) {
            // Calculate the number of times this candidate is expected to
            // be selected on average and add it to the cumulative total
            // of expected frequencies.
            cumulativeExpectation += candidate.fitness / aggregateFitness * selectionSize;

            // If f is the expected frequency, the candidate will be selected at
            // least as often as floor(f) and at most as often as ceil(f). The
            // actual count depends on the random starting offset.
            while (cumulativeExpectation > startOffset + index) {
                selection.add(candidate);
                index++;
            }
        }
        return selection;
    }
}
class StochasticUniversalSampling extends Sampler {
    public ArrayList<Individual> sample(ArrayList<Individual> population,
                                                             int selectionSize, Random r) {
        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        // Pick a random offset between 0 and 1 as the starting point for selection.
        double startOffset = r.nextDouble();
        double cumulativeExpectation = 0;
        int index = 0;
        int rank = 0;
        int populationSize = population.size();
        for (Individual candidate : population) {
            // Calculate the number of times this candidate is expected to
            // be selected on average and add it to the cumulative total
            // of expected frequencies.
            cumulativeExpectation += LinearRankInd(rank, populationSize, 1.5) * selectionSize;

            // If f is the expected frequency, the candidate will be selected at
            // least as often as floor(f) and at most as often as ceil(f). The
            // actual count depends on the random starting offset.
            while (cumulativeExpectation > index) {
                selection.add(candidate);
                index++;
            }
            rank++;
        }
        return selection;
    }

}
class RouletteSampling extends Sampler {
    public ArrayList<Individual> sample(ArrayList<Individual> population,
                                                  int selectionSize, Random r) {
        ArrayList<Individual> selection = new ArrayList<Individual>(selectionSize);
        // Calculate the weights for whole population
        double[] weights = LinearRankPop(population.size(), 1.5);
        for (int i = 0; i < selectionSize; i++) {
            selection.add(population.get(rouletteSelection(weights, r)));
        }
        return selection;
    }
}


