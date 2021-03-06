package net.parchat.parcord.paper.api;

import com.google.inject.Inject;
import net.parchat.parcord.paper.Methods;
import net.parchat.parcord.paper.Parcord;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

    private final HashMap<String, FileConfiguration> fileConfigurations = new HashMap<>();
    private final HashMap<String, File> files = new HashMap<>();

    private boolean logChanges = false;

    @Inject private Parcord parcord;

    @Inject private Methods methods;

    private final ArrayList<String> homeFolders = new ArrayList<>();
    private final ArrayList<CustomFile> CustomFiles = new ArrayList<>();
    private final HashMap<String, String> jarHomeFolders = new HashMap<>();
    private final HashMap<String, String> autoGeneratedFiles = new HashMap<>();

    public FileManager setup() {
        if (!parcord.getDataFolder().exists()) parcord.getDataFolder().mkdirs();

        if (homeFolders.size() > 0) {
            if (logChanges) parcord.getLogger().warning("Loading custom files...");

            for (String folder : homeFolders) {
                File homeFile = new File(parcord.getDataFolder(), "/" + folder);

                if (homeFile.exists()) {

                    String[] list = homeFile.list();

                    if (list != null) {
                        for (String name : list) {
                            if (name.endsWith(".yml")) {
                                CustomFile file = new CustomFile(name, folder, logChanges, parcord);

                                if (file.exists()) {
                                    CustomFiles.add(file);

                                    if (logChanges) parcord.getLogger().info("Loaded new custom file: " + homeFile + "/" + name + ".");
                                }
                            }
                        }
                    }

                    return this;
                }

                homeFile.mkdir();

                if (logChanges) parcord.getLogger().info("The folder " + folder + "/ was not found so it was created.");

                for (String fileName : autoGeneratedFiles.keySet()) {
                    if (autoGeneratedFiles.get(fileName).equalsIgnoreCase(folder)) {
                        folder = autoGeneratedFiles.get(fileName);

                        try {
                            File serverFile = new File(parcord.getDataFolder(), folder + "/" + fileName);
                            InputStream jarFile = getClass().getResourceAsStream((jarHomeFolders.getOrDefault(fileName, folder)) + "/" + fileName);

                            methods.copyFile(jarFile, serverFile);

                            if (fileName.toLowerCase().endsWith(".yml")) {
                                CustomFiles.add(new CustomFile(fileName, folder, logChanges, parcord));
                            }

                            if (logChanges) parcord.getLogger().info("Created new default file: " + folder + "/" + fileName + ".");
                        } catch (Exception e) {
                            parcord.getLogger().warning("Could not save " + fileName + "!");

                            for (StackTraceElement stack : e.getStackTrace()) {
                                parcord.getLogger().severe(String.valueOf(stack));
                            }
                        }
                    }
                }
            }
        }

        return this;
    }

    // Sets whether its loggable.
    public void isLogging(boolean toggle) {
        this.logChanges = toggle;
    }

    // Check if its loggable.
    public boolean loggable() {
        return logChanges;
    }

    // Custom Files Methods Start

    /**
     * Register a folder that has custom files in it. Make sure to have a "/" in front of the folder name.
     * @param homeFolder The folder that has custom files in it.
     */
    public FileManager registerCustomFolder(String homeFolder) {
        homeFolders.add(homeFolder);
        return this;
    }

    /**
     * Unregister a folder that has custom files in it. Make sure to have a "/" in front of the folder name.
     * @param homeFolder The folder with custom files in it.
     */
    public FileManager unregisterCustomFilesFolder(String homeFolder) {
        homeFolders.remove(homeFolder);
        return this;
    }

    /**
     * Register a file that needs to be generated when it's home folder doesn't exist. Make sure to have a "/" in front of the home folder's name.
     * @param fileName The name of the file you want to auto-generate when the folder doesn't exist.
     * @param homeFolder The folder that has custom files in it.
     */
    public FileManager registerDefaultGenerateFiles(String fileName, String homeFolder) {
        autoGeneratedFiles.put(fileName, homeFolder);
        return this;
    }

    /**
     * Register a file that needs to be generated when it's home folder doesn't exist. Make sure to have a "/" in front of the home folder's name.
     * @param fileName The name of the file you want to auto-generate when the folder doesn't exist.
     * @param homeFolder The folder that has custom files in it.
     * @param jarHomeFolder The folder that the file is found in the jar.
     */
    public FileManager registerDefaultGenerateFiles(String fileName, String homeFolder, String jarHomeFolder) {
        autoGeneratedFiles.put(fileName, homeFolder);
        jarHomeFolders.put(fileName, jarHomeFolder);
        return this;
    }

    /**
     * Unregister a file that doesn't need to be generated when it's home folder doesn't exist. Make sure to have a "/" in front of the home folder's name.
     * @param fileName The file that you want to remove from auto-generating.
     */
    public FileManager removeDefaultGenerateFiles(String fileName) {
        autoGeneratedFiles.remove(fileName);
        jarHomeFolders.remove(fileName);
        return this;
    }

    /**
     * Get a custom file from the loaded custom files instead of a hardcoded one.
     * This allows you to get custom files like Per player data files.
     * @param name Name of the crate you want. (Without the .yml)
     * @return The custom file you wanted otherwise if not found will return null.
     */
    public CustomFile getCustomFile(String name) {
        for (CustomFile file : CustomFiles) {
            if (file.getName().equalsIgnoreCase(name)) {
                return file;
            }
        }

        return null;
    }

    /**
     * Save a custom file.
     * @param name The name of the custom file.
     */
    public void saveCustomFile(String name) {
        CustomFile file = getCustomFile(name);

        if (file != null) {
            try {
                file.getFile().save(new File(parcord.getDataFolder(), file.getHomeFolder() + "/" + file.getFileName()));

                if (logChanges) parcord.getLogger().info("Successfully saved the " + file.getFileName() + ".");
            } catch (Exception e) {
                parcord.getLogger().warning("Could not save " + file.getFileName() + "!");

                for (StackTraceElement stack : e.getStackTrace()) {
                    parcord.getLogger().severe(String.valueOf(stack));
                }
            }

            return;
        }

        if (logChanges) parcord.getLogger().warning("The file " + name + ".yml could not be found!");
    }

    /**
     * Overrides the loaded state file and loads the file systems file.
     */
    public void reloadCustomFile(String name) {
        CustomFile file = getCustomFile(name);

        if (file != null) {
            try {
                file.file = YamlConfiguration.loadConfiguration(new File(parcord.getDataFolder(), "/" + file.getHomeFolder() + "/" + file.getFileName()));

                if (logChanges) parcord.getLogger().info("Successfully reloaded the " + file.getFileName() + ".");
            } catch (Exception e) {
                parcord.getLogger().warning("Could not save " + file.getFileName() + "!");

                for (StackTraceElement stack : e.getStackTrace()) {
                    parcord.getLogger().severe(String.valueOf(stack));
                }
            }

            return;
        }

        if (logChanges) parcord.getLogger().warning("The file " + name + ".yml could not be found!");
    }

    // Custom Files Methods End

    // Adds a single file to the hash maps.
    public void addConfiguration(String fileName, File file, FileConfiguration fileConfiguration) {
        fileConfigurations.put(fileName, fileConfiguration);
        files.put(fileName, file);

        if (logChanges) parcord.getLogger().warning("Loading " + fileName + "...");
    }

    // Saves the new file contents after doing something like getFile("config.yml").set("").
    public void saveFile(String fileName) {
        try {
            fileConfigurations.get(fileName).save(files.get(fileName));

            if (logChanges) parcord.getLogger().warning("Saving " + fileName + "...");
        } catch (Exception e) {
            parcord.getLogger().warning("Could not save " + fileName + "!");

            for (StackTraceElement stack : e.getStackTrace()) {
                parcord.getLogger().severe(String.valueOf(stack));
            }
        }
    }

    // Reloads the file, obviously.
    public void reloadFile(String fileName, FileConfiguration fileConfiguration) {
        fileConfigurations.put(fileName, fileConfiguration);

        if (logChanges) parcord.getLogger().warning("Reloading " + fileName + "...");
    }

    public void removeFile(String fileName) {
        fileConfigurations.remove(fileName);
        files.remove(fileName);

        if (logChanges) parcord.getLogger().warning(fileName + " has been removed...");
    }

    // Gets the file for things like reloading.
    public File getFile(File file) {
        return files.get(file.getName());
    }

    // Gets the file if you need to get a value.
    public FileConfiguration getFile(String fileName) {
        return fileConfigurations.get(fileName);
    }
}