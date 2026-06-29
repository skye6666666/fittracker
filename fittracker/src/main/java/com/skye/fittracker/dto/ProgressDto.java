package com.skye.fittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ProgressDto {

    private LocalDate date;

    private Double weight;

    private Double estimatedOneRm;

    private Double volume;

}