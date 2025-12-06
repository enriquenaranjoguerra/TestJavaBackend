package com.opotromatic.controllers;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

    @Autowired
    private ConfigurableApplicationContext context;

    @GetMapping("/shutdown")
    public String shutdownApplication() {
        context.close();
        return "Cerrando la aplicación... ¡Gracias por usar TestApp!";
    }
}