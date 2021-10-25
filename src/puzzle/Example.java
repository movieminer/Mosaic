package puzzle;

import java.util.ArrayList;
import java.util.Scanner;

public class Example {
    private final String[] strings = {
                        "5x5:a1a4c5d46b5f0a",
                        "5x5:a5b3b6d6863a6c45a2",
                        "5x5:b0a2442a33b5b5a5a2a4b",
                        "5x5:3c4c5a4a7a34b5b465a",
                        "5x5:f23a3b56a0b9f",
                        "5x5:a1b00a22a244c4a6a24c",
                        "5x5:e4b2c5a2a24b0a4a3",
                        "5x5:0a4d5a6a2b6d5a02b",
                        "5x5:b2a00e54a146g0",
                        "5x5:3b33b7b6885a5a96f",
                        "5x5:a3a543a6a54a5d5b3b0a",
                        "5x5:d4a455a4a6e304d",
                        "5x5:a0b1a2e43g201a",
                        "5x5:e2a354a2a66a4c2a5a3",
                        "5x5:44a3f4a85b5b2b3b"};
    private final String data;
    private final int WIDTH, HEIGHT;
    private final int puzzle_instance;

    public Example(){
        this.puzzle_instance = (int)(Math.random()* strings.length);
        String[] str = strings[(puzzle_instance)].split(":");
        Scanner scanner = new Scanner(str[0]);

        this.WIDTH = Integer.parseInt(str[0].split("x")[0]);
        this.HEIGHT = Integer.parseInt(str[0].split("x")[1]);
        this.data = str[1];
    }

    public Example(String config){
        this.puzzle_instance = 0;
        String[] str = config.split(":");
        Scanner scanner = new Scanner(str[0]);

        this.WIDTH = Integer.parseInt(str[0].split("x")[0]);
        this.HEIGHT = Integer.parseInt(str[0].split("x")[1]);
        this.data = str[1];
    }

    public int getWidth(){
        return WIDTH;
    }

    public int getHeight(){
        return HEIGHT;
    }

    public String getString(){
        return data;
    }

    public int getInstance(){
        return puzzle_instance;
    }

    public String[][] board() {
        String str = data;
        ArrayList<String> converted = new ArrayList<String>();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 58){
                for (int j = 0; j < str.charAt(i)-96; j++) {
                    converted.add("-1");
                }
            }
            else
                converted.add(String.valueOf(str.charAt(i)));
        }
        return convertDoubleArray(converted);
    }

    public String[][] convertDoubleArray(ArrayList<String> arraystr) {
        String[][] result = new String[HEIGHT][WIDTH];
        for (int i = 0; i < arraystr.size(); i++) {
            result[(int) Math.floor(i/WIDTH)][i%WIDTH] = arraystr.get(i);
        }
        return result;
    }
}
