package com.mobitel.storageMigration;

import com.mobitel.storageMigration.service.FileReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StorageMigrationApplication  implements ApplicationRunner {
	@Autowired
	FileReadService fileReadService;

	public static void main(String[] args) {
		SpringApplication.run(StorageMigrationApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		fileReadService.readExcelFile();
	}
}
