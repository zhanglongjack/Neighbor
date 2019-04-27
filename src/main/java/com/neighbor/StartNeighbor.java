package com.neighbor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.neighbor.app.commission.entity.CommissionHandleTask;
import com.neighbor.app.robot.entity.GrapPacketData;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;

/**
 * Hello world!
 * 
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
public class StartNeighbor {
	public static void main(String[] args) {
		// SpringApplication.run(StartCRMApp.class,args);
		SpringApplication app = new SpringApplication(StartNeighbor.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}
	
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect(new GroupingStrategy());
	}
	
	@Bean
	public BlockingQueue<GrapPacketData> getRobotQueue() {
		return new LinkedBlockingQueue<GrapPacketData>(2);
	}
	
	@Bean
	public BlockingQueue<CommissionHandleTask> getCommisionHandleTaskQueue() {
		return new LinkedBlockingQueue<CommissionHandleTask>(100);
	}
}
