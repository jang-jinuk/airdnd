package com.dmz.be_airdnd;

import org.springframework.boot.SpringApplication;

public class TestBeAirdndApplication {

	public static void main(String[] args) {
		SpringApplication.from(BeAirdndApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
