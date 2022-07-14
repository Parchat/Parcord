package net.parchat.parcord.paper.handlers.modules;

import net.parchat.parcord.paper.Parcord;
import net.parchat.parcord.paper.api.config.ConfigFile;
import net.parchat.parcord.paper.handlers.ModuleHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConfigModule implements ModuleHandler {

    @Inject private Parcord plugin;

    @Inject private ConfigFile configFile;

    @Override
    public void enable() {
        configFile.load();
    }

    @Override
    public void disable() {
        configFile.load();
    }
}