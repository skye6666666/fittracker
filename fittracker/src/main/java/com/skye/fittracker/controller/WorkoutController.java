package com.skye.fittracker.controller;

import com.skye.fittracker.dto.WorkoutCreateRequest;
import com.skye.fittracker.dto.WorkoutResponse;
import com.skye.fittracker.dto.WorkoutUpdateRequest;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.entity.WorkoutRecord;
import com.skye.fittracker.service.WorkoutService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public WorkoutResponse createWorkout(
            @AuthenticationPrincipal User user, //@RequestHeader("Authorization") String authHeader,
            @RequestBody WorkoutCreateRequest request
    ) {
        //String token = authHeader.substring(7); // 去掉 "Bearer "
        return workoutService.createWorkout(user.getId(), request);
    }

//    @GetMapping
//    public List<WorkoutResponse> getMyWorkouts(
//            @RequestHeader("Authorization") String authHeader
//    ) {
//        String token = authHeader.substring(7);
//        return workoutService.getMyWorkouts(token);
//    }

//    @GetMapping
//    public List<WorkoutResponse> getMyWorkouts(
//            @AuthenticationPrincipal User user
//    ) {
//        return workoutService.getMyWorkouts(user.getId());
//    }

    @GetMapping
    public List<WorkoutResponse> getWorkoutsByDate(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) LocalDate date
    ) {
        return workoutService.getWorkoutsByDate(
                user.getId(),
                date
        );
    }

    @DeleteMapping("/{id}")
    public void deleteWorkout(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        workoutService.deleteWorkout(
                user.getId(),
                id
        );
    }

    @PutMapping("/{id}")
    public WorkoutResponse updateWorkout(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @RequestBody WorkoutUpdateRequest request
    ) {

        return workoutService.updateWorkout(
                user.getId(),
                id,
                request
        );

    }
}