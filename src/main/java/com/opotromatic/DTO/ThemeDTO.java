package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDTO {
    private String name;
    private Long blockId;
    private String description;
}
