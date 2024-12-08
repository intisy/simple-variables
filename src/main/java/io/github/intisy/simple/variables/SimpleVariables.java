package io.github.intisy.simple.variables;

import io.github.intisy.utils.utils.PasswordEncryptionUtil;

import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked", "unused"})
public class SimpleVariables {
    final HashMap<String, Object> variables;
    final File file;

    public SimpleVariables(String filePath) {
        this(new File(filePath));
    }
    public SimpleVariables(File file) {
        this.file = file;
        variables = loadVariablesFromFile();
    }

    public HashMap<String, Object> loadVariablesFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                return (HashMap<String, Object>) inputStream.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("Variable file not found. Creating new variable file.");
                return new HashMap<>();
            } catch (IOException | ClassNotFoundException e) {
                return new HashMap<>();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);}
    }

    public void saveVariablesToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            outputStream.writeObject(variables);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSecretVariable(String name, String value, String password) throws Exception {
        byte[] salt = PasswordEncryptionUtil.generateSalt();
        IvParameterSpec iv = PasswordEncryptionUtil.generateIv();
        String encryptedString = PasswordEncryptionUtil.encrypt(value, password, salt, iv);
        setVariable(name, value);
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
        saveVariablesToFile();
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }
    public String getFolder() {
        return file.getParent();
    }

    public boolean variableExists(String name) {
        return variables.containsKey(name);
    }

    public void deleteVariable(String name) {
        variables.remove(name);
        saveVariablesToFile();
    }
}
