package puzzle;

import com.sun.prism.impl.ps.CachingEllipseRep;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private final int WIDTH, HEIGHT;
    private final Cell[][] board;
    private int nextFreeVariable;
    private int clauses = 0;
    private final String solve_method;

    public Game(int width, int height, String solve_method) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.board = new Cell[HEIGHT][WIDTH];
        this.nextFreeVariable = WIDTH * HEIGHT + 1;
        this.solve_method = solve_method;
        for (int y = 0; y < HEIGHT; y++)
            for (int x = 0; x < WIDTH; x++)
                board[y][x] = new Cell(x, y, -1);
    }

    public Game(int width, int height, String solve_method, String[][] config) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.nextFreeVariable = WIDTH * HEIGHT + 1;
        this.board = new Cell[HEIGHT][WIDTH];
        this.solve_method = solve_method;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                board[y][x] = new Cell(x, y, Integer.parseInt(config[y][x]));
            }
        }
    }


    public void changeType(int x, int y, Type newType) {
        board[y][x].setType(newType);
    }

    public Cell getCell(int x, int y) {
        return board[y][x];
    }

    public Cell[][] getBoard(){
        return board;
    }

    public int getVariableCount() {
        return nextFreeVariable - 1;
    }

    public int getClausesCount() {
        return clauses;
    }

    public String getSolve_method(){
        return solve_method;
    }

    public String getCellValue(int x, int y) {
        if (board[y][x].getValue() != -1)
            return board[y][x].toString();
        else
            return "";
    }

    public void solveWithDIMACS(String sol) {
        if (sol == null) {
            return;
        }
        Scanner scanner = new Scanner(sol);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (scanner.nextInt() < 0)
                    changeType(x, y, Type.W);
                else
                    changeType(x, y, Type.B);
            }

        }
    }

    public boolean checkCell(int x, int y) {
        int expected = board[y][x].getValue();
        int count = 0;
        if (expected == -1) {
            return true;
        }
        for (Dir dir : Dir.values()) {
            if (x + dir.x >= 0 && x + dir.x < WIDTH && y + dir.y >= 0 && y + dir.y < HEIGHT) {
                if (board[y + dir.y][x + dir.x].getType() == Type.B)
                    count++;
            }
        }
        return (expected == count);
    }

    public boolean checkBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (!checkCell(x, y) || board[y][x].getType() == Type.U) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(board[y][x] + "  ");
            }
            System.out.print("\n");
        }

    }

    public void printTypes() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(board[y][x].getType() + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public String generateCounterSolution() {
        StringBuilder sb = new StringBuilder();

        for (Cell[] cellRow : board){
            for(Cell cell : cellRow){
                if (cell.getType() == Type.B)
                    sb.append("-");
                sb.append(cell.getRank(WIDTH)).append(" 0\n");
                clauses++;
            }
        }
        return sb.toString();
    }

    public String SATNotUndefined() {
        StringBuilder sb = new StringBuilder();

        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                sb.append(cell.getRank(WIDTH)).append(" -").append(cell.getRank(WIDTH)).append(" 0").append('\n');
                clauses++;
            }
        }
        return sb.toString();
    }

    public List<Integer> getSurroundingRanks(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        List<Integer> values = new ArrayList<>();

        for (Dir dir : Dir.values()) {
            if (x + dir.x >= 0 && x + dir.x < WIDTH && y + dir.y >= 0 && y + dir.y < HEIGHT) {
                values.add(getCell(x + dir.x, y + dir.y).getRank(WIDTH));
            }
        }
        return values;
    }

    public String CNFToDimacs() {
        List<List<Integer>> cnf = allCellsToCNF();

        StringBuilder sb = new StringBuilder();

        for (List<Integer> integers : cnf) {
            for (Integer integer : integers) {
                sb.append(integer).append(" ");
            }
            sb.append("0\n");
            clauses++;
        }
        return sb.toString();
    }

    public List<List<Integer>> allCellsToCNF() {
        List<List<Integer>> CNF = new ArrayList<>();

        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.getValue() != -1) {
                    CNF.addAll(cellToCNF(cell));
                }
            }
        }
        return CNF;
    }

    public List<List<Integer>> cellToCNF(Cell cell) {
        if (solve_method.equals("naive") || solve_method.equals("naiveGenerator"))
            return tseytin(allDNF(getSurroundingRanks(cell), cell.getValue()));
        else if (solve_method.equals("improved") || solve_method.equals("improvedGenerator"))
            return improvedCNF(getSurroundingRanks(cell), cell.getValue());
        else {
            System.out.println("Invalid solve method");
            return null;
        }

    }

    public List<List<Integer>> tseytin(List<List<Integer>> dnf) {
        //In: List of list of variables in dnf, so the
        //inner lists are all conjunctions, the outer list
        //contains all disjunctions,
        //e.g. [[1,2],[3,4]] is (1 and 2) or (3 and 4)

        List<List<Integer>> cnf = new ArrayList<>();
        List<Integer> finalClause = new ArrayList<>();

        for (List<Integer> conjunction : dnf) {

            int additionalVar = nextFreeVariable;
            //nextFreeVariable is just a global counter to see which
            //variables are available to add

            for (int var : conjunction) {
                //All variables in a clause
                List<Integer> disj = new ArrayList<>();
                disj.add(var);
                disj.add(-additionalVar);
                cnf.add(disj);
                //Already solved the implication arrow of tseytin
            }
            finalClause.add(nextFreeVariable);
            nextFreeVariable++;
        }

        //cnf now contains lists of disjunctions.
        //Need to add new variable for whole clause
        finalClause.add(-nextFreeVariable);
        cnf.add(finalClause);
        List<Integer> superClause = new ArrayList<>();
        superClause.add(nextFreeVariable);
        cnf.add(superClause);

        nextFreeVariable++;

        return cnf;
        //Out: List of List of disjunctions
        //e,g. [[1,2],[3,4]] is (1 or 2) and (3 or 4)
    }


    public List<List<Integer>> allDNF(List<Integer> numbers, int value) {
//        List<Integer> negnum = new ArrayList<>();
//        for (int number : numbers)
//            negnum.add(number * -1);

        List<List<Integer>> CNFs = Generator.combination(numbers).simple(value).stream().collect(Collectors.toList());
        for (List<Integer> cnf : CNFs) {
            for (int num : numbers) {
                //Adds all numbers not yet in that clause in negative form
                if (!cnf.contains(num))
                    cnf.add(num * -1);
            }
        }
        return CNFs;
    }

    /*
        Start of SAT Encoding, Improved implementation

     */

    public List<List<Integer>> improvedCNF(List<Integer> surroundingRanks, int value) {
        List<List<Integer>> cnf = new ArrayList<>();

        if (value == surroundingRanks.size()) {
            for (int rank : surroundingRanks) {
                List<Integer> clause = new ArrayList<>();
                clause.add(rank);
                cnf.add(clause);
            }
        } else if (value == 0) {
            for (int rank : surroundingRanks) {
                List<Integer> clause = new ArrayList<>();
                clause.add(rank * -1);
                cnf.add(clause);
            }
        }
        else {
            cnf = counterCNF(surroundingRanks, value);
        }
        return cnf;
    }

    private List<List<Integer>> counterCNF(List<Integer> surroundingRanks, int value) {
        int n = surroundingRanks.size();

        List<Integer> x = surroundingRanks;
        List<List<Integer>> cnf = new ArrayList<>();
        List<List<Integer>> c = new ArrayList<>();

        for (int i = 0; i < n; i++){
            List<Integer> counter = new ArrayList<>();
            for (int j = 0; j < n; j++){
                counter.add(nextFreeVariable);
                nextFreeVariable++;
            }
            c.add(counter);
        }

        cnf.add(Arrays.asList(-x.get(0), c.get(0).get(0)));
        cnf.add(Arrays.asList(x.get(0), -c.get(0).get(0)));

        for (int i = 1; i < value; i++){
            cnf.add(Arrays.asList(-c.get(0).get(i)));
        }

        for (int i = 1; i < n; i++){
            cnf.add(Arrays.asList(-x.get(i), c.get(i).get(0)));
            cnf.add(Arrays.asList(-c.get(i-1).get(0), c.get(i).get(0)));
            cnf.add(Arrays.asList(-x.get(i), -c.get(i-1).get(value-1)));
            cnf.add(Arrays.asList(-c.get(i).get(0), x.get(i), c.get(i-1).get(0)));

            for (int j = 1; j < value; j++){
                cnf.add(Arrays.asList(-c.get(i-1).get(j), c.get(i).get(j)));
                cnf.add(Arrays.asList(-x.get(i), -c.get(i-1).get(j-1), c.get(i).get(j)));

                cnf.add(Arrays.asList(x.get(i), c.get(i-1).get(j), -c.get(i).get(j)));
                cnf.add(Arrays.asList(c.get(i-1).get(j-1), c.get(i-1).get(j), -c.get(i).get(j)));
            }
        }

        cnf.add(Arrays.asList(-x.get(n-1), -c.get(n-2).get(value-1)));
        cnf.add(Arrays.asList(c.get(n-1).get(value-1)));

        return cnf;
    }

}