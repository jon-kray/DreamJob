package ru.ecosystem.dreamjob.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DreamjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamjobApplication.class, args);
    }

}
