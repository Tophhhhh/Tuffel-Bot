package de.toph.tuffelbot.configuration;

import de.toph.tuffelbot.listener.AbstractCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Configuration
public class BotConfiguration {

    private final Logger logger = LoggerFactory.getLogger(BotConfiguration.class);

    @Value("${dc.bot.activity}")
    private String botActivity;

    @Value("${dc.bot.token}")
    private String botToken;

    @Bean
    public <T extends AbstractCommandListener> JDA getJda(List<AbstractCommandListener> eventListener) {
        List<CommandData> commandList = new ArrayList<>();

        JDA bot = JDABuilder
                .createDefault(botToken)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .setActivity(Activity.playing(botActivity))
                .setStatus(OnlineStatus.ONLINE)
                .setEnableShutdownHook(true)
                .build();

        for(AbstractCommandListener listener : eventListener) {
            bot.addEventListener(listener);
            if(listener.getCommandData() != null) {
                commandList.add(listener.getCommandData());
            }
            logger.info("Command {} has been registered!", listener);
        }

        bot.updateCommands().addCommands(commandList).queue();
        return bot;
    }
}
