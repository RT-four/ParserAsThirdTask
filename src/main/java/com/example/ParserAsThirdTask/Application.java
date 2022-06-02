package com.example.ParserAsThirdTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(Application.class, args);
        Parser.start();
    }


}
