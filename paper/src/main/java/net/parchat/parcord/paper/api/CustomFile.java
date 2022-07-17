package net.parchat.parcord.paper.api;

import net.parchat.parcord.paper.Parcord;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

public class CustomFile {

    private final String name;
    private final String fileName;
    private final String homeFolder;

    FileConfiguration file;

    private final File blankFile;

    private final boolean loggable;

    private final Parcord parcord;

    /**
     * A custom file that is being made.
     * @param name Name of the file.
     * @param homeFolder The home folder of the file.
     */
    public CustomFile(String name, String homeFolder, boolean loggable, Parcord parcord) {
        this.name = name.replace(".yml", "");
        this.fileName = name;
        this.homeFolder = homeFolder;

        this.parcord = parcord;

        this.loggable = loggable;

        File newFile = new File(parcord.getDataFolder(), "/" + homeFolder);
        File namedFile = new File(newFile, "/" + name);

        blankFile = new File(parcord.getDataFolder(), "/" + homeFolder + "/" + fileName);

        try {
            if (newFile.exists()) {
                if (namedFile.exists()) {
                    file = YamlConfiguration.loadConfiguration(namedFile);
                } else {
                    file = null;
                }

                return;
            }
        } catch (Exception e) {
            parcord.getLogger().warning("Could not save " + fileName + "!");

            for (StackTraceElement stack : e.getStackTrace()) {
                parcord.getLogger().severe(String.valueOf(stack));
            }
        }

        newFile.mkdirs();

        if (loggable) parcord.getLogger().info("The folder " + homeFolder + "/ was not found so it was created.");

        file = null;
    }

    /**
     * Get the name of the file without the .yml part.
     * @return The name of the file without the .yml.
     */
    public String getName() {
        return name;
    }

    // Get the full name of the file with .yml included.
    public String getFileName() {
        return fileName;
    }

    // Get the name of the home folder where the file is.
    public String getHomeFolder() {
        return homeFolder;
    }

    // Get the custom file.
    public FileConfiguration getFile() {
        return file;
    }

    // Check if file exists, True if yes, False if no.
    public boolean exists() {
        return file != null;
    }

    // Save the file.
    public boolean saveFile() {
        if (file == null) return true;

        try {
            file.save(blankFile);

            if (loggable) parcord.getLogger().info("Saved " + fileName + ".");

            return true;
        } catch (Exception e) {
            parcord.getLogger().warning("Could not save " + fileName + "!");

            for (StackTraceElement stack : e.getStackTrace()) {
               parcord.getLogger().severe(String.valueOf(stack));
            }
        }

        if (loggable) parcord.getLogger().warning("There was a null custom file that could not be found!");

        return false;
    }

    // Reload the file.
    public boolean reloadFile() {

        if (file == null) return true;

        try {
            file = YamlConfiguration.loadConfiguration(blankFile);

            if (loggable) parcord.getLogger().info("Reloaded " + fileName + ".");
        } catch (Exception e) {
            parcord.getLogger().warning("Could not save " + fileName + "!");

            for (StackTraceElement stack : e.getStackTrace()) {
                parcord.getLogger().severe(String.valueOf(stack));
            }
        }

        if (loggable) parcord.getLogger().warning("There was a null custom file that could not be found!");

        return false;
    }
}