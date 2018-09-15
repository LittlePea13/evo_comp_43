import java.util.Random;
import java.util.Properties;
import java.util.Arrays;

public class Test
{
	Random rnd_;
  
  public static double[] newParent(){
    Random rnd_ = new Random();
    double maxX = 5.0f;
    double minX = -5.0f;
    double[] parent = new double[10];
    for (int i = 0; i < parent.length; i++) {
      parent[i] = rnd_.nextDouble() * (maxX - minX) + minX;
    }
    return(parent);
  }

  public static double[][] pointcrossover(double[] parentOne, double[] parentTwo){
    double[][] children = new double[2][10];
    for (int i = 0; i < parentOne.length; i++) {
      double prob = Math.random();
      System.out.println(prob);
      if(prob>0.5){
        children[0][i]=parentOne[i];
        children[1][i]=parentTwo[i];
      } else {
        children[1][i]=parentOne[i];
        children[0][i]=parentTwo[i];
      }
    }
    return(children);
  }

  public static double[][] crossover(double[] parentOne, double[] parentTwo){
    double[][] children = new double[2][10];
    Random rnd_ = new Random();
    int point = rnd_.nextInt(8)+1;
    System.out.println(point);
    System.arraycopy(Arrays.copyOfRange(parentOne, 0, point), 0, children[0], 0, point);
    System.arraycopy(Arrays.copyOfRange(parentTwo, point, parentOne.length), 0, children[0], point, parentOne.length-point);
    System.arraycopy(Arrays.copyOfRange(parentTwo, 0, point), 0, children[1], 0, point);
    System.arraycopy(Arrays.copyOfRange(parentOne, point, parentOne.length), 0, children[1], point, parentOne.length-point);
    return(children);
  }

	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public static void main(String[] args)
	{
    // Run your algorithm here
    double[][] children = new double[2][10];
    double[][] childrenCros = new double[2][10];
    int size = 2;
    double[][] population = new double[size][10];
    for (int i = 0; i < population.length; i++) {
      population[i] = newParent();
    }
    System.out.println(Arrays.deepToString(population));
    children = pointcrossover(population[0], population[1]);
    System.out.println(Arrays.deepToString(children));
    childrenCros = crossover(population[0], population[1]);
    System.out.println(Arrays.deepToString(childrenCros));
	}
}
