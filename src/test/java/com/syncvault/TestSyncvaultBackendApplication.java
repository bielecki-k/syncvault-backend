package com.syncvault;

import org.springframework.boot.SpringApplication;

public class TestSyncvaultBackendApplication {

	static void main(String[] args) {
		SpringApplication.from(SyncvaultBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
