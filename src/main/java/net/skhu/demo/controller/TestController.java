package net.skhu.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by ds on 2018-03-26.
 */

@Controller
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("main")
    public String main(Model model) {
        String id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("user", id);
        return "main";
    }

    @GetMapping("error")
    public String error() {
        return "error";
    }
}
