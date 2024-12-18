package com.example.sampleproject.member.controller;

import com.example.sampleproject.member.spring.AuthInfo;
import com.example.sampleproject.member.spring.AuthService;
import com.example.sampleproject.member.spring.WrongIdPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String LoginForm(LoginCommand loginCommand, Model model,
                       @CookieValue(value = "REMEMBER", required = false) Cookie rCookie) {

        if (rCookie != null) {
            loginCommand.setEmail(rCookie.getValue());
            loginCommand.setRememberEmail(true);
        }
        System.out.println("-----------------여기");
        System.out.println(loginCommand.getEmail());

        model.addAttribute("loginCommand", loginCommand);

        return "/member/loginForm";
    }

    @PostMapping("/login")
    public String submit(LoginCommand loginCommand, Errors errors, HttpSession session, HttpServletResponse response, Model model) {

        new LoginCommandValidator().validate(loginCommand, errors);
        if (errors.hasErrors()) {
            return "/member/loginForm";
        }
        try {
            AuthInfo authInfo = authService.authenticate(
                    loginCommand.getEmail(),
                    loginCommand.getPassword());
            session.setAttribute("authInfo", authInfo);
            System.out.println(authInfo.getId() + " / " + authInfo.getEmail() + " / "+ authInfo.getName() + " / " + authInfo.getRole() + " 세션 저장!");

            Cookie rememberCookie = new Cookie("REMEMBER", loginCommand.getEmail());
            rememberCookie.setPath("/");
            if(loginCommand.isRememberEmail()) {
                rememberCookie.setMaxAge(60*60*24*30);
            } else {
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);

            return "/main/home";
        } catch (WrongIdPasswordException e) {
            errors.reject("idPasswordNotMatching", "아이디 또는 비밀번호가 일치하지 않습니다.");
            model.addAttribute("loginCommand", loginCommand);
            return "/member/loginForm";
        }
    }

}
