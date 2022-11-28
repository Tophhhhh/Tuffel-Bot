package de.toph.listener;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.database.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

/**
 * 
 * @author Toph
 *
 *         Verify Listener to verify new Users ADMIN!!
 */
public class VerifyListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyListener.class);

    /**
     * GatewayIntent.MESSAGE_CONTENT
     * 
     * is needed
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
	if (event.getAuthor().isBot()) {
	    return;
	}
	String message = event.getMessage().getContentRaw();
	if(!message.startsWith("!")) {
	    return;
	}
	if (message.equals("!verify")) {
	    Guild guild = event.getGuild();
	    String name = guild.getName();
	    String iconUrl = guild.getIconUrl();

	    event.getChannel().sendMessageEmbeds(verifyEmbeded(name, iconUrl))
		    .addActionRow(Button.primary("Verify", Emoji.fromUnicode("U+2705"))).queue();
	    event.getChannel().deleteMessageById(event.getMessageId()).queue();
	}
    }

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {
	if (event.getName().equals("Verify")) {
	    String name = event.getGuild().getName();
	    String iconUrl = event.getGuild().getIconUrl();
	    event.replyEmbeds(verifyEmbeded(name, iconUrl))
		    .addActionRow(Button.primary("Verify", Emoji.fromUnicode("U+2705"))).queue();
	}
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	if ("verify".equals(event.getName())) {
	    String name = event.getGuild().getName();
	    String iconUrl = event.getGuild().getIconUrl();
	    event.replyEmbeds(verifyEmbeded(name, iconUrl))
		    .addActionRow(Button.primary("Verify", Emoji.fromUnicode("U+2705"))).queue();
	}
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
	if (event.getComponentId().equals("Verify") && event.getChannelType() == ChannelType.TEXT) {
	    User user = event.getUser();
	    Guild guild = event.getGuild();
	    Long roleid = getVerifyRoleId(guild.getIdLong());

	    Role role = guild.getRoleById(roleid);
	    if (role == null) {
		RoleAction ra = guild.createRole();
		ra.setName("Verify");
		ra.setColor(Color.ORANGE);
		ra.queue(e -> {
		    createVerifyRole(e.getGuild().getIdLong(), e.getIdLong());
		    guild.addRoleToMember(user, e).queue();
		    user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Du wurdest Verifiziert!"))
			    .queue();
		});
	    } else {
		guild.addRoleToMember(user, role).queue();
		user.openPrivateChannel().flatMap(channel -> channel.sendMessage("Du wurdest Verifiziert!")).queue();
	    }
	    event.deferEdit().complete();
	}
    }

    /**
     * @param name
     * @param iconUrl
     * @return verify Embeded
     */
    private MessageEmbed verifyEmbeded(String name, String iconUrl) {
	EmbedBuilder eb = new EmbedBuilder();
	eb.setColor(Color.red);
	eb.setTitle("Verifiziere dich!");
	eb.setDescription("Um alle Channel sehen zu können und dich in dem Server frei bewegen zu können musst du Verifiziert sein!");
	eb.addField("Wie?", "Drücke auf den Butten unter der Nachricht!", false);
	eb.setFooter(String.format("Created by %s", name), iconUrl);
	return eb.build();
    }

    /**
     * Method to create the verify role if not exist
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
     * Method to get the id for the verify role
     * 
     * @param guildId
     * @return roleId
     */
    private long getVerifyRoleId(Long guildId) {
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
	    return rid == null ? -1l : rid;
	} catch (SQLException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return 0l;
    }
}