package puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TestMain {
    private static Game game;
    private static int WIDTH;
    private static int HEIGHT;

    public static void run(Game game, String solve_method){
        game.setSolve_method(solve_method);
        game.printBoard();
        //System.out.println(generateDIMACS(game));
        long start = System.currentTimeMillis();
        game.solveWithDIMACS(SATSolver.solve(game));
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        game.printTypes();
        System.out.println("Time elapsed: " + timeElapsed + " ms");
        System.out.println("-------------------------------------------\n");
    }

    public static void runSimple(Game game, String solve_method){
        game.setSolve_method(solve_method);
        long start = System.currentTimeMillis();
        game.solveWithDIMACS(SATSolver.solve(game));
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Size: " + WIDTH + " x " + HEIGHT);
        System.out.println("Time elapsed: " + timeElapsed + " ms");
        System.out.println("-------------------------------------------");

    }

    public static long runExtended(Game game, String solve_method){
        game.setSolve_method(solve_method);
        long start = System.currentTimeMillis();
        game.solveWithDIMACS(SATSolver.solve(game));
        game.printTypes();
        long finish = System.currentTimeMillis();
        return finish - start;
    }

    public static Game createGameFromEx(Example example, String solve_method){
        WIDTH = example.getWidth();
        HEIGHT = example.getHeight();
        return new Game(WIDTH, HEIGHT, solve_method, example.board());
    }


    private static List<String> readFile(String input){
        List<String> downloads = new ArrayList<>();

        File file = new File(input);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (sc.hasNextLine())
            downloads.add(sc.nextLine());

        return downloads;
    }

    public static void solvePuzzles() {
        String currentsize = "";
        List<String> examples = readFile("teststring.txt");
        for (String ex : examples) {
            String size = ex.split(":")[0];
            if (!currentsize.equals(size)) {
                System.out.println("Size: " + size);
                currentsize = size;
            }
            System.out.println(runExtended(createGameFromEx(new Example(ex), "improved"), "improved"));
        }
    }

    public static void generatePuzzles(int amount){
        int[] sizes = {5,10,15};

        for (int size : sizes){
            System.out.println("\nSize: " + size + "x" + size);
            List<Game> gamesToSolve = new ArrayList<>();
            for (int i = 0; i < amount; i++){
                long start = System.currentTimeMillis();
                Game generatedGame = new Generator(size,size).generateNewGame("improvedGenerator");
                long finish = System.currentTimeMillis();
                System.out.println(finish - start);
                gamesToSolve.add(generatedGame);
            }
            System.out.println("\nSolving ...");
            for (int i = 0; i < amount; i++){
                System.out.println(runExtended(gamesToSolve.get(i), "naive"));
            }
        }
    }

    public static void generatePuzzlesOnTime(int amount, int[] sizes, String generator, String solver){
        int time = amount*60000;

        for (int size : sizes){
            System.out.println("\nSize: " + size + "x" + size);
            List<Game> gamesToSolve = new ArrayList<>();
            long start_time = System.currentTimeMillis();
            System.out.println("Generating ...");
            while(System.currentTimeMillis() - start_time <= time){
                long start = System.currentTimeMillis();
                Game generatedGame = new Generator(size,size).generateNewGameTimeRandom(generator, start_time, time);
                long finish = System.currentTimeMillis();
                if (generatedGame.getSolve_method().equals("time")){
                    break;
                }
                gamesToSolve.add(generatedGame);
                System.out.println(finish - start);
            }
            System.out.println("Generated "+gamesToSolve.size()+ " puzzle(s) in " + amount + " minutes.");
            System.out.println("\nSolving ...");
            for (Game g : gamesToSolve) {
                System.out.println(runExtended(g, solver));
            }
        }
    }

    public static void main(String[] args) {
        int time =  Integer.parseInt(args[0]);
        int[] sizes = Arrays.stream(args[1].split(",")).mapToInt(Integer::parseInt).toArray();
        String generator = args[2];
        String solver = args[3];

        solvePuzzles();
        //generatePuzzles(100);
        //generatePuzzlesOnTime(time, sizes, generator, solver);
    }
}