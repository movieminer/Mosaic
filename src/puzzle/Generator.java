package puzzle;

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
                if (game.getCell(x,y).getValue()==-1)
                    break;

                int old_val = game.getCell(x,y).getValue();
                game.getCell(x,y).setValue(-1);
                game.printBoard();
                if (SATSolver.solve(game)!=null) {
                    game.getCell(x, y).setValue(old_val);
                }
                else {
                    uniqueSolve(game);
                }
            }
        }


    }

    public Game generateNewGame(String solve_method){
        Game game = new Game(WIDTH, HEIGHT, solve_method);

        generateTypes(game);
        generateClues(game);

        uniqueSolve(game);

        return game;
    }

}
