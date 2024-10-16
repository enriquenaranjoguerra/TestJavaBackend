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
public class Answer {

    public Answer(String name, Theme theme, String explanation) {
        this.name = name;
        this.theme = theme;
        this.explanation = explanation;
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

    @Column(nullable = true)
    private String explanation;
}