package puzzle;

import javafx.scene.paint.Color;

public enum Type {
    U(Color.CORNFLOWERBLUE),
    B(Color.GRAY),
    W(Color.WHITE);

    private Color color;
    private static Type[] values = values();

    Type(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public Type next(){
        return values[(this.ordinal()+1) % values.length];
    }

    public Type previous(){
        return values[(this.ordinal()+2) % values.length];
    }
}
