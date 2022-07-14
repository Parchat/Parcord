package net.parchat.parcord.paper.handlers.modules;

import com.google.inject.Inject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.parchat.parcord.paper.api.config.ConfigFile;
import net.parchat.parcord.paper.handlers.ModuleHandler;
import javax.security.auth.login.LoginException;
import java.util.Objects;

public class JDAModule implements ModuleHandler {

    @Inject private ConfigFile configFile;

    private JDA jda;

    @Override
    public void enable() throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault(configFile.getFile().getString("settings.token"))
                .enableCache(CacheFlag.EMOJI)
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                .setContextEnabled(false)
                .build().awaitReady();

        boolean toggle = configFile.getFile().getBoolean("settings.guild-settings.start-message");

        String localeStartUp = configFile.getFile().getString("settings.guild-settings.start-up-message");

        String guildChannel = configFile.getFile().getString("settings.guild-settings.chat-channel");

        String guildID = configFile.getFile().getString("settings.guild-settings.guild-id");

        if (toggle) {
            if (guildID != null & guildChannel != null && localeStartUp != null) {
                Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guildID)).getTextChannelById(guildChannel)).sendMessage(localeStartUp).queue();
            }
        }
    }

    @Override
    public void disable() {
        boolean toggle = configFile.getFile().getBoolean("settings.guild-settings.stop-message");

        String localeShutDown = configFile.getFile().getString("settings.guild-settings.shut-down-message");

        String guildChannel = configFile.getFile().getString("settings.guild-settings.chat-channel");

        String guildID = configFile.getFile().getString("settings.guild-settings.guild-id");

        if (toggle) {
            if (guildID != null & guildChannel != null && localeShutDown != null) {
                Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guildID)).getTextChannelById(guildChannel)).sendMessage(localeShutDown).queue();
            }
        }

        jda.shutdown();
    }

    public JDA getJda() {
        return jda;
    }
}