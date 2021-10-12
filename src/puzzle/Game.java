package puzzle;

public class Game {
    private final int size;
    private final Cell[][] board;

    public Game(int size, String[][] config){
        this.size = size;
        board = new Cell[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
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
            if (x+dir.x >= 0 && x+dir.x < size && y+dir.y >= 0 && y+dir.y < size){
                if (board[y+dir.y][x+dir.x].getType() == Type.BLACK)
                    count++;
            }
        }
        return (expected == count);
    }

    public boolean checkBoard(){
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (!checkCell(x,y)){
                    return false;
                }
            }
        }
        return true;
    }



    public void printBoard(){
        for (int y=0; y<size; y++){
            for (int x=0; x<size; x++){
                System.out.print(board[y][x] + "  ");
            }
            System.out.print("\n");
        }

    }

    public void printTypes(){
        for (int y=0; y<size; y++){
            for (int x=0; x<size; x++){
                System.out.print(board[y][x].getType() + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }


}
