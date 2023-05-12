package de.toph.command;

import java.awt.Color;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * 
 * @author Tophhhhh
 *
 * Coinflip command
 */
public class CoinflipCommand extends AbstractCommand {

    private static CoinflipCommand command;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CoinflipCommand.class);
    
    private CoinflipCommand() {
	// empty
    }
    
    /**
     * 
     * get instance of coinflipcommand
     * 
     * @return CoinflipCommand
     */
    public static CoinflipCommand getInstance() {
	if(command == null) {
	    command = new CoinflipCommand();
	}
	return command;
    }
    
    /**
     * run slash command
     * 
     * @param event
     */
    @Override
    protected void runSlashCommand(Object event) {
	SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
	Random r = new Random();
	int value = r.nextInt(2);
	String result = value == 1 ? "Head" : "Tail";
	LOGGER.debug(result);
	
	EmbedBuilder eb = new EmbedBuilder();
	eb.setTitle("Head or Tail");
	eb.setColor(Color.yellow);
	eb.addField(String.format("Das Ergebnis ist: %s", result),"",false);
	eb.setFooter(String.format("%s hat eine Sucht", slashEvent.getUser().getAsTag()));
	
	slashEvent.replyEmbeds(eb.build()).queue();
    }
}