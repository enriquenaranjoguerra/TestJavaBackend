package com.opotromatic.services;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import com.opotromatic.repositories.BlockRepository;
import com.opotromatic.repositories.CategoryRepository;
import com.opotromatic.repositories.QuestionRepository;
import com.opotromatic.repositories.ThemeRepository;
import org.springframework.stereotype.Service;

@Service
public class FinderService {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final ControllerUtils controllerUtils;

    public FinderService(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository,
            QuestionRepository questionRepository,
            ControllerUtils controllerUtils){
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
        this.controllerUtils = controllerUtils;
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(controllerUtils.nonExistingElementMessage("category"));
    }
    public Block findBlockById(Long id) {
        return blockRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("block"));
    }
    public Theme findThemeById(Long id) {
        return themeRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("theme"));
    }
    public Question findQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(controllerUtils.nonExistingElementMessage("Question"));
    }
}
