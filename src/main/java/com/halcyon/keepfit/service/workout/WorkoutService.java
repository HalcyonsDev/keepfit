package com.halcyon.keepfit.service.workout;

import com.halcyon.keepfit.dto.workout.NewWorkoutDto;
import com.halcyon.keepfit.model.*;
import com.halcyon.keepfit.repository.IWorkoutRepository;
import com.halcyon.keepfit.service.auth.AuthService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final IWorkoutRepository workoutRepository;
    private final UserService userService;
    private final AuthService authService;

    public Workout create(NewWorkoutDto dto) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());

        Workout workout = Workout.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .complexity(dto.getComplexity())
                .owner(owner)
                .build();

        return workoutRepository.save(workout);
    }

    public String deleteById(Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        workoutRepository.delete(workout);
        return "Workout was deleted successfully.";
    }

    public Workout findById(Long workoutId) {
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workout with this id not found."));
    }

    public List<Workout> findAllByOwner() {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return workoutRepository.findAllByOwner(owner);
    }

    public List<Workout> findAllByComplexity(Complexity complexity) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return workoutRepository.findAllByComplexityAndOwner(complexity, owner);
    }

    public List<Workout> findAllByStatus(Status status) {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return workoutRepository.findAllByStatusAndOwner(status, owner);
    }

    public Workout complete(Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        if (workout.getStatus().equals(Status.COMPLETED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This workout has already completed");
        }

        int experience = user.getExperience() + workout.getComplexity().getExperience();
        Rank rank = getRank(experience);
        workout.setStatus(Status.COMPLETED);

        user.setExperience(experience);
        user.setRank(rank);

        userService.save(user);

        return workoutRepository.save(workout);
    }

    public Workout fail(Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        if (workout.getStatus().equals(Status.FAILED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This workout has already failed");
        }

        int experience = user.getExperience() - workout.getComplexity().getExperience();
        Rank rank = getRank(experience);
        workout.setStatus(Status.FAILED);

        user.setExperience(experience);
        user.setRank(rank);

        userService.save(user);

        return workoutRepository.save(workout);
    }

    public Workout inProgress(Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        if (workout.getStatus().equals(Status.IN_PROGRESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This workout is already in progress");
        }

        workout.setStatus(Status.IN_PROGRESS);

        return workoutRepository.save(workout);
    }

    public Workout waiting(Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        if (workout.getStatus().equals(Status.WAITING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This workout is already waiting");
        }

        workout.setStatus(Status.IN_PROGRESS);

        return workoutRepository.save(workout);
    }

    public Workout updateTitleById(String title, Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        workout.setTitle(title);
        workoutRepository.updateTitleById(title, workoutId);

        return workout;
    }

    public Workout updateDescriptionById(String description, Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        workout.setDescription(description);
        workoutRepository.updateDescription(description, workoutId);

        return workout;
    }

    public Workout updateComplexityById(Complexity complexity, Long workoutId) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Workout workout = checkForAccess(user, workoutId);

        workout.setComplexity(complexity);
        workoutRepository.updateComplexity(complexity, workoutId);

        return workout;
    }

    private Rank getRank(int experience) {
        if (Rank.LEGEND.getStart() < experience) return Rank.LEGEND;
        if (Rank.MASTER.getStart() < experience && experience <= Rank.MASTER.getEnd()) return Rank.MASTER;
        if (Rank.EXPERT.getStart() < experience && experience <= Rank.EXPERT.getEnd()) return Rank.EXPERT;
        return Rank.BEGINNER;
    }

    private Workout checkForAccess(User user, Long workoutId) {
        Workout workout = findById(workoutId);

        if (!user.equals(workout.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this workout");
        }

        return workout;
    }
}
