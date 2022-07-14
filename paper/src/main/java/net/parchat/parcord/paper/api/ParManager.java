package net.parchat.parcord.paper.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.parchat.parcord.paper.Parcord;

@Singleton
public class ParManager {

    @Inject private Parcord plugin;

    public void load() {
        plugin.getLogger().info("Guten Tag!");
    }

    public void stop() {
        plugin.getLogger().info("Good Bye!");
    }
}