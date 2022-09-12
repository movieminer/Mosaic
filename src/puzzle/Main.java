package puzzle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main extends Application {
    Example test = new Example("48x48:f3b43f32e1b1g2a2e3a1c3b6h42d0a6b33c3b2g54d4a3c5a3a6a4c35b2c77b3b2d676a5b3a545a42a3b4c4b4a35a4a23c87a2c5f4a333a66b4a4b2b4a2f6c1f34b43a5b3b5a644a3d5a43c5a7a98c5b33a2a3b22d213a7b3e66a4b5a4a4a67g01d7b4d213a8a4a3a2a43a2a2a4a5b3a668a5d2223c7a30134b6f13a4b42b3a221c753a67442a3b66532a444455a5a33a3b2c21a3b35a7a4c75a23a5d5b676a44b3c4c5b30c3a555a4b7a4c2546b545554c5g32b3a1a4a57c34a65534a3a5a76a6b46a322a5a3a2a56b4a2a5a5b3b4a45a53a33a555a6b234a3a34a44a2d52d6d334a33a54a4456543655245b135a5b26b7c4a7b2a4b6f55b6f2a33a3b7a6c7i3i3a5c3f7a6a5e676b75c2a7a6a5c5b54h46a5b4a5a44a5a67a3b43a3c65b5a3a4a4a31b7e7i6d5c5a455b6a53a33a46d4a8a3a35674a45c58a7c3a6a55a6a3b32a3a43a4b4e3b5a5d4b65556a3c6a55c2g6a6a5a8a55a4h5b5b6b1a7a543a2b2c433b3a57a76b5a4a3a4a44a2b3c4c45444a4b23a4b34a6b6755b7a7a6a3f3g4d34b33a4a4f44e75a4d5c4b5c44a656c454d3a6b3a4f43g8b44b3a44a7b4a7b5a5452a46c3a2b5a3b22a34e6b3a5a45c756b2d4a4a5b21a44b4c3b3b5b4f6565a4a4c6b5544c3b54a3334543a3a23a4a4c34a5a5a5c4d653447b43c233c22a6a5c5a35a6a4446a44d4g6c31c335b1c44a5a3367a6b4c65b12a3a4a5c4a423b6a6d56a4435c966a5b4b8b2a32e6e36a5a56a4a4a3a3c4a7a524a6b776c112c6a3b4b5b6e64b3d5b4a6b3b8a4a3a2b3567a5b4c4d5a643b55b534a56a5b787d4434f86a13454a34464336b44c356a4a5775b2b3a45b998b5a5a535b4b12c4c33a5644d5a2a5b3b7b7a64a444b55a32c6a5a3b13d455d3a2a4i3d6a54b12g0a3b6a4a56a3a34234a4a4b43a3k3a2c5e5a7654c5a5c3a4a7a5a2a124a43575c1a5b643a6443655b7554a4b44b7a5a4b3b324a5d2d45c4g6a45a5b44b6a5d2b2a3a455a4b4c68b6c44e6b43335a4c11a1b4a255b65a3a3b5a5a6a3a3a55b5a53334c66b2b57a6b45a86b4c56b4b4a2b86e3a3a4a5a3a3a57d345b2a2b5a3b4a44a5a8b5a4d45c7a7a8a6a455a3d3b4b4c3a6e7a4d2d46b575633b33a4d3a42a1e5a4a5d111a34d45j1b2"
                                );
    int WIDTH = test.getWidth();
    int HEIGHT = test.getHeight();
    int GRID_SIZE = 50;
    Game game = new Game(WIDTH, HEIGHT, test.board());
    String filename = "dimacs.cnf";

    @Override
    public void start(Stage primaryStage) {
        BorderPane border = new BorderPane();

        GridPane root = new GridPane();
        GridPane buttons = new GridPane();

        createBoard(root);
        addButtons(buttons, root);
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
        border.setCenter(root);
        border.setTop(buttons);
        BorderPane.setMargin(buttons,new Insets(10,5,10,5));

        primaryStage.setTitle("Mosaic #" + test.getInstance());
        primaryStage.setScene(new Scene(border));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public int getCoord(double i){
        return (int)Math.floor(i/GRID_SIZE);
    }

    public void addButtons(GridPane buttons, GridPane root){
        Button but = new Button("Solve");
        Button newgame = new Button("New game");
        but.setOnAction(actionEvent -> {
            long start = System.currentTimeMillis();
            game.solveWithDIMACS(solve(game));
            createBoard(root);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Time elapsed: " + timeElapsed + " ms");
        });
        newgame.setOnAction(actionEvent -> {
            test = new Example();
            WIDTH = test.getWidth();
            HEIGHT = test.getHeight();
            GRID_SIZE = 50;
            game = new Game(WIDTH, HEIGHT, test.board());
            root.getChildren().clear();
            createBoard(root);
        });
        buttons.add(but, 0,0);
        buttons.add(newgame,1,0);
    }

    public void createBoard(GridPane root){
        if(HEIGHT > 15 || WIDTH > 15){
            GRID_SIZE = 900/HEIGHT;
        }
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Rectangle box = new Rectangle(20,20,GRID_SIZE,GRID_SIZE);
                box.setFill(game.getCell(x,y).getType().getColor());
                box.setStroke(Color.BLACK);
                Text text = new Text(game.getCellValue(x,y));
                text.setFont(Font.font(20));

                if(HEIGHT > 15 || WIDTH > 15)
                    text.setFont(Font.font(10));

                StackPane stack = new StackPane();
                stack.getChildren().addAll(box, text);
                root.add(stack,x,y);
            }

        }
    }

    public String generateDIMACS(Game game){
        StringBuilder sb = new StringBuilder();
        String dimacs = game.SATNotUndefined() + game.CNFToDimacs();

        sb.append("c dimacs of Mosaic\n");
        sb.append("p cnf ").append(game.getVariableCount()).append(" ").append(game.getClausesCount()).append("\n");
        sb.append(dimacs);
        return sb.toString();
    }

    public void createFile(Game game){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(generateDIMACS(game));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String solve(Game game){
        createFile(game);

        ISolver solver = SolverFactory.newDefault();
        String solution = null;
        Reader reader = new DimacsReader(solver);
        try {
            IProblem problem = reader.parseInstance(filename);
            if (problem.isSatisfiable()) {
                System.out.println(" Satisfiable !");
                solution = reader.decode(problem.model());
                System.out.println(solution);
            } else {
                System.out.println(" Unsatisfiable !");
            }
        } catch (ContradictionException | IOException | ParseFormatException | TimeoutException e) {
            e.printStackTrace();
        }
        return solution;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
