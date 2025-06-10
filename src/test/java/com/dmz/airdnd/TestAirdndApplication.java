package com.dmz.airdnd;

import org.springframework.boot.SpringApplication;

public class TestAirdndApplication {

	public static void main(String[] args) {
		SpringApplication.from(AirdndApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
