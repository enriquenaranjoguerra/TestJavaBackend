package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private String name;
    private Long themeId;
    private String explanation;
}
