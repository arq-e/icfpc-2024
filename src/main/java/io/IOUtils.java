package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class IOUtils {
    static final String TOKEN_PATH = ".api_token";

    public static String getApiToken() {
        String res = null;
        try (BufferedReader bw = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(TOKEN_PATH))))) {
            res = bw.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }       
        
        return res;
    }
}
