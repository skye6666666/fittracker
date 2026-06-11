package com.skye.fittracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExerciseCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String muscleGroup;

    @NotBlank
    private String description;
}