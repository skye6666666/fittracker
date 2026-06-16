package com.skye.fittracker.dto;

import java.time.LocalDate;

public record WorkoutResponse(
        Long id,
        String exerciseName,
        String muscleGroup,
        Double weight,
        Integer reps,
        Integer sets,
        LocalDate workoutDate
) {}