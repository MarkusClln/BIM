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
public class Knapsack2 extends DynProJava<String> {

  public static void main(String[] args) {


    //String[] s = {"-", "K", "I", "Q", "Y", "K", "R", "E", "P", "N", "I", "P", "S", "V", "S", "L", "I", "N", "S", "L", "F", "A", "W", "E", "I", "R", "D", "R", "I"};
    //String[] t = {"-", "K", "A", "Q", "Y", "R", "R", "E", "C", "M", "I", "F", "V", "W", "E", "I", "N", "R", "L"};
    String[] t = {"-", "A", "G", "C", "T","C","A","C","A"};
    String[] s = {"-", "G", "C", "T", "A","C","A","C"};


    Knapsack2 dp = new Knapsack2(s, t);

    // The maximum is expected at the last item (n-1)
    // with no capacity left (0);
    List<PathEntry<String>> solutionJava =
            dp.solutionAsList(new Idx(dp.n()-1, dp.m()-1));



    for (PathEntry<String> entry : solutionJava) {
      System.out.print(entry.decision() + " ");
    }
    System.out.println("\n");
    System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n()-1, dp.m()-1))));

    dp.draw_result(s,t,solutionJava);
  }


  private String[] s;
  private String[] t;
  private Idx best_idx;


  public Knapsack2(String[] s, String[] t) {
    this.s = s;
    this.t = t;
    // Defines how values are formatted in the console output.
    // Formatter are: INT, ENGINEER, DECIMAL
    this.formatter_$eq(this.INT());
  }


  @Override
  public int n() {
    return s.length;
  }

  @Override
  public int m() {
    return t.length;
  }

  @Override
  public double value(Idx idx, String d) {
    if (d.equals("Start")) {
      return 0;
    } else
        if (d.equals("Match")) {
          return 1;
    } else
      if (d.equals("Mismatch")) {
        return -1;
    } else
      {
        return -2;
      }
  }

  /**
   * If the remaining capacity (idx.j) plus the weight that could be taken
   * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
   * If not, we can only skip it (={0}).
   */
  @Override
  public String[] decisions(Idx idx) {
    if (idx.j() == 0) {
      if (idx.i() == 0) {
        return new String[]{"Start",};
      } else {
        return new String[]{"Delete"};
      }
    } else
        if (idx.i() == 0) {
          return new String[] {"Insert"};
        } else {
            if (s[idx.i()] == (t[idx.j()])) {
              return new String[] {"Match", "Delete", "Insert"};
            } else {
                return new String[] {"Mismatch", "Delete", "Insert"};
            }
    }
  }

  /**
   * The prev. state is the previous item (idx.i-1) and the prev. capacity.
   * The prev. capacity is the remaining capacity (idx.j) plus weight that was
   * taken (or plus 0 if it was skipped).
   */
  @Override
  public Idx[] prevStates(Idx idx, String d) {
    Idx pidx;

      switch (d) {
        case "Delete":
          pidx = new Idx(idx.i() - 1, idx.j());
          break;
        case "Insert":
          pidx = new Idx(idx.i(), idx.j() - 1);
          break;
        case "Match":
          pidx = new Idx(idx.i() - 1, idx.j() - 1);
          break;
        case "Mismatch":
          pidx = new Idx(idx.i() - 1, idx.j() - 1);
          break;
        default:
          return new Idx[]{};
      }
    return new Idx[]{pidx};
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
    return new Some(s);
  }

  /**
   * Provide column labels, i.e. each columns gets a short description.
   * In this case, the column labels are the same as the column index.
   *
   * @return Array of size m with the labels.
   */
  @Override
  public Option<String[]> columnLabels() {
    return new Some(t);
  }

  public void draw_result(String[] s, String[] t, List<PathEntry<String>> solutionJava){
    int index_s =1;
    int index_t =1;
    String str_s="";
    String str_t="";
    String str_compare="";
    for (PathEntry<String> entry : solutionJava) {

      switch (entry.decision()) {
        case "Delete":
          str_s= str_s+s[index_s]+" ";
          str_t= str_t+"- ";
          str_compare = str_compare+"  ";
          index_s++;
          break;
        case "Insert":
          str_t= str_t+t[index_t]+" ";
          str_s= str_s+"- ";
          str_compare = str_compare+"  ";
          index_t++;
          break;
        case "Match":
          str_s= str_s+s[index_s]+" ";
          str_t= str_t+t[index_t]+" ";
          str_compare = str_compare+"| ";
          index_s++;
          index_t++;
          break;
        case "Mismatch":
          str_s= str_s+s[index_s]+" ";
          str_t= str_t+t[index_t]+" ";
          str_compare = str_compare+"  ";
          index_s++;
          index_t++;
          break;

      }
    }
    System.out.println(str_s);
    System.out.println(str_compare);
    System.out.println(str_t);
  }
}