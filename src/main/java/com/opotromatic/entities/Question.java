package com.opotromatic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"theme", "answers"})
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;
    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @BatchSize(size = 10)
    private List<Answer> answers;

    public Question(String name, Theme theme) {
        this.name = name;
        this.theme = theme;
    }

    public List<Answer> getLimitedAnswers(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 4;
        }

        List<Answer> allAnswers = new ArrayList<>(this.answers);

        Collections.shuffle(allAnswers);

        int maxAnswers = Math.min(limit, allAnswers.size());

        return allAnswers.subList(0, maxAnswers);
    }
}