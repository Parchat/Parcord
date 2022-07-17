package net.parchat.parcord.paper.handlers.modules;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.parchat.parcord.paper.Parcord;
import net.parchat.parcord.paper.api.config.ConfigFile;
import net.parchat.parcord.paper.handlers.ModuleHandler;
import org.bstats.bukkit.Metrics;

@Singleton
public class MetricsModule implements ModuleHandler {

    @Inject private Parcord plugin;

    @Inject private ConfigFile configFile;

    @Override
    public void enable() {
        if (configFile.getFile().getBoolean("settings.metrics-enabled")) new Metrics(plugin, 15776);
    }

    @Override
    public void disable() {}
}