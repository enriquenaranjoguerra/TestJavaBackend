package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDTO {
    private String name;
    private Long categoryId;
    private String description;
}
