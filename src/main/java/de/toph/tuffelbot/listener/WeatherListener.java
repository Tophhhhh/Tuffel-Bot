package de.toph.tuffelbot.listener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Component
public class WeatherListener extends AbstractCommandListener {

	private static final String WEATHER = "weather";

	@Override
	public CommandData getCommandData() {
		return Commands.slash(WEATHER, "dummy");
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(!event.getName().equals(WEATHER)) {
			return;
		}
		event.reply("Dummy hat funktioniert!").queue();

	}
}
