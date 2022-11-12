package puzzle;

public class Cell {
    private int x,y;
    private int value;
    Type type = Type.U;

    public Cell(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX(){
       return x;
    }

    public int getY(){
        return y;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public Type getType(){
        return type;
    }

    public void setType(Type newType){
        type = newType;
    }

    public int getRank(int WIDTH){
        return (y*WIDTH + x + 1);
    }

    @Override
    public String toString(){
        if (value >= 0)
            return String.valueOf(value);
        else
            return "*";
    }
}
