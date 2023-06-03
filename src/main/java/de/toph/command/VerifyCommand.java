package de.toph.command;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.database.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

/**
 * Command to verify user
 * 
 * @author Tophhhhh
 *
 */
public class VerifyCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCommand.class);

    /**
     * Run command button interaction
     * 
     * @param event
     */
    @Override
    protected void runButtonCommand(ButtonInteractionEvent event) {
	if (!event.getComponentId().equals("verify-accept") && !(event.getChannelType() == ChannelType.TEXT)) {
	    return;
	}
	User user = event.getUser();
	Guild guild = event.getGuild();
	Long roleid = getVerifyRoleId(guild.getIdLong());

	Role role = guild.getRoleById(roleid);
	if(role == null && roleid != null) {
	    deleteVerifyRoleEntry(guild.getIdLong());
	}
	if (role == null) {
	    RoleAction ra = guild.createRole();
	    ra.setName("Verify");
	    ra.setColor(Color.ORANGE);
	    ra.queue(e -> {
		createVerifyRole(e.getGuild().getIdLong(), e.getIdLong());
		guild.addRoleToMember(user, e).queue();
		user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Du wurdest Verifiziert!")).queue();
	    });
	} else {
	    guild.addRoleToMember(user, role).queue();
	    user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Du wurdest Verifiziert!")).queue();
	}
	event.deferEdit().complete();
    }

    /**
     * run verify command
     * 
     * @param event
     */
    @Override
    protected void runMessageCommand(MessageReceivedEvent event) {
	if(!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
	    event.getChannel().sendMessage("Du hast keine Rechte dazu!").queue();
	}
	
	Guild guild = event.getGuild();
	String name = guild.getName();
	String iconUrl = guild.getIconUrl();
	
	event.getChannel().sendMessageEmbeds(createVerifyEmbeded(name, iconUrl))
	    .addActionRow(Button.primary("verify-accept", Emoji.fromUnicode("U+2705"))).queue();
	event.getChannel().deleteMessageById(event.getMessageId()).queue();
    }
    
    /**
     * create verify embeded
     * 
     * @param name
     * @param iconUrl
     * @return Message embed
     */
    private MessageEmbed createVerifyEmbeded(String name, String iconUrl) {
	EmbedBuilder eb = new EmbedBuilder();
	eb.setColor(Color.red);
	eb.setTitle("Verifiziere dich!");
	eb.setDescription("Um alle Channel sehen zu können und dich in dem Server frei bewegen zu können musst du Verifiziert sein!");
	eb.addField("Wie?", "Drücke auf den Butten unter der Nachricht!", false);
	eb.setFooter(String.format("Created by %s", name), iconUrl);
	return eb.build();
    }
    
    /**
     * Create verify role if not exists in database
     * 
     * @param guildId
     * @param roleId
     */
    private void createVerifyRole(Long guildId, Long roleId) {
	StringBuilder sb = new StringBuilder();
	sb.append("INSERT INTO roles(guildid,rolename,roleid) ");
	sb.append("VALUES(" + guildId + ", 'Verify'," + roleId + ");");
	LiteSQL.onUpdate(sb.toString());
	LOGGER.info("Verify role created!");
    }

    /**
     * Get verify role ID from Database
     * 
     * @param guildId
     * @return verifyroleId
     */
    private Long getVerifyRoleId(Long guildId) {
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT DISTINCT * FROM roles ");
	sb.append("WHERE guildid = " + guildId + " AND rolename = 'Verify'");
	Long rid = null;
	ResultSet rs = LiteSQL.onQuery(sb.toString());
	try {
	    if (rs.next()) {
		rid = rs.getLong("roleid");
	    }
	    if (rs != null) {
		rs.close();
	    }
	    return rid == null ? -1l : Long.valueOf(rid);
	} catch (SQLException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return 0l;
    }
    
    /**
     * Delete verify role if entry in database exists<br> 
     * but role doesnt exists on server
     * 
     * @param guildId
     */
    private void deleteVerifyRoleEntry(Long guildId) {
	StringBuilder sb = new StringBuilder();
	sb.append("DELETE * FROM roles ");
	sb.append("where guildid = " + guildId + " AND rolename = 'Verify' ");
	LiteSQL.onUpdate(sb.toString());
	LOGGER.info("Verfiyrole deleted!");
    }
}
