package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private String name;
    private Long themeId;
}
