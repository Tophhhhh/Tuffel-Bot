package de.toph.command;

import java.awt.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.constant.CommandConstant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * 
 * @author Tophhhhh
 *
 */
public class PollCommand implements ICommand {

    private static PollCommand command;

    private static final Logger LOGGER = LoggerFactory.getLogger(PollCommand.class);

    private PollCommand() {
	// E M P T Y
    }
    
    public static PollCommand getInstance() {
	if (command == null) {
	    command = new PollCommand();
	}
	return command;
    }
    
    @Override
    public void runCommand(String type, Object event) {
	switch (type) {
	case CommandConstant.SLASH:
	    runSlashCommand(event);
	    break;
	case CommandConstant.BUTTONINTERACTION:
	    runButtonCommand(event);
	    break;
	}
    }

    private void runButtonCommand(Object event) {
	// TODO Auto-generated method stub
	
    }

    private void runSlashCommand(Object event) {
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