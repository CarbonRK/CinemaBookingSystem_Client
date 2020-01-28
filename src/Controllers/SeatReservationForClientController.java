package Controllers;

import Utilities.CustomProgressIndicator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import API.Main;

public class SeatReservationForClientController {

    @FXML private GridPane placesInRoom;
    @FXML private Label chosenFilm;
    @FXML private JFXRadioButton radioButton1;
    @FXML private JFXRadioButton radioButton2;
    @FXML private Label placeNumber;
    @FXML private ToggleGroup toggleGroup;
    @FXML private VBox vBox;
    @FXML private JFXComboBox<String> day;
    @FXML private JFXComboBox<String> time;
    @FXML private AnchorPane anchorPane;

    private JFXTextField name;
    private JFXTextField surname;
    private JFXButton proceed;
    private static String filmTitle;
    private String[] choppedDates;
    private String[] occupiedPlaces;
    private String index;
    private CustomProgressIndicator progressIndicator;

    public static void setText(String title){
        filmTitle = title;
    }

    @FXML public void initialize(){
        placesInRoom.setVisible(true);
        progressIndicator = new CustomProgressIndicator(255,188,anchorPane);
        day.getSelectionModel().selectedItemProperty().addListener(this::unlockTime);
        time.getSelectionModel().selectedItemProperty().addListener(this::unlockGridPane);
        
        makeUI();
        makeDays();
        makeToggleGroup();
    }

    private void makeUI(){
        time.setDisable(true);
        chosenFilm.setText(filmTitle);
        choppedDates = Main.getWebClient().getDatesFilm(filmTitle).split(" ");
        proceed = getProceed();
        name = getFields("Name");
        surname = getFields("Surname");
    }
    
    private void makeToggleGroup(){
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (toggleGroup.getSelectedToggle() == radioButton2) {
                vBox.getChildren().remove(proceed);
                vBox.getChildren().add(name);
                vBox.getChildren().add(surname);
                vBox.getChildren().add(proceed);
            } else {
                vBox.getChildren().remove(name);
                vBox.getChildren().remove(surname);
                name.clear();
                surname.clear();
            }
        });
    }

    private JFXButton getProceed(){
        JFXButton proceed;

        proceed = new JFXButton("Proceed");
        proceed.setMaxWidth(200);
        proceed.setStyle("-fx-background-color: #ab7cff;");
        proceed.setTextFill(Color.web("#ffffff"));
        proceed.setOnAction(event -> makeOrder());
        return proceed;
    }

    private JFXTextField getFields(String prompt){
        JFXTextField textField;

        textField = new JFXTextField();
        textField.setMaxWidth(200);
        textField.setPromptText(prompt);
        textField.setUnFocusColor(Color.web("#ab7cff"));
        textField.setFocusColor(Color.web("#ffffff"));
        textField.setStyle("-fx-text-inner-color: #ffffff; -fx-prompt-text-fill: #ffffff;");
        return textField;
    }
    
    private void unlockTime(ObservableValue<? extends String> options, String oldValue, String newValue) {
        if (newValue != null) {
            makeTime();
            time.setDisable(false);
            placesInRoom.setVisible(false);
            setOrderMenu(false);
        } else {
            time.setDisable(true);
        }
    }

    private void unlockGridPane(ObservableValue<? extends String> options, String oldValue, String newValue) {
        if (newValue != null) {
            setPlaces();
            placesInRoom.setVisible(true);
            setOrderMenu(false);
        } else {
            placesInRoom.setVisible(false);
        }
    }

    private void makeDays(){
        for (int i = 1; i < choppedDates.length; i += 2){
            if (!day.getItems().contains(choppedDates[i])) {
                day.getItems().add(choppedDates[i]);
            }
        }
    }

    private void makeTime(){
        time.getItems().clear();

        for (int i = 1; i < choppedDates.length; i += 2){
            if (day.getSelectionModel().getSelectedItem().equals(choppedDates[i])){
                time.getItems().add(choppedDates[i + 1]);
            }
        }
    }

    private void makeOccupiedPlaces(){
        String dayString =  day.getSelectionModel().getSelectedItem();
        String timeString =  time.getSelectionModel().getSelectedItem();
        occupiedPlaces = Main.getWebClient().getPlaces(filmTitle, dayString, timeString).split(" ");
    }
    
    private int getPlaceState(int counter){
        int color = 1;
        
        for (int k = 1; k<occupiedPlaces.length; k++){
            if (Integer.parseInt(occupiedPlaces[k]) == counter){
                color = 0;
                break;
            }
        }
        
        return color;
    }
    
    private JFXButton makePlaceButton(int counter, int color){
        JFXButton button;
        String[] colors = {"-fx-background-color: #2d3447", "-fx-background-color: #ab7cff"};
        
        button = new JFXButton(String.format("%02d", counter));
        button.setStyle(colors[color]);
        button.setTextFill(Color.web("#ffffff"));
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #ffffff");
            button.setTextFill(Color.web("#000000"));
        });
        button.setOnMouseExited(e -> {
            button.setStyle(colors[color]);
            button.setTextFill(Color.web("#ffffff"));
        });

        if (color == 1) {
            button.setOnAction(e -> onClick(String.format("%02d", counter)));
        }
        
        return button;
    }

    private void showPlaces(){
        int offset = 2;
        int counter = 1;
        int color;
        JFXButton button;

        for (int i = 0; i < 6; i++) {
            for (int j = offset; j < 13 - offset; j++) {
                color = getPlaceState(counter);
                button = makePlaceButton(counter,color);
                placesInRoom.add(button, j, i, 1, 1);
                counter++;
            }
            offset -= i % 2;
        }
    }

    private Task<Void> runPlacesTask(){
        Task<Void> asyncTask = new Task<>() {
            @Override
            protected Void call() {
                makeOccupiedPlaces();
                return null;
            }
        };

        asyncTask.setOnSucceeded((e) -> {
            progressIndicator.hide();
            showPlaces();
        });

        asyncTask.setOnFailed((e) -> progressIndicator.hide());

        return asyncTask;
    }

    private void setPlaces() {
        placesInRoom.getChildren().clear();
        progressIndicator.show();
        Task<Void> asyncTask = runPlacesTask();
        new Thread(asyncTask).start();
    }

    private void setOrderMenu(boolean bool){
        placeNumber.setVisible(bool);
        radioButton1.setVisible(bool);
        radioButton1.setSelected(true);
        radioButton2.setVisible(bool);

        if (vBox.getChildren().contains(proceed)){
            proceed.setVisible(bool);
        }
        if (vBox.getChildren().contains(name)){
            name.setVisible(bool);
            name.clear();
        }
        if (vBox.getChildren().contains(surname)){
            surname.setVisible(bool);
            surname.clear();
        }
    }

    private void onClick(String index) {
        setOrderMenu(true);
        placeNumber.setText("For who should I order place number "+index+"?");
        this.index = index;

        if (!vBox.getChildren().contains(proceed)){
            vBox.getChildren().add(proceed);
        }
    }

    private void successfulOrder(){
        setPlaces();
        setOrderMenu(false);

        new PopupTicketBoughtController();
    }

    private Task<Void> runOrderTask(String allOrder){
        Task<Void> asyncTask = new Task<>() {
            @Override
            protected Void call() {
                Main.getWebClient().makeOrder(allOrder);
                return null;
            }
        };

        asyncTask.setOnSucceeded((e) -> {
            progressIndicator.hide();
            successfulOrder();
        });

        asyncTask.setOnFailed((e) -> progressIndicator.hide());

        return asyncTask;
    }

    private void makeOrder(){
        StringBuilder allOrder;

        allOrder = new StringBuilder();
        allOrder.append(index).append(" ");
        if (radioButton1.isSelected()) {
            allOrder.append("myself ");
        } else {
            String tmp = name.getText()+"-"+surname.getText();
            allOrder.append(tmp).append(" ");
        }
        allOrder.append(day.getSelectionModel().getSelectedItem()).append(" ");
        allOrder.append(time.getSelectionModel().getSelectedItem()).append(" ");
        allOrder.append(filmTitle).append(" ");

        progressIndicator.show();
        Task<Void> asyncTask = runOrderTask(allOrder.toString());

        new Thread(asyncTask).start();
    }
}
