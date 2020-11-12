package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.LoginDto;
import com.glenn.myblog.dto.UserDto;
import com.glenn.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping
    public String moveToUserListPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute UserDto userDto) {
        userService.save(userDto);
        return "redirect:/login";
    }

    @PostMapping("/withdraw")
    public String deleteUser(HttpSession httpSession, @ModelAttribute String password) {
        Long id = ((LoginDto) httpSession.getAttribute("loginDto"))
                .getId();
        userService.deleteUser(id, password);
        return "redirect:/";
    }
}
