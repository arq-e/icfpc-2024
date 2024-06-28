import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import parsing.Parser;
import submittor.Submittor;

public class Main {
    static final String lambdaPath = "inputs/lambdaman/";
    static final String spaceshipPath = "inputs/spaceship/";
    public static void main(String[] args) throws IOException, InterruptedException {
        /*try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("input"))))) {
            Parser.parse(bw.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }        */
        //String s = "S" + Parser.convertString("get spaceship1");
        //System.out.println(s);
        //Parser.parse(s);

        //String res = Submittor.submit(s);
        //Parser.parse(res);

        //loadLambdaman();
        loadSpaceship();

    }

    public static void loadLambdaman() throws InterruptedException{
        for (int i = 1; i <= 21; ++i) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(lambdaPath + i))))) {
                String s = "S" + Parser.convertString("get lambdaman"+ i);                
                String res = Submittor.submit(s);                
                bw.write(Parser.parseString(res));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(100);
        }
    }

    public static void loadSpaceship() throws InterruptedException{
        for (int i = 1; i <= 25; ++i) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(spaceshipPath + i))))) {
                String s = "S" + Parser.convertString("get spaceship"+ i);                
                String res = Submittor.submit(s);                
                bw.write(Parser.parseString(res));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(100);
        }
    }
}
