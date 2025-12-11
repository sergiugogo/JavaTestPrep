package service;

import domain.Workout;
import repo.DBRepository;
import repo.RepositoryException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    private DBRepository repo;

    public Service(DBRepository repo) {
        this.repo = repo;
    }

    public List<Workout> getALlWorkoutSorted(){
        return StreamSupport.stream(repo.getAll().spliterator(), false)
                .sorted(Comparator.comparing(Workout::getStart))
                .collect(Collectors.toList());
    }
    public List<Workout> getAllWorkoutIntenSorted(int value){
        return StreamSupport.stream(repo.getAll().spliterator(), false)
                .filter(workout -> workout.getIntensity() > value)
                .sorted(Comparator.comparing(Workout::getStart))
                .collect(Collectors.toList());
    }
    public List<Workout> searchWorkouts(String text, int hour){
        return StreamSupport.stream(repo.getAll().spliterator(), false)
                .filter(workout -> workout.getDesc().equalsIgnoreCase(text))
                .filter(workout -> workout.getStart() >= hour)
                .collect(Collectors.toList());
    }
}
