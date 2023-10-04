package de.toph.tuffelbot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
public class PingListener extends AbstractCommandListener {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot())
			return;
		Message message = event.getMessage();
		String content = message.getContentRaw();

		if (content.equals("!ping")) {
			MessageChannel channel = event.getChannel();
			channel.sendMessage("Pong!").queue();
		}
	}
}
