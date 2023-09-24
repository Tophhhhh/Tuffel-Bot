package de.toph.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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
//	Trigger triggerNew = TriggerBuilder.newTrigger().withIdentity("NameOfTrigger","NameOfGroup")
//		.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
//	
//	
//	// Pattern Sec, Min, Hour, day, month, dayofweek
//	Trigger triggerCron = TriggerBuilder.newTrigger().withIdentity("NameOfTrigger","NameOfGroup")
//		.withSchedule(CronScheduleBuilder.cronSchedule("0 0 17 * * SAT,SUN")).build();
//	// 00 17 * * SAT,SUN

        Trigger caligor = TriggerBuilder.newTrigger().withIdentity("caligor", "nostale")
//		.withSchedule(CronScheduleBuilder.cronSchedule("0 0 17 * * SAT,SUN")).build();
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")).build();

//	JobDetail jobInstance = JobBuilder.newJob(TestJob.class).withIdentity("NameOfJob","NameOfGroup").build();

        JobDetail caligorInstance = JobBuilder.newJob(MessageTask.class).withIdentity("caligor", "nostale").build();

//	scheduler.scheduleJob(jobInstance, triggerNew);
        scheduler.scheduleJob(caligorInstance, caligor);
    }

    public void stop() throws SchedulerException {
        scheduler.shutdown();
    }
}