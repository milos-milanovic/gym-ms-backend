package dev.milosmilanovic.gymms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {

    @RequestMapping("/")
    public String rootResponse() {
        return "Gym Management System API";
    }

}
