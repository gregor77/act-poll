package com.act.poll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories("com.act.poll")
@SpringBootApplication
public class PollApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollApplication.class, args);
	}

}

