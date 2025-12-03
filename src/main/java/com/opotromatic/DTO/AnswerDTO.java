package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private String name;
    private String explanation;
    private Long questionId;
    private boolean correct;
}
