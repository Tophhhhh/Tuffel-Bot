package de.toph.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.constant.CommandConstant;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * 
 * @author Tophhhhh
 *
 * Moveall command
 */
public class MoveallCommand implements ICommand {

    private static MoveallCommand command;

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveallCommand.class);

    /**
     * 
     * get instance of moveall
     * 
     * @return MoveallCommand
     */
    public static MoveallCommand getInstance() {
	if (command == null) {
	    command = new MoveallCommand();
	}
	return command;
    }

    /**
     * run command 
     */
    @Override
    public void runCommand(String type, Object event) {
	switch (type) {
	case CommandConstant.SLASH:
	    runSlashCommand(event);
	    break;
	}
    }

    /**
     * 
     * @param event
     */
    private void runSlashCommand(Object event) {
	SlashCommandInteractionEvent slashEvent = (SlashCommandInteractionEvent) event;
	Member member = slashEvent.getMember();
	AudioChannelUnion acu = member.getVoiceState().getChannel();
	if (acu == null) {
	    slashEvent.reply("Du befindest dich nicht in einem VoiceChannel!").queue();
	    return;
	}
	List<Member> members = acu.getMembers();
	GuildChannelUnion channel = slashEvent.getOption("channel").getAsChannel();
	if (channel.getType() == ChannelType.VOICE) {
	    for (Member m : members) {
		slashEvent.getGuild().moveVoiceMember(m, channel.asVoiceChannel()).queue();
	    }
	    slashEvent.reply(String.format("Member aus dem channel `%s` wurden von %s verschoben", acu.getName(), member.getAsMention())).queue();
	    LOGGER.info("User succesfull moved!");
	} else {
	    slashEvent.reply("Wrong Channeltype! Please select voicechannel!").setEphemeral(true).queue();
	}
    }

}
