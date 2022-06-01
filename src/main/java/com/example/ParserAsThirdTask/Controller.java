package com.example.ParserAsThirdTask;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {

    @GetMapping("/")
    public String index() throws IOException {
        return "Something";
    }

}