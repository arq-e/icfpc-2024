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
    static final String lambda = "lambdaman";
    static final String space = "spaceship";
    static final String d3 = "3d";

    static final String tasksPath = "tasks/";
    static final String inputsPath = "inputs/";
    static final String outputsPath = "outputs/";

    static final int maxLambdaman = 21;
    static final int maxSpaceship = 25;
    static final int max3d = 12;
    public static void main(String[] args) throws IOException, InterruptedException {
        load("3d", 1, max3d);
        // load_task("lambdaman");
        // load_task("spaceship");
        // solve("lambdaman", 1, 5);
        // solve("spaceship", 1, 10);
        // load("spaceship", 1, maxSpaceship);
        // load("lambdaman", 1, maxLambdaman);

        // String s = "S'%4}).$%8";
        // String in = "";
        // String s = "S" + Parser.convertString(in);
        // String res = Submittor.submit(s);
        // Parser.parse(res);
    }

    public static void solve(String name, int start, int end) throws InterruptedException{
        for (int i = start; i <= end; ++i) {
            try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(outputsPath + name + "/" + i))))) {
                String in = bw.readLine();
                String out = "S" + Parser.convertString("solve " + name + i + " " + in);
                String res = Submittor.submit(out);
                Parser.parse(res);
                System.out.println("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(100);
        }
    }

    public static void load(String name, int start, int end) throws InterruptedException{
        for (int i = start; i <= end; ++i) {
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(inputsPath + name + "/" + i))))) {
                String s = "S" + Parser.convertString("get " + name + i);                
                String res = Submittor.submit(s);                
                bw.write(Parser.parseString(res));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.sleep(100);
        }
    }

    public static void load_task(String name) throws InterruptedException{
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(tasksPath + name + ".md"))))) {
            String s = "S" + Parser.convertString("get " + name);                
            String res = Submittor.submit(s);                
            bw.write(Parser.parseString(res));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
