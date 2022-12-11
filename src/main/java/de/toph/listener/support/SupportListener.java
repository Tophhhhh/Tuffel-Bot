package de.toph.listener.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if (!"support".equals(event.getName())) {
	    return;
	}
	
	TextInput subject = TextInput.create("subject", "Subject", TextInputStyle.SHORT)
		.setPlaceholder("Subject of this ticket")
		.setMinLength(10)
		.setMaxLength(100)
		.build();
	
	TextInput body = TextInput.create("body", "Body", TextInputStyle.PARAGRAPH)
		.setPlaceholder("Your concerns go here")
		.setMinLength(30)
		.setMaxLength(1000)
		.build();

	Modal modal = Modal.create("modmail", "Modmail")
		.addActionRows(ActionRow.of(subject), ActionRow.of(body))
		.build();

	event.replyModal(modal).queue();
    }
    
    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
	if (event.getModalId().equals("modmail")) {
	    String subject = event.getValue("subject").getAsString();
	    String body = event.getValue("body").getAsString();

	    LOGGER.debug(subject);
	    LOGGER.debug(body);

	    event.reply("Thanks for your request!").setEphemeral(true).queue();
	}
    }
}