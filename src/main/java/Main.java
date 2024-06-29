import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import evaluation.Evaluator2;
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
        // load("3d", 1, max3d);
        // load_task("3d");
        // load_task("efficiency");
        // load_task("lambdaman");
        // load_task("spaceship");
        // load_task("index");
        // solve("lambdaman", 11, 20);
        // solve("spaceship", 21, 21);
        // load("spaceship", 22, maxSpaceship);
        // load("lambdaman", 6, maxLambdaman);
        // test3D(3, -1, 0);

        // solve("3d", 2, 2);

        // String s = "S'%4}).$%8";
        // String s = "L# B. v# v# B$ L# SU";
        // String s = "S" + Parser.convertString("echo efficiency");
        // System.out.println(s);
        // String s = "B$ L$ v$ B. SB%,,/ S}Q/2,$_";
        String s = "B$ L# B. v# v# SU";
        String res = Submittor.submit(s);
        Parser.parse(res);
    }


    public static void test3D(int i, int A, int B) throws InterruptedException{
        try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(outputsPath + "3d" + "/" + i))))) {
            String in;
            String out;
            String prog = "test 3d " + A + " " + B + "\n";
            while ((in = bw.readLine()) != null) {
                prog += in + "\n";
            }
            out = "S" + Parser.convertString(prog);
            String res = Submittor.submit(out);
            Parser.parse(res);
            System.out.println("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String lambdify(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            int pos = (int) ch;
            if (pos < 33) {
                sb.append((char)(pos + 33));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    public static void solve(String name, int start, int end) throws InterruptedException{
        for (int i = start; i <= end; ++i) {
            try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(outputsPath + name + "/" + i))))) {
                String in;
                String out;
                if (name == "3d") {
                    String prog = "solve " + name + i + "\n";
                    while ((in = bw.readLine()) != null) {
                        prog += in + "\n";
                    }
                    out = "S" + Parser.convertString(prog);
                } else {
                    in = bw.readLine();
                    out = "S" + Parser.convertString("solve " + name + i + " " + in);
                }
                String res = Submittor.submit(out);
                //Parser.parse(res);
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
                String s = Parser.convertString("get " + name + i);                
                String res = Submittor.submit(s);  
                System.out.println(Parser.parseString(res));              
                bw.write(Parser.parseString(res));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (StringIndexOutOfBoundsException e) {
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

    public static void test3D(int i, int A, int B) throws InterruptedException{
        try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(outputsPath + "3d" + "/" + i))))) {
            String in;
            String out;
            String prog = "test 3d " + A + " " + B + "\n";
            while ((in = bw.readLine()) != null) {
                prog += in + "\n";
            }
            out = Parser.convertString(prog);
            String res = Submittor.submit(out);
            Evaluator2 evaluator2 = new Evaluator2();
            System.out.println(evaluator2.parse(res));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
