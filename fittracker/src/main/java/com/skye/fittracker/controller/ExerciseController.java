package com.skye.fittracker.controller;

import com.skye.fittracker.dto.ExerciseCreateRequest;
import com.skye.fittracker.entity.Exercise;
import com.skye.fittracker.service.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
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
}