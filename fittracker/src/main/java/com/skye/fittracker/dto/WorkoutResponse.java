package com.skye.fittracker.dto;

import java.time.LocalDateTime;

public record WorkoutResponse(
        Long id,
        String exerciseName,
        String muscleGroup,
        Double weight,
        Integer reps,
        Integer sets,
        LocalDateTime workoutDate
) {}