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
    
    private String key;
    
    public Config() {
	try(FileReader fr = new FileReader(new File("src/main/resources/config/application.properties"))) {
	    Properties prop = new Properties();
	    prop.load(fr);
	    key = prop.getProperty("bot.token");
	    
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    public String getKey() {
        return StringUtil.emtpyStringIfNull(key);
    }

    public void setKey(String key) {
        this.key = key;
    }
}