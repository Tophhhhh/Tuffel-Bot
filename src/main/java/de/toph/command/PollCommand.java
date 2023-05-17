package de.toph.command;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * 
 * @author Tophhhhh
 *
 */
public class PollCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollCommand.class);

    @Override
    protected void runButtonCommand(Object event) {
	// TODO Auto-generated method stub
	
    }

    @Override
    protected void runSlashCommand(Object event) {
	SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
	
	OptionMapping omQuestion = slashEvent.getOption("question");
	if(omQuestion == null) {
	    slashEvent.reply("Bitte eine Frage eingeben!").setEphemeral(false).queue();
	    return;
	}
	
	String question = omQuestion.getAsString();
	
	EmbedBuilder eb = new EmbedBuilder();
	eb.setAuthor(slashEvent.getUser().getName());
	eb.setTitle("Umfrage");
	eb.setColor(Color.CYAN);
	eb.setDescription(question);
	
	eb.addField("Aktzeptiert", "-", true);
	eb.addField("Abgelehnt", "-", true);
	eb.addField("Fragezeichen", "-", true);
	
	eb.setFooter(slashEvent.getUser().getAsTag());
	
	slashEvent.replyEmbeds(eb.build()).queue();
    }
}