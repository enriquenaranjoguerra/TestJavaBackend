package com.opotromatic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(exclude = {"theme", "qaMappings"})
@NoArgsConstructor
public class Question {

    public Question(String name, Theme theme){
        this.name = name;
        this.theme = theme;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @OneToMany(mappedBy = "question")
    @BatchSize(size = 10)
    private List<QaMapping> qaMappings;

    public List<Answer> getAnswers(){
        return qaMappings.stream().map(QaMapping::getAnswer).collect(Collectors.toList());
    }
}