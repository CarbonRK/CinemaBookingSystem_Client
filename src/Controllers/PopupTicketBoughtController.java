package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import API.Main;
import java.net.URL;


public class PopupTicketBoughtController extends Pane {

    @FXML private Label text;
    @FXML private JFXButton orderOk;
    private static Stage ticketBought;

    public PopupTicketBoughtController(){
        ticketBought = new Stage();
        ticketBought.setMinHeight(132);
        ticketBought.setMinWidth(500);
        ticketBought.initOwner(Main.getStage());
        ticketBought.initModality(Modality.APPLICATION_MODAL);

        try{
            FXMLLoader loader = new FXMLLoader();
            URL xmlURL = getClass().getResource("/Views/popupTicketBoughtView.fxml");
            loader.setLocation(xmlURL);
            loader.setController(this);
            Parent root1 = loader.load();
            ticketBought.setScene(new Scene(root1));
            ticketBought.setResizable(false);
            ticketBought.initStyle(StageStyle.UNDECORATED);
            orderOk.setOnAction(event -> close());
            ticketBought.showAndWait();

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void close(){
        ticketBought.close();
    }
}
