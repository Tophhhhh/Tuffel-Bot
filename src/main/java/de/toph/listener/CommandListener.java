package de.toph.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.command.CoinflipCommand;
import de.toph.command.ICommand;
import de.toph.command.MoveallCommand;
import de.toph.command.Verifycommand;
import de.toph.command.WetterCommand;
import de.toph.constant.CommandConstant;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Tophhhhh
 *
 * Listener for all commands
 */
public class CommandListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListener.class);
    
    private Map<String, ICommand> commandList = new HashMap<>();
    
    public CommandListener() {
	commandList.put(CommandConstant.COINFLIPCOMMAND, CoinflipCommand.getInstance());
	commandList.put(CommandConstant.MOVEALLCOMMAND, MoveallCommand.getInstance());
	commandList.put(CommandConstant.VERIFYCOMMAND, Verifycommand.getInstance());
	commandList.put(CommandConstant.WEATHERCOMMAND, WetterCommand.getInstance());
    }
    
    /**
     * Slash command interaction
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	String command = event.getName();
	ICommand instance = commandList.get(command);
	if(instance != null) {
	    LOGGER.debug("Run slashcommand");
	    instance.runCommand(CommandConstant.SLASH, event);
	}
    }
    
    /**
     * Message recieved interaction
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
	String message = event.getMessage().getContentRaw();
	if(event.getAuthor().isBot() && !message.startsWith("!")) {
	    return;
	}
	message = message.substring(1);
	ICommand instance = commandList.get(message.split(" ")[0]);
	if(instance != null) {
	    LOGGER.debug("Run message command");
	    instance.runCommand(CommandConstant.MESSAGERECIEVED, event);
	}
    }
    
    /**
     * Button interaction
     */
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
	String componentId = event.getComponentId().split("-")[0];
	ICommand instance = commandList.get(componentId);
	if(instance != null) {
	    LOGGER.debug("Run button interaction");
	    instance.runCommand(CommandConstant.BUTTONINTERACTION, event);
	}
    }
    
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
	String modalId = event.getModalId();
	ICommand instance = commandList.get(modalId);
	if(instance != null) {
	    LOGGER.debug("Run modal interaction");
	    instance.runCommand(CommandConstant.MODALINTERACTION, event);
	}
    }
}
