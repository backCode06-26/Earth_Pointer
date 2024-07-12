package com.earth_pointer.controller;

import com.earth_pointer.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userController {

    private final MailService mailService;

    @Autowired
    public userController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping({"", "/"})
    public String home() {

        mailService.sendEmailVerification("hogin1005@gmail.com");
        return "home";
    }
}
