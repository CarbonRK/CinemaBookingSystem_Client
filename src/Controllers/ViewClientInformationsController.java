package Controllers;

import Utilities.CustomProgressIndicator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import API.Main;

public class ViewClientInformationsController {

    @FXML private Label name;
    @FXML private Label surname;
    @FXML private Label email;
    @FXML private Label sex;
    @FXML private Label birthday;

    @FXML
    public void initialize() {
        String info;
        String[] infos;

        info = Main.getWebClient().getInfos();
        infos = info.split(" ");

        name.setText(infos[1]);
        surname.setText(infos[2]);
        email.setText(infos[3]);

        if (infos[4].equals("F")) {
            sex.setText("Female");
        } else {
            sex.setText("Male");
        }

        birthday.setText(infos[5]);
    }
}