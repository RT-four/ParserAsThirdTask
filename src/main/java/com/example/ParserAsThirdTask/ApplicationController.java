package com.example.ParserAsThirdTask;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

 @Controller
 public class ApplicationController {

    @GetMapping("/")
    public String handle(Model model) {
        List<Card> cards = Parser.getCardsToShow();
        model.addAttribute("cards", cards);
        return "application";
    }

}