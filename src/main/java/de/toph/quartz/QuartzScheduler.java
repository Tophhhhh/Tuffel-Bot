package de.toph.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzScheduler {

    private Scheduler scheduler;
    
    public void start() throws SchedulerException {
	scheduler = new StdSchedulerFactory().getScheduler();
	scheduler.start();
    }
    
    public void init() throws SchedulerException {
	Trigger triggerNew = TriggerBuilder.newTrigger().withIdentity("NameOfTrigger","NameOfGroup")
		.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

	JobDetail jobInstance = JobBuilder.newJob(TestJob.class).withIdentity("NameOfJob","NameOfGroup").build();
	
	scheduler.scheduleJob(jobInstance, triggerNew);
    }
    
    public void stop() throws SchedulerException {
	scheduler.shutdown();
    }
}