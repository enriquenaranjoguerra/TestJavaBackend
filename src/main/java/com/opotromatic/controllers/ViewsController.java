package com.opotromatic.controllers;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Theme;
import com.opotromatic.repositories.*;
import com.opotromatic.services.ControllerUtils;
import com.opotromatic.services.QaService;
import com.opotromatic.services.QuestionsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/questions")

public class ViewsController {

    private final QuestionsService questionsService;

    public ViewsController(
            QuestionsService questionsService) {
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

        return "listing";
    }
}
