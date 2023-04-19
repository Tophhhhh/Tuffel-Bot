package de.toph.listener;

import de.toph.database.LiteSQL;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Tophhhhh
 *
 * Delete role from Database
 */
public class RoleListener extends ListenerAdapter {

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {
	Long roleid = event.getRole().getIdLong();
	StringBuilder sb = new StringBuilder();
	sb.append("DELETE FROM roles ");
	sb.append("WHERE guildid = " + event.getGuild().getIdLong());
	sb.append(" AND roleid = " + roleid);
	LiteSQL.onUpdate(sb.toString());
    }
}