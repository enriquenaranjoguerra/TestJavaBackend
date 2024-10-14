package com.opotromatic.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QaMappingDTO {
    private Long questionId;
    private Long answerId;
    private boolean correct;
}
