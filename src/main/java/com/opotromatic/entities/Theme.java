package com.opotromatic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"block", "questions"})
@NoArgsConstructor
public class Theme {

    public Theme(String name, Block block, String description){
        this.name = name;
        this.block = block;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

}
