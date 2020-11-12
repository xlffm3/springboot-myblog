package com.glenn.myblog.web;

import com.glenn.myblog.domain.exception.DuplicatedUserEmailException;
import com.glenn.myblog.domain.exception.WrongEmailException;
import com.glenn.myblog.domain.exception.WrongPasswordException;
import com.glenn.myblog.dto.UserDto;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException bindException) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("org.springframework.validation.BindingResult.userDto", bindException.getBindingResult());
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @ExceptionHandler(DuplicatedUserEmailException.class)
    public ModelAndView handleDuplicatedUserEmailException(DuplicatedUserEmailException duplicatedUserEmailException) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", true);
        modelAndView.addObject("message", duplicatedUserEmailException.getMessage());
        modelAndView.addObject("userDto", new UserDto());
        modelAndView.setViewName("signup");
        return modelAndView;
    }

    @ExceptionHandler({WrongEmailException.class, WrongPasswordException.class})
    public ModelAndView handleWrongEmailException(RuntimeException loginException) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", true);
        modelAndView.addObject("message", loginException.getMessage());
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
