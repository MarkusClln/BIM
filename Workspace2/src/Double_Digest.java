import org.apache.commons.collections4.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Double_Digest {


    public static void main(String[] args) {

        List<Integer> list_A = new ArrayList<Integer>();
        list_A.add(1);
        list_A.add(3);
        list_A.add(5);
        list_A.add(8);
        int max_length_a =0;

        for(int a: list_A){
            max_length_a+=a;
        }
        Collection<List<Integer>> list_A_permu = CollectionUtils.permutations(list_A);

        List<Integer> list_B = new ArrayList<Integer>();
        list_B.add(2);
        list_B.add(3);
        list_B.add(5);
        list_B.add(7);

        int max_length_b =0;
        for(int b: list_B){
            max_length_b+=b;
        }
        Collection<List<Integer>> list_B_permu = CollectionUtils.permutations(list_B);

        List<Integer> list_AB = new ArrayList<Integer>();
        list_AB.add(1);
        list_AB.add(1);
        list_AB.add(2);
        list_AB.add(2);
        list_AB.add(3);
        list_AB.add(4);
        list_AB.add(4);

        System.out.println("Anzahl gültiger Fragment-Reihenfolgen für A: " + list_A_permu.size());
        int i = 1;
        for (List a : list_A_permu) {
            System.out.println(i + ". Fragment-Reihenfolge A = " + a);
            i++;
        }
        System.out.println("Anzahl gültiger Fragment-Reihenfolgen für B: " + list_B_permu.size());
        i = 1;
        for (List a : list_B_permu) {
            System.out.println(i + ". Fragment-Reihenfolge B = " + a);
            i++;
        }


        List<Integer> list_A_schnitte = new ArrayList<Integer>();
        List<Integer> list_B_schnitte = new ArrayList<Integer>();
        int ergebnisse=1;
        for (List<Integer> a : list_A_permu) {
            int tmp_A_schnitte=0;
            for(int o =0;o<a.size();o++) {
                tmp_A_schnitte += a.get(o);
                if(tmp_A_schnitte<=max_length_a) {
                    list_A_schnitte.add(tmp_A_schnitte);
                }
            }

            for (List<Integer> b : list_B_permu) {
                int tmp_B_schnitte = 0;
                for (int k = 0; k < b.size(); k++) {
                    tmp_B_schnitte += b.get(k);
                    if(tmp_B_schnitte<=max_length_b){
                        list_B_schnitte.add(tmp_B_schnitte);
                    }
                }
                //System.out.println("A: "+list_A_schnitte+" B: "+list_B_schnitte);
                List<Integer> compare_schnitte = new ArrayList<Integer>();
                compare_schnitte.addAll(list_A_schnitte);
                compare_schnitte.addAll(list_B_schnitte);
                Collections.sort(compare_schnitte);

                List<Integer> end_liste = new ArrayList<Integer>();
                int zähler=0;
                for(int q :compare_schnitte){
                    if(q-zähler!=0){
                        end_liste.add(q-zähler);
                        zähler=q;
                    }

                }
                Collections.sort(end_liste);

                if(list_AB.equals(end_liste)) {
                    System.out.println(ergebnisse+". Compare A: " + list_A_schnitte + " with B: " + list_B_schnitte + " = " + end_liste);
                    ergebnisse++;
                }

                list_B_schnitte.clear();
            }

            list_A_schnitte.clear();

        }
    }
}
