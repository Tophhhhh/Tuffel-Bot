package de.toph.listener;

import java.util.HashMap;
import java.util.Map;

import de.toph.command.CoinflipCommand;
import de.toph.command.ICommand;
import de.toph.command.MoveallCommand;
import de.toph.constant.CommandConstant;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Tophhhhh
 *
 * Listener for all commands
 */
public class CommandListener extends ListenerAdapter {

    private Map<String, ICommand> commandList = new HashMap<>();
    
    public CommandListener() {
	commandList.put(CommandConstant.COINFLIPCOMMAND, CoinflipCommand.getInstance());
	commandList.put(CommandConstant.MOVEALLCOMMAND, MoveallCommand.getInstance());
    }
    
    /*
     * Slash command Interactions
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	String command = event.getName();
	ICommand instance = commandList.get(command);
	if(instance != null) {
	    instance.runCommand("slash", event);
	}
    }
}
