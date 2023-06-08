package de.toph.quartz;

import javax.security.auth.login.LoginException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * 
 * @author Tophhhhh
 *
 */
public class MessageTask implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTask.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
	
	try {
	    Guild guild = DiscordBot.getInstance().getJda().getGuildById("");
	    if(guild != null){
		TextChannel tc = guild.getTextChannelById("");
		if(tc != null) {
		    tc.sendMessage("dummy");
		}
	    }
	} catch (LoginException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	LOGGER.debug("start");
    }

}
