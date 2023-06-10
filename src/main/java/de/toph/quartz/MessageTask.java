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
	LOGGER.info("test");
	
	try {
//	    Guild guild = DiscordBot.getInstance().getJda().getGuildById("690294066990415893"); NMCE
	    Guild guild = DiscordBot.getInstance().getJda().getGuildById("702995451242086442");
	    if(guild != null){
//		TextChannel tc = guild.getTextChannelById("1006268808681488587"); NMCE
		TextChannel tc = guild.getTextChannelById("903670109619503114");
		if(tc != null) {
		    tc.sendMessage("dummy").queue();
		}
	    }
	} catch (LoginException e) {
	    LOGGER.error(e.getMessage(), e);
	}
	LOGGER.debug("start");
    }

}
