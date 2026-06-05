package com.skye.fittracker.service;

import com.skye.fittracker.dto.ExerciseCreateRequest;
import com.skye.fittracker.entity.Exercise;
import com.skye.fittracker.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Exercise createExercise(ExerciseCreateRequest request) {

        Exercise exercise = new Exercise();

        exercise.setName(request.getName());
        exercise.setMuscleGroup(request.getMuscleGroup());
        exercise.setDescription(request.getDescription());

        return exerciseRepository.save(exercise);
    }

    public List<Exercise> getAllExercises(){
        return exerciseRepository.findAll();
    }
}