package ru.isu.cityinfra.frontend.dto;

import lombok.*;
import ru.isu.cityinfra.frontend.enums.IssueCategory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequestDto {
    private IssueCategory category;
    private String description;
    private String address;
}
