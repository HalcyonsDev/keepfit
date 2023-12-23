package com.halcyon.keepfit.dto.goal;

import com.halcyon.keepfit.model.Complexity;
import com.halcyon.keepfit.model.Status;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewGoalDto {
    @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.")
    private String title;

    @Size(max = 64, message = "Description must be less than 64 characters.")
    private String description;

    private Status status;

    private Complexity complexity;

    private Instant deadline;
}
