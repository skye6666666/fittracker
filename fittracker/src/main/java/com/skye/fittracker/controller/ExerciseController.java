package com.skye.fittracker.controller;

import com.skye.fittracker.dto.ExerciseCreateRequest;
import com.skye.fittracker.dto.WorkoutResponse;
import com.skye.fittracker.dto.WorkoutUpdateRequest;
import com.skye.fittracker.entity.Exercise;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Exercise createExercise(
            @Valid
            @RequestBody ExerciseCreateRequest request) {


        return exerciseService.createExercise(request);
    }

    @GetMapping
    public List<Exercise> getAllExercises(){
        return exerciseService.getAllExercises();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Exercise updateExercise(
            @PathVariable Long id,
            @Valid
            @RequestBody ExerciseCreateRequest request
    ) {

        return exerciseService.updateExercise(
                id,
                request
        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
    }
}