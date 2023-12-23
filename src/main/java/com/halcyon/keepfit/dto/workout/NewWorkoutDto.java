package com.halcyon.keepfit.dto.workout;

import com.halcyon.keepfit.model.Complexity;
import com.halcyon.keepfit.model.Status;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewWorkoutDto {
    @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.")
    private String title;

    @Size(max = 64, message = "Description must be less than 64 characters.")
    private String description;

    private Status status;

    private Complexity complexity;
}
