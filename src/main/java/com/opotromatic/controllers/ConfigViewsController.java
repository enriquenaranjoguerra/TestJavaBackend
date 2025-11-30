package com.opotromatic.controllers;

import com.opotromatic.DTO.AnswerUpdateDTO;
import com.opotromatic.DTO.AnswerListUpdateDTO;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import com.opotromatic.services.ControllerUtils;
import com.opotromatic.services.QuestionsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/manage")
public class ConfigViewsController {

    // Repositorios y Servicios necesarios para cargar datos y vistas
    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final ControllerUtils controllerUtils;
    private final QuestionsService questionsService;

    public ConfigViewsController(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository,
            QuestionRepository questionRepository,
            ControllerUtils controllerUtils,
            QuestionsService questionsService) {
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
        this.controllerUtils = controllerUtils;
        this.questionsService = questionsService;
    }

    private void addCommonAttributes(Model model) {
        model.addAttribute("homeUrl", "/");
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(controllerUtils.nonExistingElementMessage("category"));
    }
    private Block findBlockById(Long id) {
        return blockRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("block"));
    }
    private Theme findThemeById(Long id) {
        return themeRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("theme"));
    }
    private Question findQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("Question"));
    }
    // ---------------------------------------------------------------------------------

    /**
     * Muestra la configuración principal (listado de categorías).
     */
    @GetMapping("/config")
    public String config(Model model) {
        List<Category> categories = questionsService.getAllCategories();
        addCommonAttributes(model);
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Category());
        return "manage/config-categories";
    }

    /**
     * Muestra los bloques de una categoría específica.
     */
    @GetMapping("/category/{categoryId}")
    public String listBlock(@PathVariable Long categoryId, Model model) {
        Category category = findCategoryById(categoryId);
        addCommonAttributes(model);
        model.addAttribute("category", category);
        model.addAttribute("blocks", category.getBlocks());
        model.addAttribute("newBlock", new Block());
        return "manage/config-blocks";
    }

    /**
     * Muestra los temas de un bloque específico.
     */
    @Transactional(readOnly = true)
    @GetMapping("/block/{blockId}")
    public String listTheme(@PathVariable Long blockId, Model model) {
        Block block = findBlockById(blockId);
        addCommonAttributes(model);
        model.addAttribute("block", block);
        model.addAttribute("themes", block.getThemes());
        model.addAttribute("newTheme", new Theme());
        return "manage/config-themes";
    }

    /**
     * Muestra las preguntas de un tema específico.
     */
    @Transactional(readOnly = true)
    @GetMapping("/theme/{themeId}")
    public String listQuestions(@PathVariable Long themeId, Model model) {
        Theme theme = findThemeById(themeId);
        addCommonAttributes(model);
        model.addAttribute("theme", theme);
        model.addAttribute("questions", theme.getQuestions());

        return "manage/config-questions";
    }

    /**
     * Muestra las respuestas de una pregunta específica y prepara el DTO
     * para la edición masiva.
     */
    @Transactional(readOnly = true)
    @GetMapping("/question/{questionId}")
    public String listAnswers(@PathVariable Long questionId, Model model) {
        Question question = findQuestionById(questionId);

        List<AnswerUpdateDTO> answersToUpdate = question.getAnswers().stream()
                .map(a -> {
                    AnswerUpdateDTO dto = new AnswerUpdateDTO();
                    dto.setId(a.getId());
                    dto.setCorrect(a.isCorrect());
                    return dto;
                })
                .collect(Collectors.toList());

        AnswerListUpdateDTO answerListUpdateDTO = new AnswerListUpdateDTO();
        answerListUpdateDTO.setAnswers(answersToUpdate);

        addCommonAttributes(model);
        model.addAttribute("question", question);
        model.addAttribute("answersForm", answerListUpdateDTO);
        model.addAttribute("answers", question.getAnswers());

        return "manage/config-answers";
    }
}