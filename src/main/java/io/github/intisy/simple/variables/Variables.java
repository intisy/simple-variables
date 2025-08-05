package io.github.intisy.simple.variables;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public class Variables {
    protected final Properties properties;
    protected final File file;

    public Variables(String filePath) {
        this(new File(filePath));
    }

    public Variables(File file) {
        this.file = file;
        this.properties = new Properties();
        load();
    }

    public String getFolder() {
        return file.getParent();
    }

    public File getFile() {
        return file;
    }

    public Properties getVariables() {
        return properties;
    }

    public void load() {
        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.length() > 0) {
                try (InputStream input = Files.newInputStream(file.toPath())) {
                    properties.load(input);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try (OutputStream output = Files.newOutputStream(file.toPath())) {
            properties.store(output, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String name, Object value) {
        properties.setProperty(name, String.valueOf(value));
        save();
    }

    public void set(String name, JsonElement value) {
        properties.setProperty(name, value.toString());
        save();
    }

    public Object get(String name) {
        return properties.getProperty(name);
    }

    public Object getOrDefault(String name, Object defaultValue) {
        Object value = get(name);
        return value != null ? value : defaultValue;
    }

    public <T> T get(String name, Class<T> clazz) {
        return clazz.cast(get(name));
    }

    public JsonElement getJson(String name) {
        String value = properties.getProperty(name);
        return value != null ? JsonParser.parseString(value) : null;
    }

    public JsonElement getJsonOrDefault(String name, JsonElement defaultValue) {
        JsonElement value = getJson(name);
        return value != null ? value : defaultValue;
    }

    public boolean contains(String name) {
        return properties.containsKey(name);
    }

    public void delete(String name) {
        properties.remove(name);
        save();
    }
}
