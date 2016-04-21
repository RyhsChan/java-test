package com.java.test.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {
	private static final Logger logger = LoggerFactory
			.getLogger(HelloJob.class);

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("Timer event occurred in thread: {}", Thread
				.currentThread().getId());
	}
}
