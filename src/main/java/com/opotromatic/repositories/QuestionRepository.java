package com.opotromatic.repositories;

import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByCategory(Category category);
    List<Question> findByTheme(Theme theme);
    List<Question> findByNameAndCategoryAndTheme(String name, Category category, Theme theme);
}
