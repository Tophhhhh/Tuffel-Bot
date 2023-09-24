package de.toph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.util.StringUtil;

/**
 * @author Tophhhhh
 * <p>
 * Bot configuration
 */
public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private static Config INSTANCE;

    private Properties prop;

    private Config() {
        try (FileReader fr = new FileReader(new File("src/main/resources/config/application.properties"))) {
            prop = new Properties();
            prop.load(fr);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * get the instance of config
     *
     * @return Config
     */
    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    // K E Y

    public String getKey() {
        return StringUtil.emtpyStringIfNull(prop.getProperty("bot.token"));
    }

    // D B - P A T H

    public String getDbPath() {
        return prop.getProperty("bot.db.connection");
    }

    // W E A T H E R - K E Y

    public String getWeatherKey() {
        return prop.getProperty("bot.weather");
    }
}