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

    private static VerifyCommand command;

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCommand.class);

    private VerifyCommand() {
	// E M P T Y
    }
    
    /**
     * 
     * get instance of verifycommand
     * 
     * @return Verifycommand
     */
    public static VerifyCommand getInstance() {
	if (command == null) {
	    command = new VerifyCommand();
	}
	return command;
    }
    
    /**
     * Run command button interaction
     * 
     * @param event
     */
    @Override
    protected void runButtonCommand(Object event) {
	ButtonInteractionEvent buttonEvent = (ButtonInteractionEvent) event;
	if (!buttonEvent.getComponentId().equals("verify-accept") && !(buttonEvent.getChannelType() == ChannelType.TEXT)) {
	    return;
	}
	User user = buttonEvent.getUser();
	Guild guild = buttonEvent.getGuild();
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
	buttonEvent.deferEdit().complete();
    }

    /**
     * run verify command
     * 
     * @param event
     */
    @Override
    protected void runMessageCommand(Object event) {
	MessageReceivedEvent messageEvent = (MessageReceivedEvent) event;
	if(!messageEvent.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
	    messageEvent.getChannel().sendMessage("Du hast keine Rechte dazu!").queue();
	}
	
	Guild guild = messageEvent.getGuild();
	String name = guild.getName();
	String iconUrl = guild.getIconUrl();
	
	messageEvent.getChannel().sendMessageEmbeds(createVerifyEmbeded(name, iconUrl))
	    .addActionRow(Button.primary("verify-accept", Emoji.fromUnicode("U+2705"))).queue();
	messageEvent.getChannel().deleteMessageById(messageEvent.getMessageId()).queue();
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
