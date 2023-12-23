package com.halcyon.keepfit.dto.diet;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewDietDto {
    @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.")
    private String title;

    @Size(max = 64, message = "Description must be less than 64 characters.")
    private String description;
}
