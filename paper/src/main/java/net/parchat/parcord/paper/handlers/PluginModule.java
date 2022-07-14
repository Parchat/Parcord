package net.parchat.parcord.paper.handlers;

import net.parchat.parcord.paper.Parcord;
import net.parchat.parcord.paper.api.FileManager;
import net.parchat.parcord.paper.api.ParManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import net.parchat.parcord.paper.api.config.ConfigFile;
import javax.annotation.Nonnull;
import java.io.File;

public class PluginModule extends AbstractModule {

    private final Parcord plugin;

    private final ParManager parManager;

    private final FileManager fileManager;

    private final ConfigFile configFile;

    public PluginModule(Parcord plugin, ParManager parManager, FileManager fileManager, ConfigFile configFile) {
        this.plugin = plugin;

        this.parManager = parManager;
        this.fileManager = fileManager;

        this.configFile = configFile;
    }

    @Nonnull
    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        bind(Parcord.class).toInstance(plugin);

        bind(ParManager.class).toInstance(parManager);
        bind(FileManager.class).toInstance(fileManager);

        bind(ConfigFile.class).toInstance(configFile);

        bind(File.class).annotatedWith(Names.named("ConfigFolder")).toInstance(plugin.getDataFolder());
    }
}