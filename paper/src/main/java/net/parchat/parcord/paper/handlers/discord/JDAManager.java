package net.parchat.parcord.paper.handlers.discord;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.parchat.parcord.paper.Parcord;
import net.parchat.parcord.paper.api.config.ConfigFile;
import javax.security.auth.login.LoginException;

@Singleton
@SuppressWarnings("all")
public class JDAManager {

    private JDA jda;

    @Inject private ConfigFile configFile;
    @Inject private Parcord parcord;

    public final void create() throws LoginException, InterruptedException {

        String token = configFile.getFile().getString("settings.token");

        jda = JDABuilder.createDefault(token)
                .enableCache(CacheFlag.EMOJI)
                .disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                .setContextEnabled(false)
                .build().awaitReady();
    }

    public JDA getJDA() {
        return jda;
    }

    public String getGuildID() {
        return configFile.getFile().getString("settings.guild-settings.guild-id");
    }
}