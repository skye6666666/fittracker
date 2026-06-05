package com.skye.fittracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Double weight;

    private Integer reps;

    private Integer sets;

    private LocalDateTime workoutDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}