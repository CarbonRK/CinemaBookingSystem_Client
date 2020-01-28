package Controllers;

import Utilities.CustomFilmsAndTicketsList;
import Utilities.CustomProgressIndicator;
import Utilities.Record;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import API.Main;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoggedInClientController {

    @FXML private JFXButton availableFilms;
    @FXML private AnchorPane anchorPane;
    @FXML private JFXButton logOut;
    @FXML private JFXButton myTickets;
    @FXML private JFXButton changeInfo;
    @FXML private JFXButton changePassword;
    @FXML private JFXButton myInfo;
    @FXML private JFXButton delete;
    private CustomProgressIndicator progressIndicator;

    @FXML public void initialize(){
        progressIndicator = new CustomProgressIndicator(255,188,anchorPane);
        assignFunctionToButtons();
    }

    private void assignFunctionToButtons(){
        availableFilms.setOnAction(event -> showFilms());
        logOut.setOnAction(event -> loggingOut());
        myTickets.setOnAction(event -> showTickets());
        changeInfo.setOnAction(event -> editInfo());
        changePassword.setOnAction(event -> changePass());
        myInfo.setOnAction(event -> showInfo());
        delete.setOnAction(event -> deleteAccount());
    }

    private void disableAllButtons(boolean set){
        JFXButton[] jfxButtons = new JFXButton[]{availableFilms,logOut, myTickets, changeInfo, changePassword, myInfo, delete};
        for (JFXButton jfxButton : jfxButtons) {
            jfxButton.setDisable(set);
        }
    }

    private void routineForTaskStart(){
        anchorPane.getChildren().clear();
        progressIndicator.show();
        disableAllButtons(true);
    }

    private void routineForTaskEnd(){
        disableAllButtons(false);
        progressIndicator.hide();
    }

    private CustomFilmsAndTicketsList getCustomList(Boolean showFilms){
        CustomFilmsAndTicketsList availableLists = new CustomFilmsAndTicketsList(showFilms);
        availableLists.getStylesheets().add(getClass().getResource("/Views/CSSFiles/darkBlueThemeForListCell.css").toExternalForm()); //podrobic
        availableLists.prefHeightProperty().bind(anchorPane.heightProperty());
        availableLists.prefWidthProperty().bind(anchorPane.widthProperty());
        availableLists.setOnMouseClicked(e -> showFilm(availableLists));
        return availableLists;
    }

    private CustomFilmsAndTicketsList getTicketList(){ return getCustomList(false); }

    private CustomFilmsAndTicketsList getFilmList(){ return getCustomList(true);}

    private void showFilms(){
        routineForTaskStart();

        Task<CustomFilmsAndTicketsList> asyncTask = new Task<>() {
            @Override
            protected CustomFilmsAndTicketsList call() {
                CustomFilmsAndTicketsList availableLists = getFilmList();
                availableLists.setOnMouseClicked(e -> showFilm(availableLists));
                return availableLists;
            }
        };

        taskWork(asyncTask);
    }

    private void showTickets(){
        routineForTaskStart();

        Task<CustomFilmsAndTicketsList> asyncTask = new Task<>() {
            @Override
            protected CustomFilmsAndTicketsList call() { return getTicketList(); }
        };

        taskWork(asyncTask);
    }

    private void taskWork(Task<CustomFilmsAndTicketsList> asyncTask) {
        asyncTask.setOnSucceeded((e) -> {
            routineForTaskEnd();

            CustomFilmsAndTicketsList availableLists;
            try {
                availableLists = asyncTask.get();
                if (availableLists != null) {
                    anchorPane.getChildren().add(availableLists);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        asyncTask.setOnFailed((e) -> { routineForTaskEnd(); });
        new Thread(asyncTask).start();
    }

    private void showFilm(ListView<Record> listView){
        SeatReservationForClientController.setText(listView.getSelectionModel().getSelectedItem().getTitle());
        changeView("/Views/seatReservationForClientView.fxml");
    }

    private void changeView(String fxml){
        routineForTaskStart();

        Task<Node> asyncTask = new Task<>() {
            @Override
            protected Node call() {
                FXMLLoader loader
                        ;
                loader = new FXMLLoader(Main.class.getResource(fxml));
                try {
                    return loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        asyncTask.setOnSucceeded((e) -> {
            routineForTaskEnd();

            try {
                anchorPane.getChildren().add(asyncTask.get());
            } catch (ExecutionException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        asyncTask.setOnFailed((e) -> {routineForTaskEnd(); });
        new Thread(asyncTask).start();
    }

    private void loggingOut(){ new LeavingAppClientPopUpController(false); }

    private void showInfo(){
        changeView("/Views/viewClientInformations.fxml");
    }

    private void editInfo() {
        changeView("/Views/updateClientInformationsView.fxml");
    }

    private void changePass() { changeView("/Views/changeClientPasswordView.fxml"); }

    private void deleteAccount(){ new LeavingAppClientPopUpController(true); }

}
