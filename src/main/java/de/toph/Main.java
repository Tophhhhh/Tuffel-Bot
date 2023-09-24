package de.toph;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Tophhhhh 
 * 
 * Main class
 *
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
	try {
	    DiscordBot.getInstance().execute(args);
	} catch (LoginException e) {
	    LOGGER.error(e.getMessage(), e);
	}
    }

}
