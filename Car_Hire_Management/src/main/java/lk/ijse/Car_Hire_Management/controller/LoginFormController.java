package lk.ijse.Car_Hire_Management.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.Car_Hire_Management.dto.UserDto;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.UserService;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;

import java.io.IOException;

public class LoginFormController {
    private static String loggedInUserName;

    @FXML
    private TextField passwordText;

    @FXML
    private TextField userNameText;

    private UserService userService;



    @FXML
    void userLoginButton(ActionEvent actionEvent) throws Exception {
        userService = ServiceFactory.getInstance().getService(ServiceType.USER);

        String username = userNameText.getText().trim();
        String password = passwordText.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username and password cannot be empty").show();
            return;
        }

        try {
            UserDto dto = userService.getUser(username,password);

            if (dto != null) {
                String hashedPassword = userService.hashPassword(password);
                if (dto.getHashedPassword().equals(hashedPassword)) {

                    System.out.println("You can log in!");

                    String UName = dto.getName() + "!";

                    loggedInUserName = UName;

                    showMainMenu(actionEvent);

                } else {
                    new Alert(Alert.AlertType.WARNING, "Incorrect Password!").show();
                }
            } else {
                    new Alert(Alert.AlertType.WARNING, "User not found!").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void showMainMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setLoggedInUser(loggedInUserName);

        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.setTitle("Main Menu Form");
        newStage.centerOnScreen();

        currentStage.close();
        newStage.show();
    }

    @FXML
    public void newUserRegisterHyperlink(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/UserForm.fxml"));
        Scene scene = new Scene(root);


        Node source = (Node) actionEvent.getSource();


        Stage currentStage = (Stage) source.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("User Registration Form");
        newStage.centerOnScreen();


        currentStage.close();

        newStage.show();
    }
}
