package com.opotromatic.services;

import com.opotromatic.DTO.CheckAnswerDTO;
import com.opotromatic.entities.*;
import com.opotromatic.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionsService {

    private final CategoryRepository categoryRepository;
    private final ThemeRepository themeRepository;
    private final BlockRepository blockRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ControllerUtils controllerUtils;

    public QuestionsService(
            CategoryRepository categoryRepository,
            ThemeRepository themeRepository,
            BlockRepository blockRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            ControllerUtils controllerUtils) {
        this.categoryRepository = categoryRepository;
        this.blockRepository = blockRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.controllerUtils = controllerUtils;
    }

    @Transactional(readOnly = true)
    public List<Question> getQuestionsByIds(List<Long> categoryIds, List<Long> blockIds, List<Long> themeIds){
        if(categoryIds.isEmpty() && blockIds.isEmpty() && themeIds.isEmpty()){
            throw new RuntimeException("No category, blocks or themes selected");
        }

        Set<Long> allBlocksIds = new HashSet<>(blockIds);
        Set<Long> allThemeIds = new HashSet<>(themeIds);
        List<Question> questions;

        if(!categoryIds.isEmpty()){
            List<Long> blocks = blockRepository.findByCategoryIdIn(categoryIds).stream().map(Block::getId).toList();
            blocks.forEach(System.out::println);
            allBlocksIds.addAll(blocks);
        }

        if(!themeIds.isEmpty()){
            List<Long> themes = themeRepository.findByBlockIdIn(allBlocksIds).stream().map(Theme::getId).toList();
            themes.forEach(System.out::println);
            allThemeIds.addAll(themes);
        }

        if(allThemeIds.isEmpty()){
            throw new RuntimeException("No questions found");
        }

        questions = questionRepository.findByThemeIdIn(allThemeIds);

        return questions;
    }


    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Block> getBlocksByCategory(@PathVariable Long categoryId) {
        Category category = controllerUtils.findCategoryById(categoryId);
        return blockRepository.findByCategory(category);

    }

    public List<Theme> getThemesByBlock(@PathVariable Long blockId) {
        Block block = controllerUtils.findBlockById(blockId);
        return themeRepository.findByBlock(block);
    }

    public List<Question> findByTheme(@PathVariable Long themeId) {
        Theme theme = controllerUtils.findThemeById(themeId);
        return questionRepository.findByTheme(theme);
    }

    public List<Answer> findByQuestion(@PathVariable Long questionId) {
        Question question = questionRepository.findWithAnswersById(questionId).orElseThrow(controllerUtils.nonExistingElementMessage("question"));
        return question.getAnswers();
    }

    public Boolean checkAnswer(@RequestBody CheckAnswerDTO checkAnswerData) {
        Answer answer = answerRepository.findById(checkAnswerData.getAnswerId()).orElseThrow(controllerUtils.nonExistingElementMessage("answer"));
        return answer.isCorrect() == checkAnswerData.isMarked();
    }

    public List<Boolean> checkAnswer(List<CheckAnswerDTO> checkAnswerSeveralData) {
        return checkAnswerSeveralData.stream().map(this::checkAnswer).toList();
    }

}
