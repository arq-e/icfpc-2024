package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import parsing.Parser;
import submittor.Submittor;


public class IOUtils {
    static final String TOKEN_PATH = ".api_token";
    static final String lambda = "lambdaman";
    static final String space = "spaceship";
    static final String d3 = "3d";
    static final String efficiency = "efficiency";

    static final String tasksPath = "tasks/";
    static final String inputsPath = "inputs/";
    static final String outputsPath = "outputs/";

    public static String getApiToken() {
        String res = null;
        try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(TOKEN_PATH))))) {
            res = bw.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }       
        
        return res;
    }

    public static String[][] load3DModel(int task) {
        String[][] res = null;
        try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(outputsPath + d3 + "/" + task))))) {
            List<String> strings = new ArrayList<>();
            String in;
            while ((in = bw.readLine()) != null) {
                strings.add(in);
            }

            res = new String[strings.size()][];
            for (int i = 0; i < res.length; ++i) {
                res[i] = strings.get(i).trim().split(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


}
