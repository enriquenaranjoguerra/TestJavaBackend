package com.opotromatic.repositories;

import com.opotromatic.entities.Answer;
import com.opotromatic.entities.QaMapping;
import com.opotromatic.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QaMappingRepository extends JpaRepository<QaMapping, Long> {
    List<QaMapping> findByQuestion(Question question);
    Optional<QaMapping> findByQuestionAndAnswer(Question question, Answer answer);

    List<QaMapping> findByQuestionAndCorrectTrue(Question question);
}
