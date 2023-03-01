package puzzle;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.round;

public class Generator {
    private final int WIDTH, HEIGHT;

    public Generator(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void generateTypes(Game game){

        for (int y = 0; y < HEIGHT; y++){
            for (int x = 0; x < WIDTH; x++){
                game.changeType(x,y, Type.random());
            }
        }
    }

    public void generateClues(Game game){
        for (int y=0; y < HEIGHT; y++){
            for (int x=0; x < WIDTH; x++){
                game.getCell(x,y).setValue(countBlack(game, x,y));
            }
        }
    }

    public int countBlack(Game game, int x, int y){
        int count=0;
        Cell[][] board = game.getBoard();

        for (Dir dir : Dir.values()) {
            if (x + dir.x >= 0 && x + dir.x < WIDTH && y + dir.y >= 0 && y + dir.y < HEIGHT) {
                if (board[y + dir.y][x + dir.x].getType() == Type.B)
                    count++;
            }
        }
        return count;
    }

    private void uniqueSolve(Game game) {
        for (int y=0; y < HEIGHT; y++){
            for (int x=0; x < WIDTH; x++){
                int old_val = game.getCell(x,y).getValue();
                game.getCell(x,y).setValue(-1);
                if (SATSolver.solve(game)!=null) {
                    game.getCell(x, y).setValue(old_val);
                }
                game.printBoard();
            }
        }
    }

    private void uniqueSolveTime(Game game, long start_time, long time) {
        for (int y=0; y < HEIGHT; y++){
            for (int x=0; x < WIDTH; x++){
                if (System.currentTimeMillis() - start_time >= time) {
                    game.setSolve_method("time");
                    return;
                }

                int old_val = game.getCell(x, y).getValue();
                game.getCell(x, y).setValue(-1);
                if (SATSolver.solve(game) != null) {
                    game.getCell(x, y).setValue(old_val);
                }

            }
        }
    }


    private void uniqueSolveRandom(Game game, long start_time, long time) {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();

        for (int y = 0; y < HEIGHT; y++){
            for (int x = 0; x < WIDTH; x++){
                cells.add(new Pair<>(x,y));
            }
        }

        while(!cells.isEmpty()) {
            int random = new Random().nextInt(cells.size());
            Pair<Integer, Integer> cell = cells.get(random);
            int x = cell.getKey();
            int y = cell.getValue();

            if (System.currentTimeMillis() - start_time >= time) {
                game.setSolve_method("time");
                return;
            }

            int old_val = game.getCell(x, y).getValue();
            game.getCell(x, y).setValue(-1);
            if (SATSolver.solve(game) != null) {
                game.getCell(x, y).setValue(old_val);
            }
            cells.remove(random);
        }

    }

    public Game generateNewGame(String solve_method){
        Game game = new Game(WIDTH, HEIGHT, solve_method);

        generateTypes(game);
        generateClues(game);

        uniqueSolve(game);
        game.clearBoard();
        return game;
    }

    public Game generateNewGameTime(String solve_method, long start_time, long time){
        Game game = new Game(WIDTH, HEIGHT, solve_method);

        generateTypes(game);
        generateClues(game);

        uniqueSolveTime(game, start_time, time);
        game.clearBoard();
        return game;
    }

    public Game generateNewGameTimeRandom(String solve_method, long start_time, long time){
        Game game = new Game(WIDTH, HEIGHT, solve_method);

        generateTypes(game);
        generateClues(game);

        uniqueSolveRandom(game, start_time, time);
        game.clearBoard();
        //System.out.println(game.toId());
        return game;
    }
}