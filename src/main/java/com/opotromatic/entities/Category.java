package com.opotromatic.entities;

        import jakarta.persistence.*;
        import lombok.*;

        import java.util.ArrayList;
        import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Block> blocks = new ArrayList<>();

    @Column(nullable = true)
    private String description;
}
