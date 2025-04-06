package com.opotromatic.services;

import com.opotromatic.DTO.CheckAnswerDTO;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import com.opotromatic.services.ControllerUtils;
import com.opotromatic.services.QaService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/questions")

public class QuestionsService {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QaService qaService;
    private final ControllerUtils controllerUtils;

    public QuestionsService(
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

    @GetMapping("questions/by_ids")
    public List<Question> getQuestionsByIds(@RequestParam List<Long> categoryIds, @RequestParam List<Long> blockIds, @RequestParam List<Long> themeIds){
        List<Long> allThemeIds = new ArrayList<>(themeIds);
        List<Long> allBlocksIds = new ArrayList<>(blockIds);
        List<Question> questions = new ArrayList<>();

        if(!categoryIds.isEmpty()){
            allBlocksIds.addAll(blockRepository.findByCategoryIdIn(categoryIds).stream().map(Block::getId).toList());
        }

        if(!themeIds.isEmpty()){
            allThemeIds.addAll(themeRepository.findByBlockIdIn(allBlocksIds).stream().map(Theme::getId).toList());
        }

        if(allThemeIds.isEmpty()){
            throw new RuntimeException("No questions found");
        }

        return questions;
    }


    @GetMapping("/category/get_all")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/block/get_by_category_id/{blockId}")
    public List<Block> getBlocksByCategory(@PathVariable Long categoryId) {
        Category category = controllerUtils.findCategoryById(categoryId);
        return blockRepository.findByCategory(category);

    }

    @GetMapping("/theme/get_by_block_id/{blockId}")
    public List<Theme> getThemesByBlock(@PathVariable Long blockId) {
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
        Question question = questionRepository.findWithAnswersById(questionId).orElseThrow(controllerUtils.nonExistingElementMessage("question"));
        return question.getAnswers();
    }

    @PostMapping("/answer/check_answer")
    public Boolean checkAnswer(@RequestBody CheckAnswerDTO checkAnswerData) {
        Question question = questionRepository.findById(checkAnswerData.getQuestionId()).orElseThrow(controllerUtils.nonExistingElementMessage("question"));
        Answer answer = answerRepository.findById(checkAnswerData.getAnswerId()).orElseThrow(controllerUtils.nonExistingElementMessage("answer"));
        return qaService.findByQuestionAndAnswer(question, answer).isCorrect() == checkAnswerData.isMarked();
    }

    @GetMapping("/answer/check_several_answer")
    public List<Boolean> checkAnswer(List<CheckAnswerDTO> checkAnswerSeveralData) {
        return checkAnswerSeveralData.stream().map(this::checkAnswer).toList();
    }


}
