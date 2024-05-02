package SublindWay_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SublindwayServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SublindwayServerApplication.class, args);
	}

}
