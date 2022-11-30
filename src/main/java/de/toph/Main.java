package de.toph;

import javax.security.auth.login.LoginException;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Toph
 * 
 *         Main class
 *
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
	try {
	    DiscordBot.getInstance().execute(args);
	    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	    scheduler.start();
	    
	    
	    scheduler.shutdown();
	} catch (LoginException e) {
	    logger.error(e.getMessage(), e);
	} catch (SchedulerException e) {
	    logger.error(e.getMessage(), e);
	}
    }

}
