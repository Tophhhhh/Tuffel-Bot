package de.toph.tuffelbot.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoveallListener extends AbstractCommandListener {

	private final Logger LOGGER = LoggerFactory.getLogger(MoveallListener.class);

	private final String MOVE_ALL = "moveall";

	@Override
	public CommandData getCommandData() {
		return Commands.slash(MOVE_ALL, "Verschiebt alle User in deinem Channel")
				.addOption(OptionType.CHANNEL, "channel", "Channel in den jeder gemoved werden soll", true)
				.setGuildOnly(true)
				.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_MOVE_OTHERS));
	}

	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		if(!MOVE_ALL.equals(event.getName())) {
			return;
		}
		Member member = event.getMember();
		AudioChannelUnion acu = member.getVoiceState().getChannel();
		if(acu == null) {
			event.reply("Du befindest dich nicht in einem VoiceChannel!").queue();
			LOGGER.info("User wasn't at a voice channel!");
			return;
		}

		List<Member> members = acu.getMembers();
		GuildChannelUnion channel = event.getOption("channel").getAsChannel();
		if(channel.getType() == ChannelType.VOICE) {
			for (Member m : members) {
				event.getGuild().moveVoiceMember(m, channel.asVoiceChannel()).queue();
			}
			event.reply(String.format("Member aus dem channel `%s` wurden von %s verschoben",
							acu.getName(),
							member.getAsMention()))
					.queue();
			LOGGER.info("User successful moved!");
		} else {
			event.reply("Wrong Channeltype! Please select voicechannel!")
					.setEphemeral(true)
					.queue();
		}
	}
}
