package com.opotromatic.repositories;

import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByCategory(Category category);
    List<Question> findByTheme(Theme theme);

    Optional<Question> findByName(String name);
    List<Question> findByNameAndTheme(String name, Theme theme);
}
