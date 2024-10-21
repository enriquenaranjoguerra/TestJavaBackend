package com.opotromatic.controllers;

import com.opotromatic.DTO.CheckAnswerDTO;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import com.opotromatic.services.ControllerUtils;
import com.opotromatic.services.QaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")

public class QuestionsController {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QaService qaService;
    private final ControllerUtils controllerUtils;

    public QuestionsController(
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


    @GetMapping("/category/get_all")
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/block/get_by_category_id/{blockId}")
    public Iterable<Block> getBlocksByCategory(@RequestParam Long categoryId) {
        Category category = controllerUtils.findCategoryById(categoryId);
        return blockRepository.findByCategory(category);

    }

    @GetMapping("/theme/get_by_block_id/{blockId}")
    public Iterable<Theme> getThemesByBlock(@RequestParam Long blockId) {
        Block block = controllerUtils.findBlockById(blockId);
        return themeRepository.findByBlock(block);
    }

    @GetMapping("/question/get_by_theme/{themeId}")
    public List<Question> findByTheme(@PathVariable Long themeId) {
        Theme theme = controllerUtils.findThemeById(themeId);
        return questionRepository.findByTheme(theme);
    }

    @GetMapping("/answer/get_by_question_id/{questionId}")
    public List<Answer> findByQuestion(@PathVariable Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(controllerUtils.nonExistingElementMessage("question"));
        // TODO review this
        return qaService.getAnswersForQuestion(question);
    }

    @GetMapping("/answer/check_answer")
    public Boolean checkAnswer(CheckAnswerDTO checkAnswerData) {
        Question question = questionRepository.findById(checkAnswerData.getQuestionId()).orElseThrow(controllerUtils.nonExistingElementMessage("question"));
        Answer answer = answerRepository.findById(checkAnswerData.getAnswerId()).orElseThrow(controllerUtils.nonExistingElementMessage("answer"));
        return qaService.findByQuestionAndAnswer(question, answer).isCorrect() == checkAnswerData.isMarked();
    }

    @GetMapping("/answer/check_several_answer")
    public List<Boolean> checkAnswer(List<CheckAnswerDTO> checkAnswerSeveralData) {
        return checkAnswerSeveralData.stream().map(this::checkAnswer).toList();
    }


}
