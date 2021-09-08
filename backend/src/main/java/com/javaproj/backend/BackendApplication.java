package com.javaproj.backend;

import com.javaproj.backend.api.EdukgController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

    public static void main(String[] args) {
        EdukgController.idForEdukg();
        SpringApplication.run(BackendApplication.class, args);
    }

}
