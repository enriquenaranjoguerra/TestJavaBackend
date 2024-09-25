package com.opotromatic.controller;

import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.QuestionRepository;
import com.opotromatic.entities.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")

public class QuestionsController {

    @Autowired
    private Category category;
    @Autowired
    private Theme theme;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/question/{name}")
    public List<Question> findByName(@PathVariable String name) {
        return questionRepository.findByName(name);
    }


}
