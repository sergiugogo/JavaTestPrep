package GUI;

import domain.Workout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import service.Service;

import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    @FXML
    private ListView<Workout> workoutListView;

    @FXML
    private TextField intensityFilterField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField startTimeField;

    @FXML
    private ListView<String> filteredIntervalsListView;

    @FXML
    private Label totalTimeLabel;

    private Service service;
    private ObservableList<Workout> workoutObservableList;
    private ObservableList<String> intervalObservableList;

    public Controller(Service service) {this.service = service;}

    @FXML
    public void initialize(){
        workoutObservableList = FXCollections.observableArrayList();
        intervalObservableList = FXCollections.observableArrayList();

        workoutListView.setItems(workoutObservableList);
        filteredIntervalsListView.setItems(intervalObservableList);

        loadAllWorkouts();

    }

    private void loadAllWorkouts(){
        List<Workout> list = service.getALlWorkoutSorted();
        workoutObservableList.setAll(list);
    }

    @FXML
    public void handleFilterByIntensity(){
        String text = intensityFilterField.getText();
        if(text.isEmpty()){
            loadAllWorkouts();
            return;
        }
        int intensity = Integer.parseInt(intensityFilterField.getText());
        List<Workout> list = service.getAllWorkoutIntenSorted(intensity);
        workoutObservableList.setAll(list);
    }

    @FXML
    public void handleCalculate(){
        String desc = descriptionField.getText();
        String time = startTimeField.getText();

        if(desc.isEmpty() || time.isEmpty()){
            showError("Please enter desc and time");
            return;
        }
        try{
            int minStart = Integer.parseInt(time);
            List<Workout> matches = service.searchWorkouts(desc, minStart);
            if(matches.isEmpty()){
                showError("No such intervals found.");
                intervalObservableList.clear();
                totalTimeLabel.setText("0");
            }
            else{
                List<String> intervalStrings = matches.stream()
                        .map(w -> w.getStart() + " - " + w.getEnd())
                        .collect(Collectors.toList());
                intervalObservableList.setAll(intervalStrings);

                int totalHours = matches.stream()
                        .mapToInt(w->w.getEnd() - w.getStart())
                        .sum();
                totalTimeLabel.setText(String.valueOf(totalHours));
            }
        }catch (NumberFormatException e){
                showError("Start time must be valid");
            }

        }

        private void showError(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText(msg);
        alert.showAndWait();
        }
}
