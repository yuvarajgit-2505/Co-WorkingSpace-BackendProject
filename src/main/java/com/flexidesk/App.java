package com.flexidesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class App 
{
     @Value("${spring.datasource.url}")
    private String testUrl;
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }
      @PostConstruct
    public void testEnv() {
        System.out.println("DB URL = " + testUrl);
    }
}

