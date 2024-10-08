package com.opotromatic.repositories;

import com.opotromatic.entities.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long> {
    public Optional<Answer> findByName(String name);
}
