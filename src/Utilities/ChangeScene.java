package Utilities;

import API.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class ChangeScene {

    public static Parent getScene(String sceneName){
        FXMLLoader loader;

        loader = new FXMLLoader(Main.class.getResource(sceneName));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void launchScene(String sceneName) {
        Scene scene;
        Parent sceneView;

        sceneView = getScene(sceneName);
        if (sceneView != null) {
            Main.setRoot(sceneView);
            scene = new Scene(Main.getRoot(), 800, 500);
            Main.getStage().setScene(scene);
            Main.getStage().show();
        }
    }
}
