package puzzle;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;

public class TestMain {

    public static List<List<Integer>> generate(int[][] sets) {
        int solutions = 1;
        List<List<Integer>> CNF = new ArrayList<>();
        for(int i = 0; i < sets.length; solutions *= sets[i].length, i++);
        for(int i = 0; i < solutions; i++) {
            int j = 1;
            List<Integer> dupelist = new ArrayList<>();
            for(int[] set : sets) {

                dupelist.add(set[(i/j)%set.length]);
                j *= set.length;
            }
            List<Integer> list = dupelist.stream().sorted().distinct().collect(Collectors.toList());
            if (list.stream().map(x -> abs(x)).distinct().collect(Collectors.toList()).size() == list.size()) {
                System.out.println(list);
                CNF.add(list);
            }
        }
        return CNF.stream().distinct().collect(Collectors.toList());
    }

    public static String CNFToDimacs(List<List<Integer>> cnf){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cnf.size(); i++) {
            for (int j = 0; j < cnf.get(i).size(); j++) {
                System.out.print(cnf.get(i).get(j) + " ");
                sb.append(cnf.get(i).get(j)).append(" ");
            }
            System.out.print('\n');
            sb.append("0\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<List<Integer>> smallList = asList(
                asList(1,2),
                asList(3,4));

        int[][] lists = {{-2,-3,5,6,8,9},
                {-2,3,-5,6,8,9},
                {-2,3,5,-6,8,9},
                {-2,3,5,6,-8,9},
                {-2,3,5,6,8,-9},
                {2,-3,-5,6,8,9},
                {2,-3,5,-6,8,9},
                {2,-3,5,6,-8,9},
                {2,-3,5,6,8,-9},
                {2,3,-5,-6,8,9},
                {2,3,-5,6,-8,9},
                {2,3,-5,6,8,-9},
                {2,3,5,-6,-8,9},
                {2,3,5,-6,8,-9},
                {2,3,5,6,-8,-9}};
        //CNFToDimacs(generate(lists));
    }
}
