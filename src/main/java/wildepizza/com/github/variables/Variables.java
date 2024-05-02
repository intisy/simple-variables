package wildepizza.com.github.variables;

import java.io.*;
import java.util.HashMap;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked", "unused"})
public class Variables {
    private final HashMap<String, Object> variables;
    private final File file;

    public Variables(String filePath) {
        this.file = new File(filePath);
        variables = loadVariablesFromFile();
    }
    public Variables(File file) {
        this.file = file;
        variables = loadVariablesFromFile();
    }

    private HashMap<String, Object> loadVariablesFromFile() {
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

    private void saveVariablesToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(variables);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
        saveVariablesToFile();
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    public boolean variableExists(String name) {
        return variables.containsKey(name);
    }

    public void deleteVariable(String name) {
        variables.remove(name);
        saveVariablesToFile();
    }
}
