package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {
    private String name;
    private Long categoryId;
    private String description;
}
