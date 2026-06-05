package com.skye.fittracker.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkoutCreateRequest {
    private Long exerciseId;
    private Double weight;
    private Integer reps;
    private Integer sets;
    //private LocalDateTime workoutDate;
}