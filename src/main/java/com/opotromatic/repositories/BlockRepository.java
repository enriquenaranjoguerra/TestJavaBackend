package com.opotromatic.repositories;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    List<Block> findByCategory(Category category);
    List<Block> findByCategoryIdIn(List<Long> categoryIds);

    Optional<Block> findByName(String name);

    Optional<Block> findByNameAndCategory(String name, Category category);

}
