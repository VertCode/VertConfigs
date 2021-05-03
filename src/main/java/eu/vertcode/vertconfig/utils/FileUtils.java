package eu.vertcode.vertconfig.utils;

public class FileUtils {

    private static FileUtils instance;

    public static FileUtils getInstance() {
        if (instance == null) instance = new FileUtils();
        return instance;
    }

    /**
     * Returns the file name without an extension.
     *
     * @param name The name of the file
     * @return A file name without an extension
     */
    public String getName(String name) {
        return name.contains(".") ? name.split("\\.")[0] : name;
    }
}
