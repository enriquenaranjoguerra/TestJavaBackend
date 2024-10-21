package com.opotromatic.controllers;

import com.opotromatic.repositories.*;
import com.opotromatic.services.ControllerUtils;
import com.opotromatic.services.QaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/questions")

public class ViewsController {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QaService qaService;
    private final ControllerUtils controllerUtils;

    public ViewsController(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            QaService qaService,
            ControllerUtils controllerUtils) {
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.qaService = qaService;
        this.controllerUtils = controllerUtils;
    }


    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/prueba")
    public String prueba(Model model) {
        String message = "¡Hola desde el controlador";
        model.addAttribute("message", message);
        return "prueba";
    }

    @GetMapping("/Ejemplo1")
    public String ejemplo1() {
        return "Ejemplo1";
    }


}
