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
}