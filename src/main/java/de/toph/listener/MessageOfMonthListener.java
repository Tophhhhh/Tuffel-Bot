package de.toph.listener;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.util.DatabaseUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * @author Toph
 * 
 *         shows the message of the month by the most reactions
 * 
 *         needed intent MESSAGE_CONTENT
 */
public class MessageOfMonthListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageOfMonthListener.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if (!event.getName().equals("messageofmonth")) {
	    return;
	}
	Guild guild = event.getGuild();
	MessageChannelUnion channel = event.getChannel();

	LocalDateTime ldt = LocalDateTime.now();
	
	List<Message> messages = channel.getIterableHistory().cache(false).stream().filter(
		e -> (e.getTimeCreated().getMonth() == ldt.getMonth() && e.getTimeCreated().getYear() == ldt.getYear()))
		.collect(Collectors.toList());

	Message mostReaction = null;
	int max = 0;
	for (Message message : messages) {
	    int count = 0;
	    for (MessageReaction mr : message.getReactions()) {
		count += mr.getCount();
	    }
	    if (max < count) {
		max = count;
		mostReaction = message;
	    }
	}

	// Get a special role
	List<IMentionable> mentions = mostReaction.getMentions().getMentions(MentionType.USER);
	if (!mentions.isEmpty()) {
	    IMentionable user = mentions.get(0);
	    Member mem = guild.getMemberById(user.getIdLong());
	    Role role = guild.getRoleById(DatabaseUtils.getRole(guild.getIdLong(), "Special"));
	    if (role != null) {
		guild.addRoleToMember(mem, role).queue();
	    }
	}

	List<User> users = mostReaction.getMentions().getUsers();
	User mention = users.isEmpty() ? null : users.get(0);
	
	EmbedBuilder eb = new EmbedBuilder();
	eb.setColor(Color.CYAN);
	eb.setTitle("Nachricht des Monats!");
	eb.setDescription("Jeden Monat wird die Nachricht mit den Meisten Reaktion angezeigt!");
	eb.addField("Anzahl Nachrichten", String.format("Es waren %s Nachrichten letzten Monat", messages.size()), false);
	if (mostReaction != null) {
	    eb.addField("Anzahl Reactions", String.format("Auf der Nachricht waren %s Reaktionen letzten Monat", max), false);
	    eb.addField("Nachricht des Monats", mostReaction.getContentRaw(), false);
	    if(mention != null) {
		eb.setImage(mention.getAvatarUrl());
	    }
	}
	eb.setFooter(String.format("Created by %s", guild.getName()), guild.getIconUrl());
	event.replyEmbeds(eb.build()).queue();
    }
}