package submittor;

import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import parsing.Parser;

public class Submittor {
    public static String submit(String s) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .header("Authorization", "Bearer d39fce9f-763a-4ad0-a6d3-9e16161801c3")
            .uri(URI.create("https://boundvariable.space/communicate"))
            .POST(HttpRequest.BodyPublishers.ofString(s))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());

        return response.body();
    }
}
