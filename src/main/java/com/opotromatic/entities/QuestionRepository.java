package com.opotromatic.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByTheme(Long themeId);
    List<Question> findByName(String name);
}
