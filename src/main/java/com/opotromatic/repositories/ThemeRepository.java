package com.opotromatic.repositories;

import com.opotromatic.entities.Block;
import com.opotromatic.entities.Category;
import com.opotromatic.entities.Question;
import com.opotromatic.entities.Theme;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    @EntityGraph(attributePaths = {"questions", "block", "block.category"})
    Optional<Theme> findById(long id);
    List<Theme> findByBlockIdIn(Set<Long> blockIds);
    List<Theme> findByBlock(Block block);
    Optional<Theme> findByNameAndBlock(String name, Block block);
}
