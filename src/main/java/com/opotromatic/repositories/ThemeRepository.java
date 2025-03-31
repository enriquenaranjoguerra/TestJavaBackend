package com.opotromatic.repositories;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findByBlock(Block block);

    Optional<Theme> findByName(String name);

    Optional<Theme> findByNameAndBlock(String name, Block block);

    Optional<Theme> findByIdAndBlock(Long id, Block block);



}
