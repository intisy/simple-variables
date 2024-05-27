package com.github.WildePizza;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;

@SuppressWarnings("unused")
public class GithubVariables extends SimpleVariables {
    final String accessToken;
    final String username;
    public GithubVariables(String filePath, String username, String accessToken) {
        this(new File(filePath), username, accessToken);
    }
    public GithubVariables(File file, String username, String accessToken) {
        super(file);
        this.username = username;
        this.accessToken = accessToken;
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
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, accessToken))
                        .add("main")
                        .call();
            } catch (IOException | GitAPIException e) {
                throw new RuntimeException(e);
            }
    }
}
