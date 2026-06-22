package com.skye.fittracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkoutCreateRequest {
    @NotNull
    private Long exerciseId;

    @NotNull
    private Double weight;

    @NotNull
    private Integer reps;

    @NotNull
    private Integer sets;

    @NotNull
    private LocalDate workoutDate;
}