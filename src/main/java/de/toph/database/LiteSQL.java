package de.toph.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiteSQL{

    private static Logger logger = LoggerFactory.getLogger(LiteSQL.class);

    private static Connection con;
    private static Statement statement;
    
    private static boolean isNew = false;

    public static void connect(String string) {
	con = null;
	try {
	    File file = new File(string);
	    if (!file.exists()) {
		file.createNewFile();
		isNew = true;
	    }
	    String url = "jdbc:sqlite:" + file.getPath();
	    con = DriverManager.getConnection(url);

	    logger.info("Connected to DB");

	    statement = con.createStatement();

	    if(isNew) {
		createTable();
	    }
	    
	} catch (SQLException | IOException e) {
	    e.printStackTrace();
	}
    }
    
    private static void createTable() {
	// Roles
	StringBuilder sb = new StringBuilder();
	sb.append("CREATE TABLE if not exists roles ( ");
	sb.append("ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
	sb.append("guildid NUMERIC NOT NULL, ");
	sb.append("rolename TEXT NOT NULL, ");
	sb.append("roleid NUMERIC NOT NULL) ");
	
	try {
	    statement.execute(sb.toString());
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    public static void disconnect() {
	try {
	    if (con != null) {
		con.close();
		logger.info("Disconnected from Database");
	    }
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    public static void onUpdate(String sql) {
	try {
	    statement.execute(sql);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    public static ResultSet onQuery(String sql) {
	try {
	    return statement.executeQuery(sql);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	}
	return null;
    }

}
