package com.skye.fittracker.dto;

import lombok.Data;

@Data
public class ExerciseCreateRequest {

    private String name;

    private String muscleGroup;

    private String description;
}