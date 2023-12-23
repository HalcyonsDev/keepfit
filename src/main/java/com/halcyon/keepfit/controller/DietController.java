package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.dto.diet.NewDietDto;
import com.halcyon.keepfit.model.Diet;
import com.halcyon.keepfit.service.diet.DietService;
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
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietController {
    private final DietService dietService;

    @PostMapping
    public ResponseEntity<Diet> create(@RequestBody @Valid NewDietDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Diet diet = dietService.create(dto);
        return ResponseEntity.ok(diet);
    }

    @DeleteMapping("/{goalId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long goalId) {
        String response = dietService.deleteById(goalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Diet>> getAllByOwner() {
        List<Diet> diets = dietService.findAllByOwner();
        return ResponseEntity.ok(diets);
    }

    @PatchMapping("/{dietId}/update-title")
    public ResponseEntity<Diet> updateTitle(
            @PathVariable Long dietId,
            @RequestParam
            @Size(min = 2, max = 50, message = "Title must be more than 1 character and less than 50 characters.") String value
    ) {
        Diet diet = dietService.updateTitleById(value, dietId);
        return ResponseEntity.ok(diet);
    }

    @PatchMapping("/{dietId}/update-description")
    public ResponseEntity<Diet> updateDescription(
            @PathVariable Long dietId,
            @RequestParam
            @Size(max = 64, message = "Description must be less than 64 characters.") String value
    ) {
        Diet diet = dietService.updateDescriptionById(value, dietId);
        return ResponseEntity.ok(diet);
    }
}
