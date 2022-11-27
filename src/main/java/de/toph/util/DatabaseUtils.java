package de.toph.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.database.LiteSQL;

public class DatabaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);

    public static Long getRole(Long guildId, String role) {
	Long rid = null;
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT DISTINCT * FROM roles ");
	sb.append("WHERE guildid = " + guildId + " AND rolename like '" + role + "'");
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
	    logger.error(e.getMessage(), e);
	}
	return 0l;
    }

}
