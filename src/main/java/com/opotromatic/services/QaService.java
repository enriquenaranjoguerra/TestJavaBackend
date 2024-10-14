package com.opotromatic.services;

import com.opotromatic.entities.Answer;
import com.opotromatic.entities.QaMapping;
import com.opotromatic.entities.Question;
import com.opotromatic.repositories.QaMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QaService {
    @Autowired
    private QaMappingRepository qaMappingRepository;

    public List<Answer> getAnswersForQuestion(Question question){
        List<QaMapping> mappings = qaMappingRepository.findByQuestion(question);
        return mappings.stream().map(QaMapping::getAnswer).collect(Collectors.toList());
    }

    public QaMapping findByQuestionAndAnswer(Question question, Answer answer){
        if(qaMappingRepository.findByQuestionAndAnswer(question, answer).isPresent()){
            return qaMappingRepository.findByQuestionAndAnswer(question, answer).get();
        } else {
            return new QaMapping();
        }

    }

    public QaMapping saveMapping(Question question, Answer answer, boolean correct){
        if(qaMappingRepository.findByQuestionAndAnswer(question, answer).isPresent()){
            return new QaMapping();
        } else {
            return qaMappingRepository.save(new QaMapping(question, answer, correct));
        }
    }
}
