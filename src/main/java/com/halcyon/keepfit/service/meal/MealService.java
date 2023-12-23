package com.halcyon.keepfit.service.meal;

import com.halcyon.keepfit.dto.meal.NewMealDto;
import com.halcyon.keepfit.model.Diet;
import com.halcyon.keepfit.model.Meal;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.repository.IMealRepository;
import com.halcyon.keepfit.service.auth.AuthService;
import com.halcyon.keepfit.service.diet.DietService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final IMealRepository mealRepository;
    private final UserService userService;
    private final AuthService authService;
    private final DietService dietService;

    public Meal create(NewMealDto dto) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        Diet diet = dietService.findById(dto.getDietId());

        Meal meal = Meal.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .recipe(dto.getRecipe())
                .carbohydrates(dto.getCarbohydrates())
                .fats(dto.getFats())
                .proteins(dto.getProteins())
                .owner(owner)
                .diet(diet)
                .build();

        dietService.changeNutrients(diet, meal);

        return mealRepository.save(meal);
    }

    public String deleteById(Long mealId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Meal meal = checkForAccess(user, mealId);

        mealRepository.delete(meal);
        return "Meal was deleted successfully.";
    }

    public Meal findById(Long mealId) {
        return mealRepository.findById(mealId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meal with this id not found."));
    }

    public List<Meal> findAllByOwner() {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return mealRepository.findAllByOwner(owner);
    }

    public List<Meal> findAllByDietId(Long dietId) {
        Diet diet = dietService.findById(dietId);
        return mealRepository.findAllByDiet(diet);
    }

    public Meal updateTitleById(String title, Long mealId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Meal meal = checkForAccess(user, mealId);

        meal.setTitle(title);
        mealRepository.updateTitleById(title, mealId);

        return meal;
    }

    public Meal updateDescriptionById(String description, Long mealId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Meal meal = checkForAccess(user, mealId);

        meal.setDescription(description);
        mealRepository.updateDescriptionById(description, mealId);

        return meal;
    }

    private Meal checkForAccess(User user, Long mealId) {
        Meal meal = findById(mealId);

        if (!meal.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this meal.");
        }

        return meal;
    }
}
