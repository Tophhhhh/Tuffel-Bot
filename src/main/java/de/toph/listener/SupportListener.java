package de.toph.listener;

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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if (!event.getName().equals("support")) {
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

	    createSupportTicket(event, subject, body);

	    event.reply("Thanks for your request!").setEphemeral(true).queue();
	}
    }
    
    private void createSupportTicket(ModalInteractionEvent event, String subject, String body) {
	User user = event.getUser();
	
	Category cat = event.getGuild().getCategoryById(1046420663860215839l);
	cat.createTextChannel(subject).queue();
    }
}