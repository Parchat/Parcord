package net.parchat.parcord.paper.handlers.modules;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.parchat.parcord.paper.Parcord;
import net.parchat.parcord.paper.api.config.ConfigFile;
import net.parchat.parcord.paper.handlers.ModuleHandler;
import net.parchat.parcord.paper.handlers.discord.JDAManager;

import javax.security.auth.login.LoginException;

@Singleton
@SuppressWarnings("all")
public class JDAModule implements ModuleHandler {

    @Inject private ConfigFile configFile;

    @Inject private JDAManager jdaManager;
    @Inject private Parcord parcord;

    @Override
    public void enable() {

        try {
            jdaManager.create();
        } catch (Exception e) {
            parcord.getLogger().severe("I don't have a valid token or no token at all.");
            parcord.getLogger().severe("Please go to your config.yml & add a valid token!");
            return;
        }

        boolean toggle = configFile.getFile().getBoolean("settings.guild-settings.start-message");

        String localeStartUp = configFile.getFile().getString("settings.guild-settings.start-up-message");

        String guildChannel = configFile.getFile().getString("settings.guild-settings.chat-channel");

        String guildID = configFile.getFile().getString("settings.guild-settings.guild-id");

        if (toggle) sendMsg(guildID, guildChannel, localeStartUp);
    }

    @Override
    public void disable() {
        boolean toggle = configFile.getFile().getBoolean("settings.guild-settings.stop-message");

        String localeShutDown = configFile.getFile().getString("settings.guild-settings.shut-down-message");

        String guildChannel = configFile.getFile().getString("settings.guild-settings.chat-channel");

        String guildID = configFile.getFile().getString("settings.guild-settings.guild-id");

        if (toggle) sendMsg(guildID, guildChannel, localeShutDown);

        jdaManager.getJDA().shutdown();
    }

    private void sendMsg(String guildID, String guildChannel, String localeMessage) {
        if (guildID == null || guildChannel == null || localeMessage == null || guildID.isBlank() || guildChannel.isBlank() || localeMessage.isBlank()) {
            parcord.getLogger().warning("One of your config values is null or has nothing provided so it will not send.");
            parcord.getLogger().warning("Please check your config.yml!");
            return;
        }

        jdaManager.getJDA().getGuildById(guildID).getTextChannelById(guildChannel).sendMessage(localeMessage).queue();
    }
}