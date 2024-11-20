package com.example.sampleproject.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String MainPage(Model model) {
        return "/main/home";
    }

    @GetMapping("/about")
    public String AboutPage() {
        return "/main/about";
    }

    @GetMapping("/services")
    public String ServicesPage() {
        return "/main/services";
    }

    @GetMapping("/contact")
    public String ContactPage() {
        return "/main/contact";
    }

}
