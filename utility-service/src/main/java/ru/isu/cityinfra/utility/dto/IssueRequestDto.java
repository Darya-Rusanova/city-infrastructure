package ru.isu.cityinfra.utility.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.isu.cityinfra.utility.enums.IssueCategory;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class IssueRequestDto {
    @NotNull(message = "Выберите категорию")
    private IssueCategory category;

    @NotNull(message = "Опишите проблему")
    private String description;

    @NotNull(message = "Укажите адрес")
    @Size(max = 200,message = "Адрес слишком длинный")
    private String address;
}
