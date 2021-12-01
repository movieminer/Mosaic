package puzzle;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private static String filename;


    public static String generateDIMACS(Game game){
        StringBuilder sb = new StringBuilder();
        String dimacs = game.SATNotUndefined() + game.CNFToDimacs();

        sb.append("c dimacs of Mosaic\n");
        sb.append("p cnf ").append(game.getVariableCount()).append(" ").append(game.getClausesCount()).append("\n");
        sb.append(dimacs);
        return sb.toString();
    }

    public static void createFile(Game game){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(generateDIMACS(game));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String solve(Game game){
        createFile(game);

        ISolver solver = SolverFactory.newDefault();
        String solution = null;
        Reader reader = new DimacsReader(solver);
        try {
            IProblem problem = reader.parseInstance(filename);
            if (problem.isSatisfiable()) {
                System.out.println(" Satisfiable !");
                solution = reader.decode(problem.model());
                System.out.println(solution);
            } else {
                System.out.println(" Unsatisfiable !");
            }
        } catch (ContradictionException | IOException | ParseFormatException | TimeoutException e) {
            e.printStackTrace();
        }
        return solution;
    }

    public static void main(String[] args) {
        test = new Example("5x5:2a6f43a3a3a3f10");
        WIDTH = test.getWidth();
        HEIGHT = test.getHeight();
        game = new Game(WIDTH, HEIGHT, test.board());
        filename = "dimacs.cnf";
        game.printBoard();
        //System.out.println(generateDIMACS(game));
        game.solveWithDIMACS(solve(game));
        game.printTypes();
    }
}
