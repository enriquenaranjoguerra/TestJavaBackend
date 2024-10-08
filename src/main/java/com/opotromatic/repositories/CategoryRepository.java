package com.opotromatic.repositories;

import com.opotromatic.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Optional<Category> findById(long id);
}
