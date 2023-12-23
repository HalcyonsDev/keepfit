package com.halcyon.keepfit.service.diet;

import com.halcyon.keepfit.dto.diet.NewDietDto;
import com.halcyon.keepfit.model.Diet;
import com.halcyon.keepfit.model.Meal;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.repository.IDietRepository;
import com.halcyon.keepfit.service.auth.AuthService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietService {
    private final IDietRepository dietRepository;
    private final UserService userService;
    private final AuthService authService;

    public Diet create(NewDietDto dto) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());

        Diet diet = Diet.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .percentageOfCarbohydrates("0%")
                .percentageOfFats("0%")
                .percentageOfProteins("0%")
                .owner(owner)
                .build();

        return dietRepository.save(diet);
    }

    public String deleteById(Long dietId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Diet diet = checkForAccess(user, dietId);

        dietRepository.delete(diet);
        return "Diet was deleted successfully.";
    }

    public Diet findById(Long dietId) {
        return dietRepository.findById(dietId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diet with this id not found."));
    }

    public List<Diet> findAllByOwner() {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return dietRepository.findAllByOwner(owner);
    }

    public void changeNutrients(Diet diet, Meal meal) {
        String perOfCarbohydrates = diet.getPercentageOfCarbohydrates();
        int carbohydrates = Integer.parseInt(perOfCarbohydrates.substring(0, perOfCarbohydrates.length() - 1)) + meal.getCarbohydrates();

        String perOfFats = diet.getPercentageOfFats();
        int fats =  Integer.parseInt(perOfFats.substring(0, perOfFats.length() - 1)) + meal.getFats();

        String perOfProteins = diet.getPercentageOfProteins();
        int proteins = Integer.parseInt(perOfProteins.substring(0, perOfProteins.length() - 1)) + meal.getProteins();

        int sum = carbohydrates + fats + proteins;

        diet.setPercentageOfCarbohydrates((carbohydrates * 100) / sum + "%");
        diet.setPercentageOfFats((fats * 100) / sum + "%");
        diet.setPercentageOfProteins((proteins * 100) / sum + "%");

        dietRepository.save(diet);
    }

    public Diet updateTitleById(String title, Long dietId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Diet diet = checkForAccess(user, dietId);

        diet.setTitle(title);
        dietRepository.updateTitleById(title, dietId);

        return diet;
    }

    public Diet updateDescriptionById(String description, Long dietId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Diet diet = checkForAccess(user, dietId);

        diet.setDescription(description);
        dietRepository.updateDescriptionById(description, dietId);

        return diet;
    }

    private Diet checkForAccess(User user, Long dietId) {
        Diet diet = findById(dietId);

        if (!diet.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this diet.");
        }

        return diet;
    }
}
