package com.glenn.myblog.web;

import com.glenn.myblog.dto.UserDto;
import com.glenn.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public String createUser(@Valid UserDto userDto) {
        userService.save(userDto);
        return "redirect:/";
    }
}
