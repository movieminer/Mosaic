package puzzle;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;

public class TestMain {
    private static Game game;
    private static int WIDTH;
    private static int HEIGHT;
    private static Example test;

    public static String CNFToDimacs(List<List<Integer>> cnf){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cnf.size(); i++) {
            for (int j = 0; j < cnf.get(i).size(); j++) {
                sb.append(cnf.get(i).get(j)).append(" ");
            }
            sb.append("0\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        test = new Example("5x5:2a6f43a3a3a3f10");
        WIDTH = test.getWidth();
        HEIGHT = test.getHeight();
        game = new Game(WIDTH, HEIGHT, test.board());
        System.out.print(game.SATNotUndefined());
        System.out.print(CNFToDimacs(game.allCellsToDNF()));
        //System.out.println(CNFToDimacs(game.tseytin(smallList)));
        //CNFToDimacs(generate(lists));
    }
}
