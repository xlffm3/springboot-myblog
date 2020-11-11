package com.glenn.myblog.web.controller;

import com.glenn.myblog.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/login")
@Controller
public class LoginController {

    private final LoginService loginService;
}
