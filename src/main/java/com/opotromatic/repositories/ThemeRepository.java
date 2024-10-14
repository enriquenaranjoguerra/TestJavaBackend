package com.opotromatic.repositories;

import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends CrudRepository<Theme, Long> {
    List<Theme> findByCategory(Category category);
    Optional<Theme> findByName(String name);
    Optional<Theme> findByNameAndCategory(String name, Category category);
    Optional<Theme> findByIdAndCategory(Long id, Category category);

}
