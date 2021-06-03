package eu.vertcode.vertconfig.object;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class VertConfig {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final String path;
    private final File file;
    private JsonObject jsonObject;

    public VertConfig(File file) {
        this.path = file.getAbsolutePath();
        this.file = file;

        try {
            this.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a Object from the config specified by the key
     *
     * @param key   The "path" of the object
     * @param clazz The clazz you want to cast it to
     * @return A Long
     */
    public <T> T get(String key, Class<T> clazz) {
        List<String> path = Arrays.asList(key.split("\\."));
        String where = path.get(path.size() - 1);

        if (path.size() == 1) return gson.fromJson(this.jsonObject.get(where), clazz);

        List<String> floorPath = path.subList(0, path.size() - 1);
        Object embeddedValue = this.getEmbeddedValue(floorPath);

        if (!(embeddedValue instanceof JsonObject))
            return get(String.join(".", path.subList(1, path.size())), clazz);
        else
            return gson.fromJson(((JsonObject) embeddedValue).get(where), clazz);
    }

    /**
     * Get a String from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A String
     */
    public String getString(String key) {
        return this.get(key, String.class);
    }

    /**
     * Get a Integer from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A Integer
     */
    public Boolean getBoolean(String key) {
        return this.get(key, boolean.class);
    }

    /**
     * Get a Integer from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A Integer
     */
    public Integer getInteger(String key) {
        return this.get(key, int.class);
    }

    /**
     * Get a Double from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A Double
     */
    public Double getDouble(String key) {
        return this.get(key, double.class);
    }

    /**
     * Get a Long from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A Long
     */
    public Long getLong(String key) {
        return this.get(key, long.class);
    }

    /**
     * Get a List containing Objects from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A List with objects in it
     */
    public List<Object> getList(String key) {
        return (List<Object>) this.get(key, Object.class);
    }

    /**
     * Get a List containing Strings from the config specified by the key
     *
     * @param key The "path" of the object
     * @return A List with strings in it
     */
    public List<String> getStringList(String key) {
        return (List<String>) this.get(key, Object.class);
    }

    /**
     * Get a String from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A String
     */
    public String getString(String key, String defaultValue) {
        String str = this.getString(key);
        return str == null ? defaultValue : str;
    }

    /**
     * Get a Boolean from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A Boolean
     */
    public Boolean getBoolean(String key, boolean defaultValue) {
        Boolean bln = this.getBoolean(key);
        return bln == null ? defaultValue : bln;
    }

    /**
     * Get a Integer from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A Integer
     */
    public Integer getInteger(String key, int defaultValue) {
        Integer integer = this.getInteger(key);
        return integer == null ? defaultValue : integer;
    }

    /**
     * Get a Double from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A Double
     */
    public Double getDouble(String key, double defaultValue) {
        Double dbl = this.getDouble(key);
        return dbl == null ? defaultValue : dbl;
    }

    /**
     * Get a Long from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A Long
     */
    public Long getLong(String key, long defaultValue) {
        Long lng = this.getLong(key);
        return lng == null ? defaultValue : lng;
    }

    /**
     * Get a List containing Objects from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A List with objects in it
     */
    public List<Object> getList(String key, List<Object> defaultValue) {
        List<Object> list = this.getList(key);
        return list == null ? defaultValue : list;
    }

    /**
     * Get a List containing Strings from the config specified by the key
     *
     * @param key          The "path" of the object
     * @param defaultValue The value it will be if the object cannot be found in the config
     * @return A List with strings in it
     */
    public List<String> getStringList(String key, List<String> defaultValue) {
        List<String> list = this.getStringList(key);
        return list == null ? defaultValue : list;
    }

    /**
     * Set an object in the file, you can create a new "Child" with the . symbol in the key. (Like in Yaml)
     *
     * @param key   The key
     * @param value The value
     */
    public void set(String key, Object value) {
        List<String> path = Arrays.asList(key.split("\\."));
        String where = path.get(path.size() - 1);

        if (path.size() == 1) {
            this.jsonObject.add(where, gson.toJsonTree(value));
            return;
        }

        List<String> floorPath = path.subList(0, path.size() - 1);
        Object embeddedValue = this.getEmbeddedValueSet(floorPath);

        if (!(embeddedValue instanceof JsonObject)) set(String.join(".", path.subList(1, path.size())), value);
        else ((JsonObject) embeddedValue).add(where, gson.toJsonTree(value));
    }

    /**
     * Reloads the config from the disk.
     *
     * @throws IOException
     */
    public void reload() throws IOException {
        this.load();
    }

    /**
     * Saves the file to the disk.
     *
     * @throws IOException
     */
    public void save() throws IOException {
        Writer writer = new FileWriter(this.file);
        gson.toJson(this.jsonObject, writer);
        writer.flush();
        writer.close();
    }

    /**
     * Loads the file from the disk.
     *
     * @throws IOException
     */
    private void load() throws IOException {
        String jsonData = new String(Files.readAllBytes(this.file.toPath()));
        if (jsonData.isEmpty()) jsonData = "{}";
        this.jsonObject = gson.fromJson(jsonData, JsonObject.class);
    }

    /**
     * Finds the deepest JsonObject in the key list
     *
     * @param keys The keys you wanna loop through
     * @param <T>  The Object it returns
     * @return
     */
    private <T> T getEmbeddedValue(final List<?> keys) {
        JsonObject value = this.jsonObject;
        for (Object key : keys) {
            value = (JsonObject) value.get(String.valueOf(key));
            if (value == null) return null;
        }
        return ((Class<T>) Object.class).cast(value);
    }

    /**
     * Finds the deepest JsonObject in the key list
     *
     * @param keys The keys you wanna loop through
     * @param <T>  The Object it returns
     * @return
     */
    private <T> T getEmbeddedValueSet(final List<?> keys) {
        JsonObject value = this.jsonObject;
        for (String key : keys.stream().map(String::valueOf).collect(Collectors.toList())) {
            if (!value.has(key))
                value.add(key, new JsonObject());
            value = (JsonObject) value.get(key);
        }
        return ((Class<T>) Object.class).cast(value);
    }

}
