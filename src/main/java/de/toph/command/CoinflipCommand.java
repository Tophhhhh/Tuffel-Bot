package de.toph.command;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.constant.CommandConstant;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * 
 * @author Tophhhhh
 *
 * Coinflip command
 */
public class CoinflipCommand implements ICommand{

    private static CoinflipCommand command;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CoinflipCommand.class);
    
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
    private void runSlashCommand(Object event) {
	SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
	Random r = new Random();
	int value = r.nextInt(2);
	String result = value == 1 ? "Head" : "Tail";
	LOGGER.debug(result);
	slashEvent.reply(String.format("Your result is `%s`", result)).queue();
    }

    /**
     * run command 
     */
    @Override
    public void runCommand(String type, Object event) {
	switch(type) {
	case CommandConstant.SLASH:
	    runSlashCommand(event);
	    break;
	}
    }


    
    
}
