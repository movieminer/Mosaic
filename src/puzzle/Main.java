package puzzle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Arrays;

public class Main extends Application {
    Example test = new Example("10x10:1e654c2a346d3a023556b4d245c244555a3a44666a54b5g33d6a7b3a322e2a2001a5a2");
    int SIZE = test.getSize();
    int GRID_SIZE = 50;
    Game game = new Game(SIZE, test.board());

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        createBoard(root);
        root.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int x = getCoord(mouseEvent.getX());
            int y = getCoord(mouseEvent.getY());
            if (mouseEvent.getButton() == MouseButton.PRIMARY)
                game.getCell(x,y).setType(game.getCell(x,y).getType().next());
            else if (mouseEvent.getButton() == MouseButton.SECONDARY)
                game.getCell(x,y).setType(game.getCell(x,y).getType().previous());
            createBoard(root);
            game.printTypes();
            if (game.checkCell(x,y)){
                if(game.checkBoard()){
                    System.out.println("CORRECT SOLUTION!");
                }
            }

        });
        primaryStage.setTitle("Mosaic Puzzle");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public int getCoord(double i){
        return (int)Math.floor(i/GRID_SIZE);
    }

    public void createBoard(GridPane root){
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                Rectangle box = new Rectangle(20,20,GRID_SIZE,GRID_SIZE);
                box.setFill(game.getCell(x,y).getType().getColor());
                box.setStroke(Color.BLACK);
                Text text = new Text(game.getCellValue(x,y));
                text.setFont(Font.font(20));
                StackPane stack = new StackPane();
                stack.getChildren().addAll(box, text);
                root.add(stack,x,y);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
