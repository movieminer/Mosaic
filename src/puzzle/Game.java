package puzzle;

import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private final int WIDTH, HEIGHT;
    private final Cell[][] board;
    private int nextFreeVariable;
    private int clauses = 0;

    public Game(int width, int height, String[][] config) {
        this.WIDTH = width;
        this.HEIGHT = height;
        nextFreeVariable = WIDTH * HEIGHT + 1;
        board = new Cell[height][width];

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

    public int getVariableCount() {
        return nextFreeVariable - 1;
    }

    public int getClausesCount() {
        return clauses;
    }

    public String getCellValue(int x, int y) {
        if (board[y][x].getValue() != -1)
            return board[y][x].toString();
        else
            return "";
    }

    public void solveWithDIMACS(String sol){
        Scanner scanner = new Scanner(sol);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (scanner.nextInt() < 0)
                    changeType(x,y,Type.WHITE);
                else
                    changeType(x,y,Type.BLACK);
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
                if (board[y + dir.y][x + dir.x].getType() == Type.BLACK)
                    count++;
            }
        }
        return (expected == count);
    }

    public boolean checkBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (!checkCell(x, y) || board[y][x].getType() == Type.UNDEFINED) {
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

    public List<List<Integer>> allCellsToDNF() {
        List<List<Integer>> CNF = new ArrayList<>();

        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.getValue() != -1) {
                    CNF.addAll(CellToCNF(cell));
                }
            }
        }
        return CNF;
    }

    public String CNFToDimacs() {
        List<List<Integer>> cnf = allCellsToDNF();
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

    public List<List<Integer>> CellToCNF(Cell cell) {
        return tseytin(allDNF(getSurroundingRanks(cell), cell.getValue()));
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
        List<Integer> negnum = new ArrayList<>();
        for (int number : numbers)
            negnum.add(number * -1);

        List<List<Integer>> CNFs = Generator.combination(numbers).simple(value).stream().collect(Collectors.toList());
        for (List<Integer> cnf : CNFs) {
            for (int num : numbers) {
                if (!cnf.contains(num))
                    cnf.add(num * -1);
            }
        }
        return CNFs;
    }

}
