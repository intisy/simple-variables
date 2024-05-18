package com.github.WildePizza;

import java.io.File;

@SuppressWarnings("unused")
public class VariablesBuilder {
    private String projectName;
    private String filePath;
    private VariablesBuilder withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
    private VariablesBuilder withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    private VariablesBuilder withFilePath(File file) {
        this.filePath = file.getAbsolutePath();
        return this;
    }
    private SimpleVariables build() {
        String filePath;
        if (projectName != null) {
            File appDataDir;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                appDataDir = new File(System.getenv("APPDATA"));
            } else {
                appDataDir = new File(System.getProperty("user.home") + File.separator + ".config");
            }
            if (!appDataDir.exists())
                appDataDir.mkdirs();
            filePath = appDataDir + File.separator + "variables.dat";
        } else if (this.filePath != null) {
            filePath = this.filePath;
        } else {
            throw new IllegalArgumentException("No project name or file path specified.");
        }
        return new SimpleVariables(filePath);
    }
}
