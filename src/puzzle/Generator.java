package puzzle;

import static java.lang.Math.random;
import static java.lang.Math.round;

public class Generator {
    private static int width, height;

    public Generator(int width, int height) {
        Generator.width = width;
        Generator.height = height;
    }

    public String generate(){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= width * height; i++) {
            if (round(random()) == 0)
                sb.append("-");
            sb.append(i).append(" ");
            }
        return sb.toString();
    }

    public void generateGame(){
        Game game = new Game(width, height);
        String solution = generate();
        System.out.println(solution);
        game.solveWithDIMACS(solution);
        game.printTypes();
    }

    public static void main(String[] args) {
        Generator generator = new Generator(10, 10);
        generator.generateGame();
    }
}
