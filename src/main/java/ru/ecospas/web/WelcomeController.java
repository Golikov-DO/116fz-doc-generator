package ru.ecospas.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String welcome(HttpServletRequest request) {

        request.setAttribute(
                "contentPage",
                "/WEB-INF/pages/index.jsp"
        );

        return "forward:/WEB-INF/layout.jsp";
    }
}