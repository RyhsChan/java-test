package com.java.test.quartz;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzTest {
	private static final Logger logger = LoggerFactory
			.getLogger(QuartzTest.class);

	public static void main(String[] args) throws Exception {
		JobDetail job = new JobDetail();
		job.setName("dummyJobName");
		job.setJobClass(HelloJob.class);

		// configure the scheduler time
		SimpleTrigger trigger = new SimpleTrigger();
		trigger.setName("dummyTrigger");
		trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
		trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		trigger.setRepeatInterval(1000);

		// schedule it
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, trigger);

		logger.info("main thread: {}", Thread.currentThread().getId());
	}
}
