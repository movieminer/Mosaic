package puzzle;

public enum Dir {
    NW(-1,-1),
    N(0,-1),
    NE(1,-1),
    W(-1,0),
    C(0,0),
    E(1,0),
    SW(-1,1),
    S(0,1),
    SE(1,1);

    int x,y;

    Dir(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int ord(){
        return this.ordinal();
    }
}
