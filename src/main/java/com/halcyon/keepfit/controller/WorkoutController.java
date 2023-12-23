package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.dto.workout.NewWorkoutDto;
import com.halcyon.keepfit.model.Complexity;
import com.halcyon.keepfit.model.Status;
import com.halcyon.keepfit.model.Workout;
import com.halcyon.keepfit.service.workout.WorkoutService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<Workout> create(@RequestBody @Valid NewWorkoutDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Workout workout = workoutService.create(dto);
        return ResponseEntity.ok(workout);
    }

    @DeleteMapping("/{workoutId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long workoutId) {
        String response = workoutService.deleteById(workoutId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Workout>> getAllByOwner() {
        List<Workout> workouts = workoutService.findAllByOwner();
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/complexity")
    public ResponseEntity<List<Workout>> getAllByComplexity(@RequestParam @Valid Complexity value) {
        List<Workout> workouts = workoutService.findAllByComplexity(value);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Workout>> getAllByStatus(@RequestParam @Valid Status value) {
        List<Workout> workouts = workoutService.findAllByStatus(value);
        return ResponseEntity.ok(workouts);
    }

    @PatchMapping("/{workoutId}/complete")
    public ResponseEntity<Workout> complete(@PathVariable Long workoutId) {
        Workout workout = workoutService.complete(workoutId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{workoutId}/fail")
    public ResponseEntity<Workout> fail(@PathVariable Long workoutId) {
        Workout workout = workoutService.fail(workoutId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{workoutId}/in-progress")
    public ResponseEntity<Workout> inProgress(@PathVariable Long workoutId) {
        Workout workout = workoutService.inProgress(workoutId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{workoutId}/waiting")
    public ResponseEntity<Workout> waiting(@PathVariable Long workoutId) {
        Workout workout = workoutService.waiting(workoutId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{workoutId}/update-title")
    public ResponseEntity<Workout> updateTitle(
            @PathVariable Long workoutId,
            @RequestParam
            @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.") String value
    ) {
        Workout workout = workoutService.updateTitleById(value, workoutId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{workoutId}/update-description")
    public ResponseEntity<Workout> updateDescription(
            @PathVariable Long workoutId,
            @RequestParam
            @Size(max = 64, message = "About must be less than 64 characters.") String value
    ) {
        Workout workout = workoutService.updateDescriptionById(value, workoutId);
        return ResponseEntity.ok(workout);
    }

    @PatchMapping("/{workoutId}/update-complexity")
    public ResponseEntity<Workout> updateComplexity(
            @PathVariable Long workoutId,
            @RequestParam @Valid Complexity value
    ) {
        Workout workout = workoutService.updateComplexityById(value, workoutId);
        return ResponseEntity.ok(workout);
    }
}
