package de.toph.listener;

import de.toph.database.LiteSQL;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tophhhhh
 * <p>
 * Delete role from Database
 */
public class RoleListener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleListener.class);

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