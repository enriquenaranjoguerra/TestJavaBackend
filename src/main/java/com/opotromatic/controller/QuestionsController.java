package com.opotromatic.controller;

import com.example.Test2.Book;
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


    @GetMapping("/question/category/{categoryId}")
    public List<Question> findByCategory(@PathVariable long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(RuntimeException::new);
        return questionRepository.findByCategory(category);
    }

    @PostMapping("/create/question")
    @ResponseStatus(HttpStatus.CREATED)
    public Question createQuestion(@RequestBody Question question) {
        if(questionRepository.findByName(question.getName()).isEmpty()){
            return questionRepository.save(question);
        } else {
            return new Question();
        }
    }

    @PostMapping("/create/theme")
    @ResponseStatus(HttpStatus.CREATED)
    public Theme createTheme(@RequestBody Map<String, String> themeData) {
        String themeName = themeData.get("name");
        String categoryName = themeData.get("category");
        String description = themeData.get("description");

        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);

        if(categoryOptional.isPresent()){
            if(themeRepository.findByName(themeName).isEmpty()){
                return themeRepository.save(new Theme(themeName, categoryOptional.get(), description));
            } else {
                return new Theme();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "That category does not exists");
        }


    }

    @PostMapping("/create/category")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        if(categoryRepository.findByName(category.getName()).isEmpty()){
            return categoryRepository.save(category);
        } else {
            return new Category();
        }
    }

}
