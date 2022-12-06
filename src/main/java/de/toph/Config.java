package de.toph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.util.StringUtil;

public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);
    
    private static String key;
    private static String dbPath;
    
    public Config() {
	try(FileReader fr = new FileReader(new File("src/main/resources/config/application.properties"))) {
	    Properties prop = new Properties();
	    prop.load(fr);
	    key = prop.getProperty("bot.token");
	    dbPath = prop.getProperty("db.connection");
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    // K E Y
    
    public static String getKey() {
        return StringUtil.emtpyStringIfNull(key);
    }

    // D B - P A T H
    
    public static String getDbPath() {
        return dbPath;
    }
}