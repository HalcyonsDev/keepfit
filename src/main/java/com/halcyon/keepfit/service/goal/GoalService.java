package com.halcyon.keepfit.service.goal;

import com.halcyon.keepfit.dto.goal.NewGoalDto;
import com.halcyon.keepfit.model.*;
import com.halcyon.keepfit.repository.IGoalRepository;
import com.halcyon.keepfit.service.auth.AuthService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final IGoalRepository goalRepository;
    private final UserService userService;
    private final AuthService authService;

    public Goal create(NewGoalDto dto) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());

        Goal goal = Goal.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .complexity(dto.getComplexity())
                .status(dto.getStatus())
                .deadline(dto.getDeadline())
                .owner(owner)
                .build();

        return goalRepository.save(goal);
    }

    public String deleteById(Long goalId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Goal goal = checkForAccess(user, goalId);

        goalRepository.delete(goal);
        return "Goal was deleted successfully.";
    }

    public Goal findById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal with this id not found."));
    }

    public List<Goal> findAllByOwner() {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return goalRepository.findAllByOwner(owner);
    }

    public List<Goal> findAllByComplexityAndOwner(Complexity complexity) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return goalRepository.findAllByComplexityAndOwner(complexity, owner);
    }

    public List<Goal> findAllByStatusAndOwner(Status status) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return goalRepository.findAllByStatusAndOwner(status, owner);
    }

    public Goal updateTitleById(String title, Long goalId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Goal goal = checkForAccess(user, goalId);

        goal.setTitle(title);
        goalRepository.updateTitleById(title, goalId);

        return goal;
    }

    public Goal updateDescriptionById(String description, Long goalId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Goal goal = checkForAccess(user, goalId);

        goal.setDescription(description);
        goalRepository.updateDescriptionById(description, goalId);

        return goal;
    }

    public Goal updateComplexityById(Complexity complexity, Long goalId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Goal goal = checkForAccess(user, goalId);

        goal.setComplexity(complexity);
        goalRepository.updateComplexityById(complexity, goalId);

        return goal;
    }

    public Goal updateStatusById(Status status, Long goalId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Goal goal = checkForAccess(user, goalId);

        goal.setStatus(status);
        goalRepository.updateStatusById(status, goalId);

        return goal;
    }

    private Goal checkForAccess(User user, Long goalId) {
        Goal goal = findById(goalId);

        if (!goal.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this goal.");
        }

        return goal;
    }
}
