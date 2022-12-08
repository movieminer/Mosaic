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

public class SATSolver {
    private static final String filename = "dimacs.cnf";

    public SATSolver(){}

    public static String generateDIMACS(Game game){
        StringBuilder sb = new StringBuilder();
        String dimacs;
        if (game.getSolve_method().equals("naiveGenerator") || game.getSolve_method().equals("improvedGenerator"))
            dimacs = game.SATNotUndefined() + game.CNFToDimacs() + game.generateCounterSolution();
        else
            dimacs = game.SATNotUndefined() + game.CNFToDimacs();

        sb.append("c dimacs of Mosaic\n");
        sb.append("p cnf ").append(game.getVariableCount()).append(" ").append(game.getClausesCount()).append("\n");
        sb.append(dimacs);
        return sb.toString();
    }

    public static void createFile(Game game){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                //System.out.println("File created: " + myObj.getName());
            } else {
                //System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(generateDIMACS(game));
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
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
                //System.out.println("Satisfiable !");
                solution = reader.decode(problem.model());
                //System.out.println(solution);
            } else {
                //System.out.println("Unsatisfiable !");
                return null;
            }
        } catch (IOException | ParseFormatException | TimeoutException e) {
            e.printStackTrace();
        } catch (ContradictionException e) {
            throw new RuntimeException(e);
        }
        return solution;
    }
}
