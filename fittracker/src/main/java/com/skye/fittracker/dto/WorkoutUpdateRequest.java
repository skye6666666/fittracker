package com.skye.fittracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkoutUpdateRequest {

    @NotNull
    private Double weight;

    @NotNull
    private Integer reps;

    @NotNull
    private Integer sets;

}