package com.opotromatic.repositories;

import com.opotromatic.entities.QaMapping;
import com.opotromatic.entities.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QaMappingRepository extends CrudRepository<QaMapping, Long> {
    List<QaMapping> findByQuestion(Question question);
}
