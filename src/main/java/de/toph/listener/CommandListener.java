package de.toph.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Tophhhhh
 *
 * Listener for all commands
 */
public class CommandListener extends ListenerAdapter {

    /*
     * Slash command Interactions
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
    }
}
