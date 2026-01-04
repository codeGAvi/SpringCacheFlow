package com.example.SpringCacheFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringCacheFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCacheFlowApplication.class, args);
	}

}
