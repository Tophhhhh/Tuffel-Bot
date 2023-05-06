package de.toph.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.constant.CommandConstant;

/**
 * 
 * @author Tophhhhh
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
	    runSlashCommand(event);
	    break;
	case CommandConstant.MESSAGERECIEVED:
	    runMessageCommand(event);
	    break;
	case CommandConstant.BUTTONINTERACTION:
	    runButtonCommand(event);
	    break;
	case CommandConstant.MODALINTERACTION:
	    runModalInteraction(event);
	    break;
	}
    }

    /**
     * run Modal interaction
     * 
     * @param event
     */
    protected void runModalInteraction(Object event) {
	logger.info("Set up modalinteraction");
    }

    /**
     * run Slash command
     * 
     * @param event
     */
    protected void runSlashCommand(Object event) {
	logger.info("Set up slashcommand");
    }

    /**
     * run Button command
     * 
     * @param event
     */
    protected void runButtonCommand(Object event) {
	logger.info("Set up buttoncommand");
    }

    /**
     * run Message command
     * 
     * @param event
     */
    protected void runMessageCommand(Object event) {
	logger.info("Set up messagecommand");
    }
}