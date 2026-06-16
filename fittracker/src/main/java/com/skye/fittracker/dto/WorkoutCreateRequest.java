package com.skye.fittracker.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkoutCreateRequest {
    private Long exerciseId;
    private Double weight;
    private Integer reps;
    private Integer sets;
    private LocalDate workoutDate;
}