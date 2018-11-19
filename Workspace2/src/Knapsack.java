import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.List;

/**
 * The Knapsack problem solved with dynamic programming.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Knapsack extends DynProJava<Integer> {

  public static void main(String[] args) {
    String[] rowLabels = {"1","3","5","6","9"};
    int[] fragments  = {1,3,5,6,9};
    int total_length = 58;
    Knapsack dp = new Knapsack(rowLabels, fragments , total_length);
    // The maximum is expected at the last item (n-1)
    // with no capacity left (0);
    List<PathEntry<Integer>> solutionJava =
            dp.solutionAsList(new Idx(dp.n() - 1, 0));
    System.out.println("Optimal Decisions:");
    for (PathEntry<Integer> entry : solutionJava) {
      System.out.print(entry.decision() + " ");
    }
    System.out.println("\n");
    System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 1, 0))));
  }

  private String[] items;
  private int[] fragments ;
  private int total_length ;

  public Knapsack(String[] items, int[] fragments , int total_length) {
    this.items = items;
    this.fragments  = fragments ;
    this.total_length = total_length;
    // Defines how values are formatted in the console output.
    // Formatter are: INT, ENGINEER, DECIMAL
    this.formatter_$eq(this.INT());
  }

  @Override
  public int n() {
    return fragments.length;
  }

  @Override
  public int m() {
    return total_length + 1;
  }

  @Override
  public double value(Idx idx, Integer d) {
    //System.out.println("i: "+idx.i()+" j: "+idx.j()+" d: "+d);
    return d;
  }

  /**
   * If the remaining capacity (idx.j) plus the weight that could be taken
   * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
   * If not, we can only skip it (={0}).
   */
  @Override
  public Integer[] decisions(Idx idx) {
    if (idx.j() + fragments [idx.i()] <= total_length) {
      int x = (total_length - idx.j())/fragments[idx.i()];
      //System.out.println("fragments: "+ fragments[idx.i()]+" x: "+x);
      return new Integer[]{x};
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
      Idx pidx = new Idx(idx.i() - 1, idx.j() + d * fragments [idx.i()]);
      //System.out.println("pidx: "+pidx+" idx: "+idx+" d: "+d);
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
    return this.MIN(); // oder MIN()
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
    String[] cArray = new String[total_length + 1];
    for (int i = 0; i <= total_length; i++) {
      cArray[i] = "" + i;
    }
    return new Some(cArray);
  }
}