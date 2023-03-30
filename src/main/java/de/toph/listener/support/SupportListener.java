package de.toph.listener.support;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

/**
 * Support Listener
 * 
 * noch in development
 * 
 * @author Toph
 *
 */
public class SupportListener extends ListenerAdapter{

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportListener.class);
    
    private Long catergoryId = 1046420663860215839l;
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if ("support".equals(event.getName())) {
	    createModal(event);
	} else if ("closeticket".equals(event.getName())) {
	    closeTicket(event);
	}
    }
    
    /**
     * nur berechtigte personen sollten dies duerfen!
     * 
     * @param event
     */
    private void closeTicket(SlashCommandInteractionEvent event) {
	Category cat = event.getGuildChannel().asTextChannel().getParentCategory();
	if(cat != null && cat.getIdLong() == catergoryId) {
	    event.getChannel().delete().queue();
	}
    }
    
    private void createModal(SlashCommandInteractionEvent event) {
	TextInput subject = TextInput.create("subject", "Thema", TextInputStyle.SHORT)
		.setPlaceholder("Thema")
		.setMinLength(10)
		.setMaxLength(100)
		.build();
	
	TextInput body = TextInput.create("body", "Beschreibung", TextInputStyle.PARAGRAPH)
		.setPlaceholder("Beschreibe dein Problem")
		.setMinLength(30)
		.setMaxLength(1000)
		.build();

	Modal modal = Modal.create("modmail", "Modmail")
		.addComponents(ActionRow.of(subject), ActionRow.of(body))
		.build();

	event.replyModal(modal).queue();
    }
    
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
	if (event.getModalId().equals("modmail")) {
	    String subject = event.getValue("subject").getAsString();
	    String body = event.getValue("body").getAsString();

	    Guild guild = event.getGuild();
	    User user = event.getUser();
	    String roomName = subject.length() > 14 ? subject.substring(0,14) : subject;
	    MessageEmbed me = createEmbeded(subject, body, user);
	    Category cat = guild.getCategoryById(catergoryId);
	    
	    guild.createTextChannel(roomName, cat)
	    .queue(e -> {
		e.sendMessageEmbeds(me).queue();
	    });
	    
	    event.reply("Danke für dein Ticket!").setEphemeral(true).queue();
	}
    }
    
    private MessageEmbed createEmbeded(String topic, String description, User user)  {
	EmbedBuilder eb = new EmbedBuilder();
	eb.setTitle(topic);
	eb.setAuthor(String.format("Author: %s", user.getName()));
	eb.setColor(Color.RED);
	eb.setFooter("Das Ausnutzen des Ticketsystems kann zu einem Bann führen!");
	eb.addField("Beschreibung", description, false);
	return eb.build();
    }
}