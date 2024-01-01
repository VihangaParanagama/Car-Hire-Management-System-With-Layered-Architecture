package lk.ijse.Car_Hire_Management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class MainMenuController {
    @FXML
    private Label userNameText;

    @FXML
    private AnchorPane mainAnchorpane;

    private String loggedInUser;

    private void loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setLoggedInUser(loggedInUser);

        mainAnchorpane.getChildren().setAll(root);
    }

    public void setLoggedInUser(String loggedInUser) {
        this.loggedInUser = loggedInUser;
        userNameText.setText(loggedInUser);
    }
    public void customerRegisterButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomerForm.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        Parent root = loader.load();
        loader.getController();
        mainAnchorpane.getChildren().setAll(root);
    }
    public void carRegisterButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Car.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        Parent root = loader.load();
        loader.getController();
        mainAnchorpane.getChildren().setAll(root);
    }

    public void addRentButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RentForm.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        Parent root = loader.load();
        loader.getController();
        mainAnchorpane.getChildren().setAll(root);
    }

    public void returnCarButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReturnForm.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        Parent root = loader.load();
        loader.getController();
        mainAnchorpane.getChildren().setAll(root);
    }

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        Scene scene = new Scene(root);


        Node source = (Node) actionEvent.getSource();


        Stage currentStage = (Stage) source.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Login Form");
        newStage.centerOnScreen();


        currentStage.close();

        newStage.show();
    }

    public void addCarCategoryButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CarCategory.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        Parent root = loader.load();
        loader.getController();
        mainAnchorpane.getChildren().setAll(root);
    }

    public void summaryButton(ActionEvent actionEvent) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Summary.fxml"));
        InputStream iconStream = getClass().getResourceAsStream("/view/icons/MenuBarIcon.png");
        Parent root = loader.load();
        loader.getController();
        mainAnchorpane.getChildren().setAll(root);
    }
}
