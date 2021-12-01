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
    Example test = new Example("50x50:a23122b334e45b3a3a4453c3a23d1a3a4a32i55d6a47b33c3d34f645a4a5a5d4d5a653568e53444a3455544454a445564a33468a3b13a4a7b4667666b44b4243535a4a4544a6a7421124654b3a4a57b2a4a55c46a5544244664b555a5a52123345544c5a5b6553a3a7a3a66a7b3557a3213a6b65345a55432a44a3a344666a3357665a5a7b2a6752a2345b6535a9887522d343578644b7a5a54a6a6c53a45a5b6a6467998a32a23a4a32a56656b676543a454b5c5c6b564b77a6c34b4433c6a7766c55a6a76a44a667a74a45545c5455a3a343325b76a44545a77c66a5a66b76j66b3a33a5c76666423355a6a77a646a6b676a34b4a355b33a2c56a76443a1124554a345565a44a4b6a45a543a5564b113b6677a4b5a3344544a3568c3a3455456a3334445a32c566a66a4335554666c3a6a865a4556a23a6a3354555b02a776a5a75b6775556a5a5a4a776646574d33364645b23c54466422557a665a3444a3a6a6a67b214a6b55654d55b22464a255a54a555a4a333a6a5b7b1a6b345a64c3a45c24a4a54a4a56554a5a31b6a45b1c7c566a53a3b642103452a3435a757a77a534a7a44b112a55b455565b76a4a3b54b344a45b655b56a754455a3a5a43a3a3b5b655332335d5a4345b4b6b765455a543c45a345b53a3a45a556a5457a434c22b77655444665443556444a46a3a4433c554b6555654b444b5466a33a2345c35a5a7b5433a5c543c5a6423566a542a686544a34654335564c66544a6a223a1a1a5b4677766b4a76a4322b55345b5a2a5a44a44312b1a1b4a3b8986c4a5654a11d5687b1b577532a34a53b3a4546c76a5b44a44422a32a577b4b335553445a65a5a5a332a5c4a3a344a555b2112a7855b1a344b6664c775323d2d334a5a565b2c8a43b0a01123555a5b896312a4b3234b3a33a5a66a455a654b3b3a3b5b34b89a3123a6a523a4a55323a7b6a556744a4c56b335a4a3b653333a445a32b54203687a776666446a43688c565c4a6a3233a4a5a3a4b44234a5b5b887666a46b32a34b1b34a3a55a35a6b3a33a2a344454557775b56b3a24a5b112344b55a46a7f45a33a5a4b6655a6a5a22a2b6a33a3344b4a235b44b5a443435675a456543b4a1a2b78e55a2344345a5e55523b776a7653a4c5222b56a4a5677b22334a65d354a23577543b76a55a5a322e44b5553b54456c23a33a4a67854a666a6b55a433a6666a7654565a5a324b5c445b3b85a1346665a43a44c8a87a4b33467a44b4b5b56542a57a644445b5c5b77a7c222434c54a4a66a335663a2a44a32346b4a3b8a6a676312333346a7a5a4b543a456553a34a4a435355a4a687766a642a33b667a64c76a554566634453a12c4a4a86c55a44443347c45d5a566a55664b8c23344b365d44233a2a36a5b64b4456665b6546a74a0133a3b24b66543b56544b4445b5546555b5c78a3a2b54a2b4a67a5a4c56e33b6555a7b78b666a5a1a55422b2a3d3a545b443c0f55b66g2a3e2a");
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
            System.out.println(timeElapsed);
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
