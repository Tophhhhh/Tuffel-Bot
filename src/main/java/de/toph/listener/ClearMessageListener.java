package de.toph.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Toph
 *
 *         Clear Listener to delete a large amount of messages
 * 
 *         Permission.MESSAGE_MANAGE
 */
public class ClearMessageListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClearMessageListener.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if (!"clear".equals(event.getName())) {
	    return;
	}
	    int amount = event.getOption("amount").getAsInt();
	    if (amount > 200) {
		event.reply("Der eingegebene Wert ist zu Groß! Wert wurde auf 200 gesetzt").queue();
		amount = 200;
	    }

	    event.getChannel().purgeMessages(get(event.getChannel(), amount));
	    event.reply(amount + " Message(s) deleted!").complete().deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
    }

    public List<Message> get(MessageChannel channel, int amount) {
	List<Message> messages = new ArrayList<>();
	int i = amount + 1;

	for (Message message : channel.getIterableHistory().cache(false)) {
	    if (!message.isPinned()) {
		messages.add(message);
	    }
	    if (--i <= 0) {
		break;
	    }
	}
	return messages;
    }

}
