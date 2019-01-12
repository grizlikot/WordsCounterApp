package com.emilos.files.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @author Ervin Milos
 * 
 * Spring Boot main application class
 */

@EnableAsync
@SpringBootApplication
@ComponentScan(basePackages = {"com.emilos.files.app"})
public class Application 
{
    public static void main( String[] args )
    {
       SpringApplication.run(Application.class, args);
    }
}
