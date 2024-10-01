package com.opotromatic.controller;

import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.repositories.CategoryRepository;
import com.opotromatic.repositories.QuestionRepository;
import com.opotromatic.entities.Theme;
import com.opotromatic.repositories.ThemeRepository;
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


    @GetMapping("/question/get_by_category/{categoryId}")
    public List<Question> findByCategory(@PathVariable long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(RuntimeException::new);
        return questionRepository.findByCategory(category);
    }


    private Category findCategoryByName(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That category does not exists");
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
    public Question createQuestion(@RequestBody Map<String, Object> questionData) {
        String questionName = (String) questionData.get("name");
        String categoryName = (String) questionData.get("category");
        String themeName = (String) questionData.get("theme");
        Boolean correct = (Boolean) questionData.get("correct");
        String explanation = (String) questionData.get("explanation");

        Category category = findCategoryByName(categoryName);
        Theme theme = findThemeByNameAndCategory(themeName, category);

        if(questionRepository.findByNameAndCategoryAndTheme(questionName, category, theme).isEmpty()){
            return questionRepository.save(new Question(questionName, category, theme, correct, explanation));
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

}
