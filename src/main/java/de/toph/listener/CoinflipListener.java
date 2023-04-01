package de.toph.listener;

import java.util.Random;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CoinflipListener extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if(!event.getName().equals("coinflip")) {
	    return;
	}
	
	Random r = new Random();
	int value = r.nextInt(2);
	String result = value == 1 ? "Head" : "tail";
	
	event.reply(String.format("Your result is `%s`", result)).queue();
    }

}
