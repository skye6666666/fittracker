package com.skye.fittracker.repository;

import com.skye.fittracker.entity.Exercise;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.entity.WorkoutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface WorkoutRecordRepository
        extends JpaRepository<WorkoutRecord, Long> {
    List<WorkoutRecord> findByUserId(Long userId);

    List<WorkoutRecord> findByUserIdAndWorkoutDate(
            Long userId,
            LocalDate date
    );

    List<WorkoutRecord> findByUserIdAndExerciseIdOrderByWorkoutDateAsc(
            Long userId,
            Long ExerciseId
    );


    Long user(User user);
}