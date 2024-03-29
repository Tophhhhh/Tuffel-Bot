package de.toph.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

/**
 * @author Tophhhhh
 * <p>
 * Moveall command
 */
public class MoveallCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoveallCommand.class);

    /**
     * @param event
     */
    @Override
    protected void runSlashCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        AudioChannelUnion acu = member.getVoiceState().getChannel();
        if (acu == null) {
            event.reply("Du befindest dich nicht in einem VoiceChannel!").queue();
            return;
        }
        List<Member> members = acu.getMembers();
        GuildChannelUnion channel = event.getOption("channel").getAsChannel();
        if (channel.getType() == ChannelType.VOICE) {
            for (Member m : members) {
                event.getGuild().moveVoiceMember(m, channel.asVoiceChannel()).queue();
            }
            event.reply(String.format("Member aus dem channel `%s` wurden von %s verschoben", acu.getName(), member.getAsMention())).queue();
            LOGGER.info("User succesfull moved!");
        } else {
            event.reply("Wrong Channeltype! Please select voicechannel!").setEphemeral(true).queue();
        }
    }

}
