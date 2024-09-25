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
public class Question {

    public Question(String name, long categoryId, long themeId, boolean correct, String explanation){
        this.name = name;
        this.categoryId = categoryId;
        this.themeId = themeId;
        this.correct = correct;
        this.description = explanation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Column(nullable = false)
    private long categoryId;


    @ManyToOne
    @JoinColumn(name = "theme_id")
    @Column(nullable = false)
    private long themeId;

    @Column(nullable = false)
    private boolean correct;

    @Column(nullable = true)
    private String description;
}