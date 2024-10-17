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
public class Theme {

    public Theme(String name, Block block, String description){
        this.name = name;
        this.block = block;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block block;

    @Column(nullable = true)
    private String description;
}
