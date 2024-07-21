package com.github.WildePizza;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

@SuppressWarnings("unused")
public class VariablesBuilder {
    private String projectName;
    private String filePath;
    private String username;
    private String accessToken;
    private boolean isGithub;
    public VariablesBuilder withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
    public VariablesBuilder withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    public VariablesBuilder withFilePath(File file) {
        this.filePath = file.getAbsolutePath();
        return this;
    }
    public VariablesBuilder withGithubFile(String url) {
        return withGithubFile(url, null);
    }
    public VariablesBuilder withGithubFile(String url, String accessToken) {
        try {
            Path tempDir = Files.createTempDirectory("variables");
            String regex = "https://.*/blob/.*/";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            String repo = url.split("/blob/")[0];
            CloneCommand command = Git.cloneRepository()
                    .setURI(repo)
                    .setDirectory(tempDir.toFile());
            username = url.replace("https://github.com/", "").split("/")[0];
            if (accessToken != null)
                command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, accessToken));
            command.call();
            this.filePath = tempDir.toFile().getAbsolutePath() + "/" + matcher.replaceAll("");
            this.accessToken = accessToken;
            isGithub = true;
            tempDir.toFile().deleteOnExit();
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public SimpleVariables build() {
        String filePath;
        if (projectName != null) {
            File appDataDir;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                appDataDir = new File(System.getenv("APPDATA") + File.separator + projectName);
            } else {
                appDataDir = new File(System.getProperty("user.home") + File.separator + ".config" + File.separator + projectName);
            }
            if (!appDataDir.exists())
                appDataDir.mkdirs();
            filePath = appDataDir + File.separator + "variables.dat";
        } else if (this.filePath != null) {
            filePath = this.filePath;
        } else {
            throw new IllegalArgumentException("No project name or file path specified.");
        }
        if (isGithub)
            return new GithubVariables(filePath, username, accessToken);
        else
            return new SimpleVariables(filePath);
    }
}
