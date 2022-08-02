package me.imjaxs.elyscube.uniquegenerator.tools.file;

import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class FileResource {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();

    private final String path;
    private final File folder, file;
    private final YamlConfiguration configuration;

    public FileResource(File folder, String file) {
        this.path = file;

        this.folder = folder;
        this.file = new File(folder, file);

        this.configuration = new YamlConfiguration();
    }

    public boolean exists() {
        return this.file.exists();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void create() {
        if (!this.folder.exists())
            this.folder.mkdir();

        if (!this.file.exists())
            plugin.saveResource(this.path, false);
    }

    public void load() {
        try {
            this.configuration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public @NotNull FileConfiguration getFileConfiguration() {
        return this.configuration;
    }
}
