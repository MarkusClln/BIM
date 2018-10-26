import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.ArrayList;
import java.util.List;

/**
 * The Knapsack problem solved with dynamic programming.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Knapsack2 extends DynProJava<Integer> {

  public static void main(String[] args) {

    int[] weight = {1,1,1,1,1,20,30,45};
    int capacity = 50;
    int[]values = {1,1,1,1,1,20,30,45};
    String[] items = {"2","9"};
    int[] weights = get_weights(weight, capacity);
    //int[] values = get_values(weights);
   // String[] items = get_rowLabels(weights);




    Knapsack2 dp = new Knapsack2(items, weight, capacity,values);
    System.out.println(new Idx(dp.n() - 1, 0).toString());
    // The maximum is expected at the last item (n-1)
    // with no capacity left (0);
    List<PathEntry<Integer>> solutionJava =
            dp.solutionAsList(new Idx(dp.n() - 1, 0));
    System.out.println("Optimal Decisions:");
    for (int i : weights) {
      System.out.print(i + " ");
    }
    System.out.println("");
    for (PathEntry<Integer> entry : solutionJava) {
      System.out.print(entry.decision() + " ");
    }
    System.out.println("\n");
    System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 1, 0))));


  }


  private String[] items;
  private int[] weights;
  private int[] values;
  private int capacity;

  public Knapsack2(String[] items, int[] weights, int capacity, int[] values) {
    this.items = items;
    this.values = values;
    this.weights = weights;
    this.capacity = capacity;
    // Defines how values are formatted in the console output.
    // Formatter are: INT, ENGINEER, DECIMAL
    this.formatter_$eq(this.INT());
  }

  public static int[] get_weights(int[] array, int capacity){
    List<Integer> tada = new ArrayList<Integer>();

    for(int i =0;i<array.length;i++){
      for(int j = 1; capacity-(array[i]*j)>=0;j++){
        tada.add(array[i]);
      }
    }

    int[] real_stuff = new int[ tada.size() ];
    for(int i = 0; i<tada.size();i++){
      real_stuff[i]=tada.get(i);
    }
    return real_stuff;
  }

  public static int[] get_values(int[] array){
    int[] real_values = new int[ array.length ];
    for(int i = 0; i<array.length;i++){
      real_values[i]=array[i]*array[i];
    }
    return real_values;
  }

  public static String[] get_rowLabels(int[] array){
    String[] rowLabels = new String[ array.length ];
    for(int i = 0; i<array.length;i++){
      rowLabels[i]=Integer.toString(array[i]);
    }
    return rowLabels;
  }

  @Override
  public int n() {
    return weights.length;
  }

  @Override
  public int m() {
    return capacity + 1;
  }

  @Override
  public double value(Idx idx, Integer d) {
    return d * weights[idx.i()]*weights[idx.i()];
  }

  /**
   * If the remaining capacity (idx.j) plus the weight that could be taken
   * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
   * If not, we can only skip it (={0}).
   */
  @Override
  public Integer[] decisions(Idx idx) {
    if (idx.j() + weights[idx.i()] <= capacity) {
      return new Integer[]{0, 1};
    } else {
      return new Integer[]{0};
    }
  }

  /**
   * The prev. state is the previous item (idx.i-1) and the prev. capacity.
   * The prev. capacity is the remaining capacity (idx.j) plus weight that was
   * taken (or plus 0 if it was skipped).
   */
  @Override
  public Idx[] prevStates(Idx idx, Integer d) {
    if (idx.i() > 0) {
      Idx pidx = new Idx(idx.i() - 1, idx.j() + d * weights[idx.i()]);
      return new Idx[]{pidx};
    } else {
      return new Idx[]{};
    }
  }

  /**
   * Defines whether the minimum or maximum is calculated.
   *
   * @return
   */
  @Override
  public Function2 extremeFunction() {
    return this.MAX(); // oder MIN()
  }

  /**
   * Provide row labels, i.e. each row gets a short description.
   *
   * @return Array of size n with the labels.
   */
  @Override
  public Option<String[]> rowLabels() {
    return new Some(items);
  }

  /**
   * Provide column labels, i.e. each columns gets a short description.
   * In this case, the column labels are the same as the column index.
   *
   * @return Array of size m with the labels.
   */
  @Override
  public Option<String[]> columnLabels() {
    String[] cArray = new String[capacity + 1];
    for (int i = 0; i <= capacity; i++) {
      cArray[i] = "" + i;
    }
    return new Some(cArray);
  }
}