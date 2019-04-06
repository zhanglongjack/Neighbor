package com.neighbor.schedule.util;
 
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
 
//@Configuration
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer{
	
	@Override
	public void customize(SchedulerFactoryBean schedulerFactoryBean) {
		 	schedulerFactoryBean.setStartupDelay(20);
	        schedulerFactoryBean.setAutoStartup(true);
	        schedulerFactoryBean.setOverwriteExistingJobs(true);
	}
 
}
