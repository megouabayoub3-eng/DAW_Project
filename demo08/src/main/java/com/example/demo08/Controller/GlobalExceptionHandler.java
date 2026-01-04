package com.example.demo08.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleConflict(DataIntegrityViolationException ex, Model model) {
        log.warn("Data integrity violation", ex);
        model.addAttribute("error", "A user with that email or username already exists.");
        return "signup";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, Model model) {
        log.warn("Bad request: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "signup";
    }

    @ExceptionHandler(Exception.class)
    public String handleAny(Exception ex, Model model) {
        log.error("Unhandled exception", ex);
        model.addAttribute("error", "An unexpected error occurred. Please try again later.");
        return "error";
    }
}
