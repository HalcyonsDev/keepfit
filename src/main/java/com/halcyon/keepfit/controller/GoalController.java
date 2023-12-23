package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.dto.goal.NewGoalDto;
import com.halcyon.keepfit.model.Complexity;
import com.halcyon.keepfit.model.Goal;
import com.halcyon.keepfit.model.Status;
import com.halcyon.keepfit.service.goal.GoalService;
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
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<Goal> create(@RequestBody @Valid NewGoalDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Goal goal = goalService.create(dto);
        return ResponseEntity.ok(goal);
    }

    @DeleteMapping("/{goalId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long goalId) {
        String response = goalService.deleteById(goalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Goal>> getAllByOwner() {
        List<Goal> goals = goalService.findAllByOwner();
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/complexity")
    public ResponseEntity<List<Goal>> getAllByComplexity(@RequestParam @Valid Complexity value) {
        List<Goal> goals = goalService.findAllByComplexityAndOwner(value);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Goal>> getAllByStatus(@RequestParam @Valid Status status) {
        List<Goal> goals = goalService.findAllByStatusAndOwner(status);
        return ResponseEntity.ok(goals);
    }

    @PatchMapping("/{goalId}/update-title")
    public ResponseEntity<Goal> updateTitle(
            @PathVariable Long goalId,
            @RequestParam
            @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.") String value
    ) {
        Goal goal = goalService.updateTitleById(value, goalId);
        return ResponseEntity.ok(goal);
    }

    @PatchMapping("/{goalId}/update-description")
    public ResponseEntity<Goal> updateDescription(
            @PathVariable Long goalId,
            @RequestParam
            @Size(max = 64, message = "About must be less than 64 characters.") String value
    ) {
        Goal goal = goalService.updateDescriptionById(value, goalId);
        return ResponseEntity.ok(goal);
    }

    @PatchMapping("/{goalId}/update-complexity")
    public ResponseEntity<Goal> updateComplexity(
            @PathVariable Long goalId,
            @RequestParam @Valid Complexity value
    ) {
        Goal goal = goalService.updateComplexityById(value, goalId);
        return ResponseEntity.ok(goal);
    }

    @PatchMapping("/{goalId}/update-status")
    public ResponseEntity<Goal> updateStatus(
            @PathVariable Long goalId,
            @RequestParam @Valid Status value
    ) {
        Goal goal = goalService.updateStatusById(value, goalId);
        return ResponseEntity.ok(goal);
    }
}
