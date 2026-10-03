package ru.isu.cityinfra.frontend.dto;

import lombok.*;
import ru.isu.cityinfra.frontend.enums.IssueStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueStatusUpdateDto {
    private IssueStatus status;
}
