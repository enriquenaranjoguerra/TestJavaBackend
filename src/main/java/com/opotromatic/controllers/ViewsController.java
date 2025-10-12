package com.opotromatic.controllers;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import com.opotromatic.services.QuestionsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/api/questions")

public class ViewsController {

    private final QuestionsService questionsService;

    public ViewsController(QuestionsService questionsService) {
        this.questionsService = questionsService;
    }

    @GetMapping("/listing")
    public String listing(Model model) {

        List<Category> categories = questionsService.getAllCategories();
        categories.forEach(c -> {
            List<Block> blocks = questionsService.getBlocksByCategory(c.getId());
            blocks.forEach(b -> {
                List<Theme> themes = questionsService.getThemesByBlock(b.getId());
            });
            c.setBlocks(blocks);
        });


        model.addAttribute("categories", categories);
        model.addAttribute("message", "Selecciona una categoría");

        return "listing";
    }

    @GetMapping("/questions_by_ids")
    public String getQuestionsByIds(
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) List<Long> blockIds,
            @RequestParam(required = false) List<Long> themeIds,
            @RequestParam(required = false, defaultValue = "10") Integer questionsAmount,
            @RequestParam(required = false, defaultValue = "4") Integer answersAmount,
            Model model) {

        categoryIds = Optional.ofNullable(categoryIds).orElseGet(ArrayList::new);
        blockIds = Optional.ofNullable(blockIds).orElseGet(ArrayList::new);

        List<Question> questions = questionsService.getQuestionsByIds(categoryIds, blockIds, themeIds);

        Collections.shuffle(questions);

        int maxQuestions = Math.min(questionsAmount, questions.size());

        questions = questions.subList(0, maxQuestions);

        model.addAttribute("questions", questions);
        model.addAttribute("message", "Preguntas encontradas: " + questions.size());
        model.addAttribute("answersAmount", answersAmount);

        return "questions-list";
    }

    @PostMapping("/check_answers")
    public String checkAnswers(@RequestParam Map<String, String> formParams, Model model){

        // El mapa 'formParams' contendrá todos los campos del formulario.
        // Los checkboxes marcados aparecerán con su 'name' (ej. "q1_answers")
        // y su 'value' (ej. "5,6" si se marcaron las respuestas 5 y 6).

        System.out.println("Formulario de respuestas recibido. Parámetros:");
        formParams.forEach((key, value) -> {
            // Solo procesamos los parámetros que representan las respuestas
            if (key.endsWith("_answers")) {
                // key será "q{ID}_answers" y value será una cadena de IDs de respuestas (ej. "5,6")
                System.out.println("Pregunta " + key + ": Respuestas marcadas: " + value);

                // Aquí se llama a la lógica de servicio para comprobar las respuestas
                // Ejemplo: questionsService.checkAnswer(key, value);
            }
        });

        model.addAttribute("message", "Respuestas comprobadas con éxito.");

        return "check-answers";
    }


}
