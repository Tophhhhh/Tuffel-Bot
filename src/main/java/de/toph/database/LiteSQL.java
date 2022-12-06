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

import de.toph.Config;

public class LiteSQL{

    private static Logger logger = LoggerFactory.getLogger(LiteSQL.class);

    private static Connection con;
    private static Statement statement;

    public static void connect() {
	con = null;
	try {
	    File file = new File(Config.getDbPath());
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    String url = "jdbc:sqlite:" + file.getPath();
	    con = DriverManager.getConnection(url);

	    logger.info("Connected to DB");

	    statement = con.createStatement();

	} catch (SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public static void disconnect() {
	try {
	    if (con != null) {
		con.close();
		System.out.println("Verbindung zur Datenbank getrennt.");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static void onUpdate(String sql) {
	try {
	    statement.execute(sql);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static ResultSet onQuery(String sql) {
	try {
	    return statement.executeQuery(sql);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

}
