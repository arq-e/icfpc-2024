package submittor;

import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.IOUtils;
import parsing.Parser;

public class Submittor {
    static final String URL = "https://boundvariable.space/communicate";

    
    public static String submit(String s) throws IOException, InterruptedException{
        String apiToken = IOUtils.getApiToken();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .header("Authorization", String.format("Bearer %s", apiToken))
            .uri(URI.create(URL))
            .POST(HttpRequest.BodyPublishers.ofString(s))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        //System.out.println(response.body());

        return response.body();
    }
}
