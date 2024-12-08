package io.github.intisy.simple.variables;

import java.io.File;

@SuppressWarnings("unused")
public class VariablesBuilder {
    private String projectName;
    private String filePath;
    private String username;
    private String accessToken;
    private String url;
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
        isGithub = true;
        this.url = url;
        this.accessToken = accessToken;
        return this;
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
                //noinspection ResultOfMethodCallIgnored
                appDataDir.mkdirs();
            filePath = appDataDir + File.separator + "variables.dat";
        } else if (this.filePath != null) {
            filePath = this.filePath;
        } else {
            throw new IllegalArgumentException("No project name or file path specified.");
        }
        if (isGithub)
            return new GithubVariables(url, accessToken);
        else
            return new SimpleVariables(filePath);
    }
}
