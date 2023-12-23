package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.dto.meal.NewMealDto;
import com.halcyon.keepfit.model.Meal;
import com.halcyon.keepfit.service.meal.MealService;
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
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @PostMapping
    public ResponseEntity<Meal> create(@RequestBody @Valid NewMealDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Meal meal = mealService.create(dto);
        return ResponseEntity.ok(meal);
    }

    @DeleteMapping("/{mealId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long mealId) {
        String response = mealService.deleteById(mealId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Meal>> getAllByOwner() {
        List<Meal> meals = mealService.findAllByOwner();
        return ResponseEntity.ok(meals);
    }

    @GetMapping("diets/{dietId}")
    public ResponseEntity<List<Meal>> getAllByDietId(@PathVariable Long dietId) {
        List<Meal> meals = mealService.findAllByDietId(dietId);
        return ResponseEntity.ok(meals);
    }

    @PatchMapping("/{mealId}/update-title")
    public ResponseEntity<Meal> updateTitle(
            @PathVariable Long mealId,
            @RequestParam
            @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.") String value
    ) {
        Meal meal = mealService.updateTitleById(value, mealId);
        return ResponseEntity.ok(meal);
    }

    @PatchMapping("/{mealId}/update-description")
    public ResponseEntity<Meal> updateDescription(
            @PathVariable Long mealId,
            @RequestParam
            @Size(max = 64, message = "Description must be less than 64 characters.") String value
    ) {
        Meal meal = mealService.updateDescriptionById(value, mealId);
        return ResponseEntity.ok(meal);
    }
}
