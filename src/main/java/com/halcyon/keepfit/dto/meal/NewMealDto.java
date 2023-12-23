package com.halcyon.keepfit.dto.meal;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewMealDto {
    @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.")
    private String title;

    @Size(max = 64, message = "Description must be less than 64 characters.")
    private String description;

    @Size(max = 64, message = "Recipe must be less than 64 characters.")
    private String recipe;

    private Long dietId;

    private int carbohydrates;

    private int fats;

    private int proteins;
}
