import java.util.Random;
import java.lang.Math;
import java.util.*;

public class AdaptiveRandomness{

public ArrayList<Individual> GeneratePopulation(ArrayList<Individual> population, int populationSize, double startingSigma, Random rnd) {
    ArrayList<Individual> PartialPop = new ArrayList<>();
    Individual ind = new Individual(startingSigma, rnd, -5, 5);
    PartialPop.add(ind);
    for(int pps=1; pps<populationSize; pps++){
        ArrayList<Individual> ST = new ArrayList<>();
        // ST = set of k trial individuals
        for(int k=0; k<15; k++){
            Individual trial_ind = new Individual(startingSigma, rnd, -5, 5);
            ST.add(trial_ind);
            }
        /* set a dictionary that takes the position (k) of the trial individual
          and the EuclideanDistance*/
        double[] minED = new double[15];

        for(int k=0; k<15; k++){
            double[] ED = new double[pps];
            for(int j=0; j<pps; j++){

                /* for each individual in the partial population, we calculate
                the EuclideanDistance between it and an particular individual in
                the set of trials */
                ED[j] = EuclideanDistance(PartialPop.get(j).genome, ST.get(k).genome);
                }
            //we want to get the k and the minimum ED associated with it
            minED[k] = findMin(ED);
            //Arrays.sort(ED);
            //map.put(k, ED[0]);
            //minED.add(ED[0]);
            }
        //now we get the max of the ED with the associated k
        //Arrays.sort(minED);
        int k = findMaxIndex(minED);

        //Map.Entry<Integer, Double> maxEntry = null;
        // get the maximum ED with k
        //for (Map.Entry<Integer, Double> entry : map.entrySet()) {
        //    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
        //    maxEntry = entry;
        //    }
        //    return MaxEntry;
        //  }
      //maxEntry = k in Set of Trial individuals
      /* Select the trial individual 𝑌𝑛 such that for all 𝑗 ∈ {1, 2, . . . , 𝑘}:
      min𝑑(𝑋𝑖,𝑌𝑛)≥ min𝑑(𝑋𝑖,𝑌𝑗) 1≤𝑖≤|PP| 1≤𝑖≤|PP| */
      //Add to the Partial Population the trial individual
      PartialPop.add(ST.get(k));
      }
      return PartialPop;
    }

double findMin (double[] input){
  return Arrays.stream(input).min().getAsDouble();
}

int findMaxIndex(double[] array) {
    double max = array[0];
    int maxIdx = 0;
    for(int i = 1; i < array.length; i++) {
        if(array[i] > max) {
          max = array[i];
          maxIdx = i;
          }
        }
        return maxIdx;
      }


public static double EuclideanDistance(double[] array1, double[] array2)
    {
        double Sum = 0.0;
        for(int i=0;i<array1.length;i++) {
           Sum = Sum + Math.pow((array1[i]-array2[i]),2.0);
        }
        return Math.sqrt(Sum);
    }
  }