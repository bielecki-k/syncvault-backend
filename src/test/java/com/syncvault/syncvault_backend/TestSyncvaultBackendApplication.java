package com.syncvault.syncvault_backend;

import com.syncvault.SyncvaultBackendApplication;
import org.springframework.boot.SpringApplication;

public class TestSyncvaultBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(SyncvaultBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
