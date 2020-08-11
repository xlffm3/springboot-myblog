package com.glenn.myblog.web;

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
}
