package com.opotromatic.controller;

import com.opotromatic.DTO.*;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import com.opotromatic.services.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Supplier;

@Controller
@RequestMapping("/api/questions")

public class QuestionsController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QaService qaService;


    private Supplier<ResponseStatusException> nonExistingElementMessage(String element) {
        return () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("That %s does not exists", element));
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


    @GetMapping("/category/get_all")
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/theme/get_by_block_id/{blockId}")
    public Iterable<Theme> getThemesByBlock(@RequestParam Long blockId) {
        Block block = findBlockById(blockId);
        return themeRepository.findByBlock(block);
    }

    @GetMapping("/question/get_by_theme/{themeId}")
    public List<Question> findByTheme(@PathVariable Long themeId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(nonExistingElementMessage("theme"));
        return questionRepository.findByTheme(theme);
    }

    @GetMapping("/answer/get_by_question_id/{questionId}")
    public List<Answer> findByQuestion(@PathVariable Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(nonExistingElementMessage("question"));
        // TODO review this
        return qaService.getAnswersForQuestion(question);
    }

    @GetMapping("/answer/check_answer")
    public Boolean checkAnswer(CheckAnswerDTO checkAnswerData) {
        Question question = questionRepository.findById(checkAnswerData.getQuestionId()).orElseThrow(nonExistingElementMessage("question"));
        Answer answer = answerRepository.findById(checkAnswerData.getAnswerId()).orElseThrow(nonExistingElementMessage("answer"));
        return qaService.findByQuestionAndAnswer(question, answer).isCorrect() == checkAnswerData.isMarked();
    }

    @GetMapping("/answer/check_several_answer")
    public List<Boolean> checkAnswer(List<CheckAnswerDTO> checkAnswerSeveralData) {
        return checkAnswerSeveralData.stream().map(this::checkAnswer).toList();
    }


    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(nonExistingElementMessage("category"));
    }

    private Block findBlockById(Long id) {
        return blockRepository.findById(id).orElseThrow(nonExistingElementMessage("block"));
    }

    private Theme findThemeById(Long id) {
        return themeRepository.findById(id).orElseThrow(nonExistingElementMessage("theme"));
    }

    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
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

    @ResponseBody
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
        Question question = questionRepository.findById(qaMappingData.getQuestionId()).orElseThrow(nonExistingElementMessage("question"));
        Answer answer = answerRepository.findById(qaMappingData.getAnswerId()).orElseThrow(nonExistingElementMessage("answer"));
        return qaService.saveMapping(question, answer, qaMappingData.isCorrect());
    }

    @ResponseBody
    @PostMapping("/several_qa_mapping/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<QaMapping> createSeveralQaMapping(@RequestBody List<QaMappingDTO> qaMappingSeveralData) {
        return qaMappingSeveralData.stream().map(this::createQaMapping).toList();
    }

}
