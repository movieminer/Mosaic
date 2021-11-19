package puzzle;

import java.util.List;

public class Game {
    private final int WIDTH, HEIGHT;
    private final Cell[][] board;

    public Game(int width, int height, String[][] config){
        this.WIDTH = width;
        this.HEIGHT = height;
        board = new Cell[height][width];

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                    board[y][x] = new Cell(x,y, Integer.parseInt(config[y][x]));
            }
        }
    }

    public void changeType(int x, int y, Type newType){
        board[y][x].setType(newType);
    }

    public Cell getCell(int x, int y){
        return board[y][x];
    }

    public String getCellValue(int x, int y){
        if(board[y][x].getValue() != -1)
            return board[y][x].toString();
        else
            return "";
    }

    public boolean checkCell(int x, int y){
        int expected = board[y][x].getValue();
        int count = 0;
        if (expected == -1){
            return true;
        }
        for (Dir dir : Dir.values() ) {
            if (x+dir.x >= 0 && x+dir.x < WIDTH && y+dir.y >= 0 && y+dir.y < HEIGHT){
                if (board[y+dir.y][x+dir.x].getType() == Type.BLACK)
                    count++;
            }
        }
        return (expected == count);
    }

    public boolean checkBoard(){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (!checkCell(x,y) || board[y][x].getType() == Type.UNDEFINED){
                    return false;
                }
            }
        }
        return true;
    }



    public void printBoard(){
        for (int y=0; y<HEIGHT; y++){
            for (int x=0; x<WIDTH; x++){
                System.out.print(board[y][x] + "  ");
            }
            System.out.print("\n");
        }

    }

    public void printTypes(){
        for (int y=0; y<HEIGHT; y++){
            for (int x=0; x<WIDTH; x++){
                System.out.print(board[y][x].getType() + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public String SATNotU(){
        StringBuilder sb = new StringBuilder();

        for (Cell[] cells : board){
            for (Cell cell : cells){
                sb.append(cell.getRank(WIDTH)).append(" -").append(cell.getRank(WIDTH)).append(" 0").append('\n');
            }
        }
        return sb.toString();
    }


}
