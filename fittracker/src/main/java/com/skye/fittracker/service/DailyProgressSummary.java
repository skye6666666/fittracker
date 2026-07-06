package com.skye.fittracker.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyProgressSummary {

    private LocalDate workoutDate;

    private double maxWeight;

    private double bestEstimatedOneRm;

    private double volume;
}