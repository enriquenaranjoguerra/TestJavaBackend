package com.opotromatic.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AnswerListUpdateDTO {
    private List<AnswerUpdateDTO> answers;
}