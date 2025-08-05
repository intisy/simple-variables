package io.github.intisy.simple.variables;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class GithubVariables extends Variables {
    final String accessToken;
    final String apiUrl;
    private String sha;

    public GithubVariables(String url, String accessToken) {
        this(createTempFileFromUrl(url, accessToken), url, accessToken);
    }

    public GithubVariables(File file, String url, String accessToken) {
        super(file);
        this.accessToken = accessToken;
        this.apiUrl = getApiUrl(url);
        this.sha = getSha(apiUrl, accessToken);
    }

    private static File createTempFileFromUrl(String url, String accessToken) {
        try {
            String apiUrl = getApiUrl(url);
            String content = getFileContent(apiUrl, accessToken);
            Path tempFile = Files.createTempFile("variables", ".properties");
            Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
            tempFile.toFile().deleteOnExit();
            return tempFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getApiUrl(String githubUrl) {
        return githubUrl.replace("github.com/", "api.github.com/repos/").replace("/blob/main", "/contents");
    }

    private static String getFileContent(String apiUrl, String accessToken) {
        JsonObject json = getJsonFromApi(apiUrl, accessToken);
        String content = json.get("content").getAsString();
        return new String(Base64.getDecoder().decode(content));
    }

    private String getSha(String apiUrl, String accessToken) {
        return getJsonFromApi(apiUrl, accessToken).get("sha").getAsString();
    }

    private static JsonObject getJsonFromApi(String apiUrl, String accessToken) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if (accessToken != null) {
                connection.setRequestProperty("Authorization", "token " + accessToken);
            }
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            return new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(String name, Object value) {
        super.set(name, value);
        if (accessToken != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                String encodedContent = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Authorization", "token " + accessToken);
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonInputString = "{\"message\":\"Variable " + name + " updated\",\"content\":\"" + encodedContent + "\",\"sha\":\"" + this.sha + "\"}";

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                if (connection.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
                }

                JsonObject responseJson = new Gson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);
                this.sha = responseJson.getAsJsonObject("content").get("sha").getAsString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
