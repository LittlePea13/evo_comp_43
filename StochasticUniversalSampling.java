
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An alternative to {@link RouletteWheelSelection}
 * as a fitness-proportionate selection strategy.  Ensures that the frequency of selection for
 * each candidate is consistent with its expected frequency of selection.
 * @author Daniel Dyer
 */
public class StochasticUniversalSampling implements SelectionStrategy<Object>
{
    public <S> List<S> select(List<Individual<S>> population,
                              boolean naturalFitnessScores,
                              int selectionSize,
                              Random rng)
    {
        // Calculate the sum of all fitness values.
        double aggregateFitness = 0;
        for (Individual<S> candidate : population)
        {
            aggregateFitness += getAdjustedFitness( candidate.fitness,
                                                   naturalFitnessScores);
        }

        List<S> selection = new ArrayList<S>(selectionSize);
        // Pick a random offset between 0 and 1 as the starting point for selection.
        double startOffset = rng.nextDouble();
        double cumulativeExpectation = 0;
        int index = 0;
        for (EvaluatedCandidate<S> candidate : population)
        {
            // Calculate the number of times this candidate is expected to
            // be selected on average and add it to the cumulative total
            // of expected frequencies.
            cumulativeExpectation += getAdjustedFitness(candidate.fitness,
                                                        naturalFitnessScores) / aggregateFitness * selectionSize;

            // If f is the expected frequency, the candidate will be selected at
            // least as often as floor(f) and at most as often as ceil(f). The
            // actual count depends on the random starting offset.
            while (cumulativeExpectation > startOffset + index)
            {
                selection.add(candidate.getCandidate());
                index++;
            }
        }
        return selection;
    }


    private double getAdjustedFitness(double rawFitness, boolean naturalFitness)
    {
        if (naturalFitness)
        {
            return rawFitness;
        }
        else
        {
            // If standardised fitness is zero we have found the best possible
            // solution.  The evolutionary algorithm should not be continuing
            // after finding it.
            return rawFitness == 0 ? Double.POSITIVE_INFINITY : 1 / rawFitness;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Stochastic Universal Sampling";
    }
}