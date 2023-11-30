package isy.task.com.test_isy_task_auto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class IsyTaskTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsyTaskTestApplication.class, args);
	}

}
