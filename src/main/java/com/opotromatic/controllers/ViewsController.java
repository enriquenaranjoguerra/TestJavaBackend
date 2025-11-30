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

    private void addCommonAttributes(Model model) {
        model.addAttribute("homeUrl", "/");
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

        addCommonAttributes(model);
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

        addCommonAttributes(model);
        model.addAttribute("questions", questions);
        model.addAttribute("message", "Preguntas encontradas: " + questions.size());
        model.addAttribute("answersAmount", answersAmount);

        return "questions-list";
    }

    @PostMapping("/check_answers")
    public String checkAnswers(@RequestParam Map<String, String> formParams, Model model){

//        formParams.forEach((key, value) -> {
//            if (key.endsWith("_answers")) {
//                System.out.println("Pregunta " + key + ": Respuestas marcadas: " + value);
//            }
//        });

        addCommonAttributes(model);
        model.addAttribute("message", "Respuestas comprobadas con éxito.");

        return "check-answers";
    }


}
