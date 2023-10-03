package de.toph.tuffelbot.bean;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotBean {

    @Value("${dc.bot.token}")
    private String botToken;

    @Bean
    public JDA getJda() {
        return JDABuilder
                .createDefault(botToken)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("Dies Das Ananas"))
                .setEnableShutdownHook(true)
                .build();
    }
}
