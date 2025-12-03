package com.opotromatic.repositories;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @EntityGraph(attributePaths = {"answers", "theme", "theme.block", "theme.block.category"})
    Optional<Question> findById(long id);
    @EntityGraph(attributePaths = {"answers"})
    List<Question> findByThemeIdIn(Set<Long> themeIds);
    List<Question> findByTheme(Theme theme);

    Optional<Question> findByName(String name);

    Optional<Question> findWithAnswersById(Long id);
    List<Question> findByNameAndTheme(String name, Theme theme);

}
