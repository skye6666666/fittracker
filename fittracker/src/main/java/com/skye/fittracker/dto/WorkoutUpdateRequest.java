package com.skye.fittracker.dto;

import lombok.Data;

@Data
public class WorkoutUpdateRequest {

    private Double weight;

    private Integer reps;

    private Integer sets;

}