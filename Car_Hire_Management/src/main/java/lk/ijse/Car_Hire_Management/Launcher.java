package lk.ijse.Car_Hire_Management;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));


        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        if (iconStream != null) {
            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);
        }

        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
