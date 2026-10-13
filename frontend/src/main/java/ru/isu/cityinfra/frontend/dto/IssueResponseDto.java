package ru.isu.cityinfra.frontend.dto;

import lombok.*;
import ru.isu.cityinfra.frontend.enums.IssueCategory;
import ru.isu.cityinfra.frontend.enums.IssueStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponseDto {
    private Integer id;
    private Integer userId;
    private IssueCategory category;
    private String nameCategory;
    private String address;
    private IssueStatus status;
    private String nameStatus;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
