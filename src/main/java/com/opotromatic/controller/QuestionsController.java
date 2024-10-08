package com.opotromatic.controller;

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
import java.util.Map;
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


    @GetMapping("/question/get_by_category/{categoryName}")
    public List<Question> findByCategory(@PathVariable String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("category")));
        return questionRepository.findByCategory(category);
    }

    @GetMapping("/question/get_by_theme/{themeName}")
    public List<Question> findByTheme(@PathVariable String themeName) {
        Theme theme = themeRepository.findByName(themeName).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("theme")));
        return questionRepository.findByTheme(theme);
    }

    @GetMapping("/question/get_answers/{questionName}")
    public List<Answer> findByQuestion(@PathVariable String questionName) {
        Question question = questionRepository.findByName(questionName).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("question")));
        return qaService.getAnswersForQuestion(question);
    }


    private Category findCategoryByName(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("category"));
        }
    }

    private Theme findThemeByNameAndCategory(String themeName, Category category) {
        Optional<Theme> themeOptional = themeRepository.findByNameAndCategory(themeName, category);
        if (themeOptional.isPresent()) {
            return themeOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That theme does not exists");
        }
    }

    @PostMapping("/question/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody Map<String, String> questionData) {
        String questionName = questionData.get("name");
        String categoryName = questionData.get("category");
        String themeName = questionData.get("theme");

        Category category = findCategoryByName(categoryName);
        Theme theme = findThemeByNameAndCategory(themeName, category);

        if (questionRepository.findByNameAndCategoryAndTheme(questionName, category, theme).isEmpty()) {
            return questionRepository.save(new Question(questionName, category, theme));
        } else {
            return new Question();
        }
    }

    @PostMapping("/theme/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Theme createTheme(@RequestBody Map<String, String> themeData) {
        String themeName = themeData.get("name");
        String categoryName = themeData.get("category");
        String description = themeData.get("description");

        Category category = findCategoryByName(categoryName);
        if (themeRepository.findByNameAndCategory(themeName, category).isEmpty()) {
            return themeRepository.save(new Theme(themeName, category, description));
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

    @PostMapping("/answer/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Answer createAnswer(@RequestBody Answer answer) {
        if (answerRepository.findByName(answer.getName()).isEmpty()) {
            return answerRepository.save(answer);
        } else {
            return new Answer();
        }
    }

    @PostMapping("/qa_mapping/create")
    @ResponseStatus(HttpStatus.CREATED)
    public QaMapping createQaMapping(@RequestBody Map<String, Object> qaMappingData) {
        Long questionId = (Long) qaMappingData.get("questionId");
        Long answerId = (Long) qaMappingData.get("answerId");
        boolean correct = (Boolean) qaMappingData.get("correct");

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("question")));
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, nonExistingElementMessage("answer")));
        return qaService.saveMapping(question, answer, correct);
    }

}
