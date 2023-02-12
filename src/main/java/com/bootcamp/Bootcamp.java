package com.bootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class Bootcamp {

	public static void main(String[] args) {
		SpringApplication.run(Bootcamp.class, args);
	}

}
