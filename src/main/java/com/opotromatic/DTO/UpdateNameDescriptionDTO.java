package com.opotromatic.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateNameDescriptionDTO {
    private Long id;
    private String name;
    private String description;
}
