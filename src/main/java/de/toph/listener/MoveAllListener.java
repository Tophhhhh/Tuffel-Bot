package de.toph.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Toph
 * 
 *         Listener to move all from current voice channel to another
 *         Permission.VOICE_MOVE_OTHERS
 *
 */
public class MoveAllListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveAllListener.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if (!event.getName().equals("moveall")) {
	    return;
	}
	try {
	    Member member = event.getMember();
	    AudioChannelUnion acu = member.getVoiceState().getChannel();
	    List<Member> members = acu.getMembers();
	    long channelId = event.getOption("voiceid").getAsLong();
	    for (Member m : members) {
		event.getGuild().moveVoiceMember(m, event.getGuild().getVoiceChannelById(channelId)).queue();
	    }
	    event.reply(String.format("Member aus dem channel `%s` wurden von %s verschoben", acu.getName(),
		    member.getAsMention())).queue();
	} catch (NumberFormatException e) {
	    LOGGER.error(e.getMessage(), e);
	    event.reply("Bitte gib eine Gültige id ein!").setEphemeral(true).queue();
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    event.reply("Etwas ist schief gelaufen!").setEphemeral(true).queue();
	}
    }
}
