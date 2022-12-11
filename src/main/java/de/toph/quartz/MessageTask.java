package de.toph.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTask implements Job{

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTask.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
	LOGGER.debug("start");
    }

}
