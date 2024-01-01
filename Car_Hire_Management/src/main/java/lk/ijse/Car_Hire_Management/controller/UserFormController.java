package lk.ijse.Car_Hire_Management.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.Car_Hire_Management.dto.UserDto;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.UserService;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserFormController implements Initializable {
    private UserService userService;

    @FXML
    private TextField confirmPasswordText;

    @FXML
    private TextField emailText;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private TextField mobileText;

    @FXML
    private TextField nICNumberText;

    @FXML
    private TextField passwordText;

    @FXML
    private Label userIdText;


    @FXML
    private TextField userNameText;

    private static final String NAME_REGEX = "^[a-zA-Z]+$";
    private static final String NIC_REGEX = "^[0-9]{8}([Vv])?$|^[0-9]{12}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String MOBILE_REGEX = "^0\\d{9}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]+$";


    private boolean validateName(String name) {
        return name.matches(NAME_REGEX);
    }

    private boolean validateNIC(String nic) {
        return nic.matches(NIC_REGEX);
    }

    private boolean validateEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    private boolean validateMobile(String mobile) {
        return mobile.matches(MOBILE_REGEX);
    }

    private boolean validateUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            userService = ServiceFactory.getInstance().getService(ServiceType.USER);
            generateAndSetUserId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void generateAndSetUserId() {
        try {
            Integer lastUserId = userService.getLastUserId();


            int newUserId = (lastUserId != null) ? lastUserId + 1 : 1;


            userIdText.setText(String.valueOf(newUserId));


        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void registerUser(ActionEvent event) {
        if (!userIdText.getText().isEmpty()) {
            try {
                Integer userId = Integer.valueOf(userIdText.getText());

                String name = firstNameText.getText().trim() + "  "+lastNameText.getText().trim();
                String email = emailText.getText().trim();
                String mobile = mobileText.getText().trim();
                String username = userNameText.getText().trim();
                String password = passwordText.getText().trim();
                String confirmPassword = confirmPasswordText.getText().trim();
                String NIC = nICNumberText.getText().trim();

                String newUserName = userNameText.getText().trim();

                List<UserDto> userDtos = userService.getAllUsers();
                boolean userNameExists = userDtos.stream()
                        .anyMatch(userDto -> userDto.getUsername().trim().equals(newUserName));

                if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || username.isEmpty() || password.isEmpty() || NIC.isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();
                } else if (userNameExists) {
                    new Alert(Alert.AlertType.WARNING, "Username already exists. Please enter another one.").show();
                } else {
                    if (!validateName(firstNameText.getText()) || !validateName(lastNameText.getText())) {
                        new Alert(Alert.AlertType.WARNING, "First name and last name must contain only letters.").show();
                    } else if (!validateUsername(username)) {
                        new Alert(Alert.AlertType.WARNING, "Invalid username. It must consist of letters, numbers, and underscores.").show();
                    } else if (!validateNIC(NIC)) {
                        new Alert(Alert.AlertType.WARNING, "NIC number must be 8 digits with an optional 'V' or 'v' at the end, or 12 digits without letters.").show();
                    } else if (!validateEmail(email)) {
                        new Alert(Alert.AlertType.WARNING, "Invalid email address.").show();
                    } else if (!validateMobile(mobile)) {
                        new Alert(Alert.AlertType.WARNING, "Mobile number must contain only 10 digits and start with 0.").show();
                    } else {
                        if (password.equals(confirmPassword)) {
                            String hashedPassword = userService.hashPassword(password);

                            UserDto userDto = new UserDto();
                            userDto.setId(userId);
                            userDto.setName(name);
                            userDto.setEmail(email);
                            userDto.setMobile(mobile);
                            userDto.setUsername(username);
                            userDto.setHashedPassword(hashedPassword);
                            userDto.setNIC(NIC);

                            try {
                                boolean saved = userService.saveUser(userDto);

                                if (saved) {
                                    new Alert(Alert.AlertType.INFORMATION, "User added successfully").show();
                                    clearFields();
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Failed to add user").show();
                                }

                            } catch (Exception e) {
                                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                                e.printStackTrace();
                            }
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Passwords Didn't Match").show();
                        }
                    }
                }

            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid user ID format").show();
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Only one user can be added when logging in once").show();
        }
    }






    @FXML
    public void backToLoginForm(ActionEvent actionEvent) throws IOException {
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

    @FXML
    private void clearFields() {
        confirmPasswordText.clear();
        emailText.clear();
        firstNameText.clear();
        lastNameText.clear();
        mobileText.clear();
        nICNumberText.clear();
        passwordText.clear();
        userIdText.setText("");
        userNameText.clear();
    }

}
