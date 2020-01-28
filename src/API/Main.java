package API;

import Model.WebClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Controllers.PopupServerUnavailableController;

public class Main extends Application {

    static Parent root;
    static Stage primaryStage;
    private static WebClient webClient;

    @Override
    public void start(Stage primaryStage) {
        Scene scene;

        webClient = new WebClient();
        try{
            if (webClient.connect()) {
                root = FXMLLoader.load(getClass().getResource("/Views/welcomePageView.fxml"));
                scene = new Scene(root, 800, 500);
                Main.primaryStage = primaryStage;
                primaryStage.setTitle("Cinema Ticket Reservation System");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.show();
            } else {
                new PopupServerUnavailableController();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static WebClient getWebClient() { return webClient;}

    public static Parent getRoot(){
        return root;
    }

    public static void setRoot(Parent root){ Main.root = root; }

    public static Stage getStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
