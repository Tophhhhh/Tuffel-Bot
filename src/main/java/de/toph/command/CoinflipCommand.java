package de.toph.command;

import java.awt.Color;
import java.io.InputStream;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

/**
 * @author Tophhhhh
 * <p>
 * Coinflip command
 */
public class CoinflipCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinflipCommand.class);

    /**
     * run slash command
     *
     * @param event
     */
    @Override
    protected void runSlashCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();

        Random r = new Random();
        int value = r.nextInt(2);
        String result = (value == 1 ? "Head" : "Tail");
        LOGGER.debug(result);

        String uri = String.format("pictures/%s.png", result);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(uri);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Head or Tail");
        eb.setColor(Color.yellow);
        eb.addField(String.format("Das Ergebnis ist: %s", result), "", false);
        eb.setFooter(String.format("%s | %s hat eine Sucht", guild.getName(), user.getName()), guild.getIconUrl());
        eb.setImage("attachment://placeholder.png");

        event.replyFiles(FileUpload.fromData(is, "placeholder.png")).setEmbeds(eb.build()).queue();
    }
}
