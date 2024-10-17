package com.opotromatic.repositories;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends CrudRepository<Long, Block> {

    Optional<Block> findById(Long id);

    List<Block> findByCategory(Category category);

    Optional<Block> findByName(String name);

    Optional<Block> findByNameAndCategory(String name, Category category);

}
