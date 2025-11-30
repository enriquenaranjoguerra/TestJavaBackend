package com.opotromatic.services;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Theme;
import com.opotromatic.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

@Service
public class ControllerUtils {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;

    public ControllerUtils(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository) {
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
    }

    public Supplier<ResponseStatusException> nonExistingElementMessage(String element) {
        return () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("That %s does not exists", element));
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(nonExistingElementMessage("category"));
    }

    public Block findBlockById(Long id) {
        return blockRepository.findById(id).orElseThrow(nonExistingElementMessage("block"));
    }

    public Theme findThemeById(Long id) {
        return themeRepository.findById(id).orElseThrow(nonExistingElementMessage("theme"));
    }
}
