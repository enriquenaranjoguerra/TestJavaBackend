package com.opotromatic.controllers;

import com.opotromatic.DTO.*;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import com.opotromatic.services.ControllerUtils;
import com.opotromatic.services.QaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manage")
public class ManageController {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QaService qaService;
    private final ControllerUtils controllerUtils;

    public ManageController(
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


    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(controllerUtils.nonExistingElementMessage("category"));
    }

    private Block findBlockById(Long id) {
        return blockRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("block"));
    }

    private Theme findThemeById(Long id) {
        return themeRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("theme"));
    }

    @PostMapping("/question/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody QuestionDTO questionData) {
        String questionName = questionData.getName();
        Theme theme = findThemeById(questionData.getThemeId());

        if (questionRepository.findByNameAndTheme(questionName, theme).isEmpty()) {
            return questionRepository.save(new Question(questionName, theme));
        } else {
            return new Question();
        }
    }

    @PostMapping("/answer/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Answer createAnswer(@RequestBody AnswerDTO answerData) {
        String answerName = answerData.getName();
        Theme theme = findThemeById(answerData.getThemeId());

        if (answerRepository.findByName(answerData.getName()).isEmpty()) {
            return answerRepository.save(new Answer(answerName, theme, answerData.getExplanation()));
        } else {
            return new Answer();
        }
    }

    @PostMapping("/block/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Block createBlock(@RequestBody BlockDTO blockData) {
        String blockName = blockData.getName();

        Category category = findCategoryById(blockData.getCategoryId());
        if (blockRepository.findByNameAndCategory(blockName, category).isEmpty()) {
            return blockRepository.save(new Block(blockName, category, blockData.getDescription()));
        } else {
            return new Block();
        }
    }

    @PostMapping("/theme/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Theme createTheme(@RequestBody ThemeDTO themeData) {
        String themeName = themeData.getName();

        Block block = findBlockById(themeData.getBlockId());
        if (themeRepository.findByNameAndBlock(themeName, block).isEmpty()) {
            return themeRepository.save(new Theme(themeName, block, themeData.getDescription()));
        } else {
            return new Theme();
        }
    }

    @PostMapping("/category/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        if (categoryRepository.findByName(category.getName()).isEmpty()) {
            return categoryRepository.save(category);
        } else {
            return new Category();
        }
    }

    @ResponseBody
    @PostMapping("/several_answer/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Answer> createSeveralAnswer(@RequestBody List<AnswerDTO> answers) {
        return answers.stream().map(this::createAnswer).toList();
    }

    @ResponseBody
    @PostMapping("/qa_mapping/create")
    @ResponseStatus(HttpStatus.CREATED)
    public QaMapping createQaMapping(@RequestBody QaMappingDTO qaMappingData) {
        Question question = questionRepository.findById(qaMappingData.getQuestionId()).orElseThrow(controllerUtils.nonExistingElementMessage("question"));
        Answer answer = answerRepository.findById(qaMappingData.getAnswerId()).orElseThrow(controllerUtils.nonExistingElementMessage("answer"));
        return qaService.saveMapping(question, answer, qaMappingData.isCorrect());
    }

    @ResponseBody
    @PostMapping("/several_qa_mapping/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<QaMapping> createSeveralQaMapping(@RequestBody List<QaMappingDTO> qaMappingSeveralData) {
        return qaMappingSeveralData.stream().map(this::createQaMapping).toList();
    }
}
