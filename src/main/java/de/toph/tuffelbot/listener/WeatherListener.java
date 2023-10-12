package de.toph.tuffelbot.listener;

import de.toph.tuffelbot.service.WeatherService;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WeatherListener extends AbstractCommandListener {

	private final String WEATHER = "weather";

	private WeatherService weatherService;

	@Autowired
	public WeatherListener(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@Override
	public CommandData getCommandData() {
		return Commands.slash(WEATHER, "Zeigt das Wetter in der angegebenen Stadt an.");
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(!event.getName().equals(WEATHER)) {
			return;
		}
		TextInput city = TextInput
				.create("weathercity", "Stadt", TextInputStyle.SHORT)
				.setPlaceholder("Stadt")
				.setRequired(true)
				.build();

		Modal modal = Modal
				.create("weather", "Weather")
				.addComponents(ActionRow.of(city))
				.build();

		event.replyModal(modal).queue();
	}

	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		if(!event.getModalId().equals("weather")) {
			return;
		}
		String city = Objects.requireNonNull(event.getValue("weathercity")).getAsString();
		weatherService.doRequest(city);
	}
}
