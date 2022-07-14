package net.parchat.parcord.paper.api.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.parchat.parcord.paper.Methods;
import net.parchat.parcord.paper.Parcord;
import net.parchat.parcord.paper.api.FileManager;
import net.parchat.parcord.paper.api.config.interfaces.IConfigFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.InputStream;

public class ConfigFile implements IConfigFile {

    @Inject
    @Named("ConfigFolder")
    private File configFolder;

    @Inject private Methods methods;

    @Inject private Parcord plugin;

    @Inject private FileManager fileManager;

    private File blankFile = null;

    private void create() {
        blankFile = new File(configFolder, "/" + "config.yml");

        if (!blankFile.exists()) {
            InputStream jarFile = plugin.getClass().getResourceAsStream("/" + "config.yml");
            methods.copyFile(jarFile, blankFile);
        }
    }

    @Override
    public void load() {
        // Copy the file from src/main/resources
        create();

        fileManager.addConfiguration(blankFile.getName(), blankFile, YamlConfiguration.loadConfiguration(blankFile));
    }

    @Override
    public void save() {
        // Copy the file from src/main/resources
        create();

        fileManager.saveFile(blankFile.getName());
    }

    @Override
    public void remove() {
        fileManager.removeFile(blankFile.getName());
    }

    @Override
    public void reload() {
        fileManager.reloadFile(blankFile.getName(), YamlConfiguration.loadConfiguration(fileManager.getFile(blankFile)));
    }

    @Override
    public FileConfiguration getFile() {
        return fileManager.getFile("config.yml");
    }
}