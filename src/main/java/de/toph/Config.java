package de.toph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.util.StringUtil;

/**
 * 
 * @author Tophhhhh
 *
 * Bot configuration
 */
public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);
    
    private String key;
    private String dbPath;
    private String weatherKey;
    
    public Config() {
	try(FileReader fr = new FileReader(new File("src/main/resources/config/application.properties"))) {
	    Properties prop = new Properties();
	    prop.load(fr);
	    key = prop.getProperty("bot.token");
	    dbPath = prop.getProperty("db.connection");
	    weatherKey = prop.getProperty("bot.weather");
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    // K E Y
    
    public String getKey() {
        return StringUtil.emtpyStringIfNull(key);
    }

    // D B - P A T H
    
    public String getDbPath() {
        return dbPath;
    }
    
    // W E A T H E R - K E Y
    
    public String getWeatherKey() {
	return weatherKey;
    }
}