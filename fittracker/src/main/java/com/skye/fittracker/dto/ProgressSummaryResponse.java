package com.skye.fittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProgressSummaryResponse {

    private Double currentWeight;

    private Double bestWeight;

    private Double currentOneRm;

    private Double bestOneRm;

    private LocalDate bestWeightDate;

    private LocalDate currentWorkoutDate;

    private LocalDate bestOneRmDate;
}