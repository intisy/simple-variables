package io.github.intisy.simple.variables;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubVariables extends SimpleVariables {
    final String accessToken;
    final String url;
    public GithubVariables(String url, String accessToken) {
        this(new File(downloadFile(url, accessToken)), url, accessToken);
    }
    public GithubVariables(File file, String url, String accessToken) {
        super(file);
        this.url = url;
        this.accessToken = accessToken;
    }

    public static String getUsername(String url) {
        return url.replace("https://github.com/", "").split("/")[0];
    }

    public static String downloadFile(String url, String accessToken) {
        try {
            Path tempDir = Files.createTempDirectory("variables");
            String regex = "https://.*/blob/.*/";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            String repo = url.split("/blob/")[0];
            CloneCommand command = Git.cloneRepository()
                    .setURI(repo)
                    .setDirectory(tempDir.toFile());
            String username = getUsername(url);
            if (accessToken != null)
                command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, accessToken));
            command.call();
            String filePath = tempDir.toFile().getAbsolutePath() + "/" + matcher.replaceAll("");
            tempDir.toFile().deleteOnExit();
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setVariable(String name, Object value) {
        super.setVariable(name, value);
        if (accessToken != null)
            try {
                Git git = Git.open(new File(file.getParent() + "/.git"));
                git.add()
                        .addFilepattern(".")
                        .call();
                git.commit()
                        .setMessage("Variable " + name + " updated to " + value)
                        .call();
                git.push()
                        .setRemote("origin")
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(getUsername(url), accessToken))
                        .add("main")
                        .call();
            } catch (IOException | GitAPIException e) {
                throw new RuntimeException(e);
            }
    }
}
