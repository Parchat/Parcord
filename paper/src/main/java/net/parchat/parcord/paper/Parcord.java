package net.parchat.parcord.paper;

import net.parchat.parcord.paper.api.FileManager;
import net.parchat.parcord.paper.api.ParManager;
import net.parchat.parcord.paper.api.config.ConfigFile;
import net.parchat.parcord.paper.handlers.PluginModule;
import net.parchat.parcord.paper.handlers.modules.ConfigModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.parchat.parcord.paper.handlers.modules.JDAModule;
import org.bukkit.plugin.java.JavaPlugin;

public class Parcord extends JavaPlugin {

    private Injector injector;

    private boolean isEnabled = false;

    @Inject private ParManager parManager;

    @Inject private ConfigModule configModule;

    @Inject private JDAModule jdaModule;

    @Inject private FileManager fileManager;

    @Inject private ConfigFile configFile;

    @Override
    public void onEnable() {

        try {
            parManager = new ParManager();
            fileManager = new FileManager();

            configFile = new ConfigFile();

            PluginModule module = new PluginModule(this, parManager, fileManager, configFile);

            injector = module.createInjector();

            injector.injectMembers(this);

            fileManager.registerCustomFolder("/locale")
                    .registerDefaultGenerateFiles("locale-en.yml", "/locale")
                    .registerDefaultGenerateFiles("locale-de.yml", "/locale").setup().isLogging(true);

            configModule.enable();

            parManager.load();

            // Load the bot.
            jdaModule.enable();
        } catch (Exception e) {
            getLogger().severe(e.getMessage());

            for (StackTraceElement stack : e.getStackTrace()) {
                getLogger().severe(String.valueOf(stack));
            }

            return;
        }

        isEnabled = true;
    }

    @Override
    public void onDisable() {
        if (!isEnabled) return;

        jdaModule.disable();

        parManager.stop();

        injector = null;
    }
}