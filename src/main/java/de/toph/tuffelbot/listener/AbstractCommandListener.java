package de.toph.tuffelbot.listener;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class AbstractCommandListener extends ListenerAdapter {

	public CommandData getCommandData() {
		return null;
	}
}
