package com.opotromatic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class QaMapping {

    public QaMapping(Question question, Answer answer, boolean correct){
        this.question = question;
        this.answer = answer;
        this.correct = correct;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "answer id", nullable = false)
    private Answer answer;

    @Column(nullable = false)
    private boolean correct;
}
