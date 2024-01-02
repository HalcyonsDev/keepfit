package com.halcyon.keepfit;

import com.halcyon.keepfit.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class KeepfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeepfitApplication.class, args);
	}

}
