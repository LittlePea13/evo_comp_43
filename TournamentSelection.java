import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Selection strategy that picks a pair of candidates at random and then
 * selects the fitter of the two candidates with probability p, where p
 * is the configured selection probability (therefore the probability of
 * the less fit candidate being selected is 1 - p).
 * @author Daniel Dyer
 */
public class TournamentSelection implements SelectionStrategy<Object>
{
    private final double selectionProbability;

    private String description = "Tournament Selection";

    /**
     * Creates a tournament selection strategy that is controlled by the
     * variable selection probability provided by the specified
     * {@link NumberGenerator}.
     * @param selectionProbability A number generator that produces values in
     * the range {@literal 0.5 < p < 1}.  These values are used as the probability
     * of the fittest candidate being selected in any given tournament.
     */
    public TournamentSelection(double selectionProbability)
    {
        Random r = new Random();
        this.selectionProbability = 0.5 + rand.nextDouble() * 0.5;;
    }
    public generateRandom()
    {
        Random r = new Random();
        this.selectionProbability = 0.5 + rand.nextDouble() * 0.5;
    }

    
    /**
     * Creates a tournament selection strategy with a fixed probability.
     * @param selectionProbability The probability that the fitter of two randomly
     * chosen candidates will be selected.  Since this is a probability it must be
     * between 0.0 and 1.0.  This implementation adds the further restriction that
     * the probability must be greater than 0.5 since any lower value would favour
     * weaker candidates over strong ones, negating the "survival of the fittest"
     * aspect of the evolutionary algorithm.
     */
    public TournamentSelection(Probability selectionProbability)
    {
        generateRandom();
        if (selectionProbability <= 0.5)
        {
            throw new IllegalArgumentException("Selection threshold must be greater than 0.5.");
        }
        this.description = "Tournament Selection (p = " + selectionProbability.toString() + ')';
    }


    public <S> List<S> select(List<Individual<S>> population,
                              boolean naturalFitnessScores,
                              int selectionSize,
                              Random rng)
    {
        Random r = new Random();
        List<S> selection = new ArrayList<S>(selectionSize);
        for (int i = 0; i < selectionSize; i++)
        {
            // Pick two candidates at random.
            Individual<S> candidate1 = population.get(rng.nextInt(population.size()));
            Individual<S> candidate2 = population.get(rng.nextInt(population.size()));

            // Use a random value to decide wether to select the fitter individual or the weaker one.
            boolean selectFitter = r.nextValue().nextEvent(rng);
            if (selectFitter == naturalFitnessScores)
            {
                // Select the fitter candidate.
                selection.add(candidate2.fitness > candidate1.fitness
                              ? candidate2.genome
                              : candidate1.genome);
            }
            else
            {
                // Select the less fit candidate.
                selection.add(candidate2.fitness > candidate1.fitness
                              ? candidate1.genome
                              : candidate2.genome);
            }
        }
        return selection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return description;
    }
}