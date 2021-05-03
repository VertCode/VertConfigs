package eu.vertcode.vertconfig;

import eu.vertcode.vertconfig.object.VertConfig;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class VertConfigs {

    private static VertConfigs instance;
    private final List<VertConfig> loadedConfigs;

    public VertConfigs() {
        this.loadedConfigs = new ArrayList<>();
    }

    public static VertConfigs getInstance() {
        if (instance == null) instance = new VertConfigs();
        return instance;
    }

    /**
     * Loads the config from the disk, if it doesn't exists it will throw a {@link NullPointerException}
     *
     * @param file The file you want to get the config from
     * @return The Config
     */
    @Nullable
    public VertConfig getConfig(File file) {
        return this.getConfig(file, null);
    }

    /**
     * Loads the config from the disk, if it doesn't exist it will create the default config from the {@link InputStream}.
     *
     * @param file          The file you want to get the config from
     * @param defaultStream The InputStream of the default file
     * @return The Config
     */
    public VertConfig getConfig(File file, InputStream defaultStream) {
        Optional<VertConfig> optional = this.loadedConfigs.stream().filter(vertConfig ->
                vertConfig.getPath().equalsIgnoreCase(file.getAbsolutePath())).findFirst();
        if (optional.isPresent()) return optional.get();

        if (file.exists()) {
            VertConfig vertConfig = new VertConfig(file);
            this.loadedConfigs.add(vertConfig);
            return vertConfig;
        }

        if (defaultStream == null) return null;

        try {
            Files.copy(defaultStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            VertConfig vertConfig = new VertConfig(file);
            this.loadedConfigs.add(vertConfig);
            return vertConfig;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
