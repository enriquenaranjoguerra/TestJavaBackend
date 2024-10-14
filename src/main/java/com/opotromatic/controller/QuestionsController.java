package com.opotromatic.controller;

import com.opotromatic.DTO.AnswerDTO;
import com.opotromatic.DTO.QaMappingDTO;
import com.opotromatic.DTO.QuestionDTO;
import com.opotromatic.DTO.ThemeDTO;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.AnswerRepository;
import com.opotromatic.repositories.CategoryRepository;
import com.opotromatic.repositories.QuestionRepository;
import com.opotromatic.repositories.ThemeRepository;
import com.opotromatic.services.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questions")

public class QuestionsController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QaService qaService;

    private String nonExistingElementMessage(String element) {
        return String.format("That %s does not exists", element);
    }


    @GetMapping("/question/get_by_category/{categoryId}")
    public List<Question> findByCategory(@PathVariable Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("category")));
        return questionRepository.findByCategory(category);
    }

    @GetMapping("/question/get_by_theme/{themeId}")
    public List<Question> findByTheme(@PathVariable Long themeId) {
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("theme")));
        return questionRepository.findByTheme(theme);
    }

    @GetMapping("/question/get_answers/{questionId}")
    public List<Answer> findByQuestion(@PathVariable Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("question")));
        return qaService.getAnswersForQuestion(question);
    }


    private Category findCategoryById(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("category"));
        }
    }

    private Theme findThemeById(Long id) {
        Optional<Theme> themeOptional = themeRepository.findById(id);
        if (themeOptional.isPresent()) {
            return themeOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("Theme"));
        }
    }

    @PostMapping("/question/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody QuestionDTO questionData) {
        String questionName = questionData.getName();
        Category category = findCategoryById(questionData.getCategoryId());
        Theme theme = findThemeById(questionData.getThemeId());

        if (questionRepository.findByNameAndCategoryAndTheme(questionName, category, theme).isEmpty()) {
            return questionRepository.save(new Question(questionName, category, theme));
        } else {
            return new Question();
        }
    }

    @PostMapping("/answer/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Answer createAnswer(@RequestBody AnswerDTO answerData) {
        String answerName = answerData.getName();
        Category category = findCategoryById(answerData.getCategoryId());
        Theme theme = findThemeById(answerData.getThemeId());

        if (answerRepository.findByName(answerData.getName()).isEmpty()) {
            return answerRepository.save(new Answer(answerName, category, theme, answerData.getExplanation()));
        } else {
            return new Answer();
        }
    }

    @PostMapping("/theme/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Theme createTheme(@RequestBody ThemeDTO themeData) {
        String themeName = themeData.getName();

        Category category = findCategoryById(themeData.getCategoryId());
        if (themeRepository.findByNameAndCategory(themeName, category).isEmpty()) {
            return themeRepository.save(new Theme(themeName, category, themeData.getDescription()));
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

    @PostMapping("/several_answer/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Answer> createSeveralAnswer(@RequestBody List<AnswerDTO> answers) {
        return answers.stream().map(this::createAnswer).toList();
    }

    @PostMapping("/qa_mapping/create")
    @ResponseStatus(HttpStatus.CREATED)
    public QaMapping createQaMapping(@RequestBody QaMappingDTO qaMappingData) {
        Question question = questionRepository.findById(qaMappingData.getQuestionId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("question")));
        Answer answer = answerRepository.findById(qaMappingData.getAnswerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("answer")));
        return qaService.saveMapping(question, answer, qaMappingData.isCorrect());
    }

    @PostMapping("/several_qa_mapping/create")
    @ResponseStatus(HttpStatus.CREATED)
    public List<QaMapping> createSeveralQaMapping(@RequestBody List<QaMappingDTO> qaMappingSeveralData) {
        return qaMappingSeveralData.stream().map(this::createQaMapping).toList();
    }

}
