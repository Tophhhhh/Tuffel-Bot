package de.toph.listener;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class RandomMessages extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if (!"love".equals(event.getName())) {
	    return;
	}

	OptionMapping om = event.getOption("user");
	User user = om.getAsUser();

	User author = event.getUser();
	
	event.reply(String.format("%s spread some love to %s", author.getAsMention(), user.getAsMention())).queue();
    }
}
