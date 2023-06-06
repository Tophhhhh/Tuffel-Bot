package de.toph.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.constant.CommandConstant;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * 
 * @author Tophhhhh
 *
 *	test
 *
 * Abstact command
 */
public class AbstractCommand implements ICommand {
    
    private final Logger logger = LoggerFactory.getLogger(AbstractCommand.class);

    /**
     * choose command / interaction
     */
    @Override
    public void runCommand(String type, Object event) {
	switch (type) {
	case CommandConstant.SLASH:
	    runSlashCommand((SlashCommandInteractionEvent) event);
	    break;
	case CommandConstant.MESSAGERECIEVED:
	    runMessageCommand((MessageReceivedEvent) event);
	    break;
	case CommandConstant.BUTTONINTERACTION:
	    runButtonCommand((ButtonInteractionEvent) event);
	    break;
	case CommandConstant.MODALINTERACTION:
	    runModalInteraction((ModalInteractionEvent) event);
	    break;
	}
    }

    /**
     * run Modal interaction
     * 
     * @param event
     */
    protected void runModalInteraction(ModalInteractionEvent event) {
	logger.info("Set up modalinteraction");
    }

    /**
     * run Slash command
     * 
     * @param event
     */
    protected void runSlashCommand(SlashCommandInteractionEvent event) {
	logger.info("Set up slashcommand");
    }

    /**
     * run Button command
     * 
     * @param event
     */
    protected void runButtonCommand(ButtonInteractionEvent event) {
	logger.info("Set up buttoncommand");
    }

    /**
     * run Message command
     * 
     * @param event
     */
    protected void runMessageCommand(MessageReceivedEvent event) {
	logger.info("Set up messagecommand");
    }
}