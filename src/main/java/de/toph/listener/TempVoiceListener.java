package de.toph.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tophhhhh
 * <p>
 * Tempchannel for Voicechat
 */
public class TempVoiceListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempVoiceListener.class);

    private List<Long> tempchannels;

    public TempVoiceListener() {
        tempchannels = new ArrayList<>();
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

        AudioChannelUnion join = event.getChannelJoined(); // null if member disconected
        AudioChannelUnion left = event.getChannelLeft(); // null if member connected
        Member member = event.getMember();

        if (join != null && left != null) {
            onLeave(left);
            onJoin(join, member);
        } else if (join != null) {
            onJoin(join, member);
        } else {
            onLeave(left);
        }
    }

    private List<Permission> allowPermission() {
        List<Permission> permission = new ArrayList<>();
        permission.add(Permission.CREATE_INSTANT_INVITE);
        permission.add(Permission.MANAGE_CHANNEL);
        permission.add(Permission.MANAGE_PERMISSIONS);
        return permission;
    }

    private void onJoin(AudioChannelUnion joined, Member member) {
        if (joined.getIdLong() == 921551994865479710L) {
            Category cat = joined.getParentCategory();
            VoiceChannel vc = cat.createVoiceChannel("🔊| " + member.getEffectiveName()).complete();
            vc.getManager().putMemberPermissionOverride(member.getIdLong(), allowPermission(), Collections.emptyList())
                    .putRolePermissionOverride(cat.getGuild().getPublicRole().getIdLong(), 0l,
                            Permission.VIEW_CHANNEL.getRawValue())
                    .queue();

            vc.getGuild().moveVoiceMember(member, vc).queue();
            tempchannels.add(vc.getIdLong());
        }
    }

    private void onLeave(AudioChannelUnion leave) {
        if (leave.getMembers().size() <= 0) {
            if (tempchannels.contains(leave.getIdLong())) {
                tempchannels.remove(leave.getIdLong());
                leave.delete().queue();
            }
        }
    }
}
