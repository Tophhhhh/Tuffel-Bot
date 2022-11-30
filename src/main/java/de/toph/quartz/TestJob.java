package de.toph.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob implements Job{

    private Logger logger = LoggerFactory.getLogger(TestJob.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
	logger.debug("test");
    }

}
