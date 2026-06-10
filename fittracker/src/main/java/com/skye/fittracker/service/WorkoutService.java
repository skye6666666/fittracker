package com.skye.fittracker.service;

import com.skye.fittracker.dto.ProgressDto;
import com.skye.fittracker.dto.WorkoutCreateRequest;
import com.skye.fittracker.dto.WorkoutResponse;
import com.skye.fittracker.dto.WorkoutUpdateRequest;
import com.skye.fittracker.entity.Exercise;
import com.skye.fittracker.entity.User;
import com.skye.fittracker.entity.WorkoutRecord;
import com.skye.fittracker.repository.ExerciseRepository;
import com.skye.fittracker.repository.UserRepository;
import com.skye.fittracker.repository.WorkoutRecordRepository;
import com.skye.fittracker.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRecordRepository workoutRepo;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final JwtUtil jwtUtil;

    public WorkoutService(WorkoutRecordRepository workoutRepo,
                          UserRepository userRepository,
                          ExerciseRepository exerciseRepository,
                          JwtUtil jwtUtil) {
        this.workoutRepo = workoutRepo;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.jwtUtil = jwtUtil;
    }

    public WorkoutResponse createWorkout(Long userId,
                                         WorkoutCreateRequest request) { //  20260603改JWt不再手動使用 (String token, WorkoutCreateRequest request) {

//        // 1️⃣ 從 token 拿 email
//        String email = jwtUtil.extractEmail(token);
//
        // 2️⃣ 找 user
        User user = userRepository.findById(userId)    //.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        // 3️⃣ 建立 workout
        WorkoutRecord record = new WorkoutRecord();

        record.setWeight(request.getWeight());
        record.setReps(request.getReps());
        record.setSets(request.getSets());
        //record.setWorkoutDate(request.getWorkoutDate());
        record.setWorkoutDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));


        // 🔥 關鍵：自動綁 user
        record.setUser(user);
        record.setExercise(exercise);

        // 4️⃣ 存 DB
        WorkoutRecord saved = workoutRepo.save(record);
        return toResponse(saved);
    }

    //一開始開發練習拿來查全部(不分類) 現在沒有用到
    public List<WorkoutResponse> getMyWorkouts(Long userId) {

        //String email = jwtUtil.extractEmail(token);

//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        return workoutRepo.findByUserId(userId)
                .stream().map(this::toResponse)
                .toList();
    }

    public List<WorkoutResponse> getWorkoutsByDate(Long userId, LocalDate date) {

        // 如果沒傳 date → 預設今天
        if (date == null) {
            date = LocalDate.now();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        return workoutRepo
                .findByUserIdAndWorkoutDateBetween(userId, start, end)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProgressDto> getWorkoutProgess(Long userId, Long exerciseId) {

        return workoutRepo
                .findByUserIdAndExerciseIdOrderByWorkoutDateAsc(userId, exerciseId)
                .stream()
                .map(record ->
                        new ProgressDto(
                                record.getWorkoutDate(),
                                record.getWeight())
                )
                .toList();
    }

    private WorkoutResponse toResponse(WorkoutRecord record) {

        return new WorkoutResponse(
                record.getId(),
                record.getExercise().getName(),
                record.getExercise().getMuscleGroup(),
                record.getWeight(),
                record.getReps(),
                record.getSets(),
                record.getWorkoutDate()
        );
    }

    public void deleteWorkout(
            Long userId,
            Long workoutId
    ) {

        WorkoutRecord workout =
                workoutRepo
                        .findById(workoutId)
                        .orElseThrow();

        if (!workout.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權限刪除");
        }

        workoutRepo.delete(workout);
    }

    public WorkoutResponse updateWorkout(
            Long userId,
            Long workoutId,
            WorkoutUpdateRequest request
    ) {

        WorkoutRecord workout =
                workoutRepo
                        .findById(workoutId)
                        .orElseThrow();

        if (!workout.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權限");
        }

        workout.setWeight(request.getWeight());
        workout.setReps(request.getReps());
        workout.setSets(request.getSets());

        workoutRepo.save(workout);

        return toResponse(workout);

    }
}