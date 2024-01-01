package lk.ijse.Car_Hire_Management.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.Car_Hire_Management.dto.CarCategoryDto;
import lk.ijse.Car_Hire_Management.dto.CarCategoryTableModel;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.CarCategoryService;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CarCategoryController implements Initializable {

    private CarCategoryService carCategoryService;

    @FXML
    private TableView<CarCategoryTableModel> carCategoryTable;

    @FXML
    private TableColumn<CarCategoryTableModel, String> categoryID;

    @FXML
    private TableColumn<CarCategoryTableModel, String> dailyRentalRate;

    @FXML
    private TableColumn<CarCategoryTableModel, String> categoryName;

    @FXML
    private Label categoryIdText;

    @FXML
    private TextField categoryNameText;

    @FXML
    private TextField dailyRentalRateText;

    private static final String DAILYRENT_REGEX = "^[0-9]+(?:\\.[0-9]+)?$";

    private boolean validateDailyRent(String dailyRent) {
        return dailyRent.matches(DAILYRENT_REGEX);
    }

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carCategoryService = ServiceFactory.getInstance().getService(ServiceType.CAR_CATEGORY);
        System.out.println("Car Category Form Loaded");

        categoryID.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        dailyRentalRate.setCellValueFactory(new PropertyValueFactory<>("dailyRentalRate"));

        generateAndSetCarCategoryId();
        loadAllCarCategories();
    }

    private void generateAndSetCarCategoryId() {
        try {
            Integer lastCarCategoryId = carCategoryService.getLastCarCategoryId();
            int newCarCategoryId = (lastCarCategoryId != null) ? lastCarCategoryId + 1 : 1;
            categoryIdText.setText(String.valueOf(newCarCategoryId));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void categoryTableMouseClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            searchCarCategory();
        }
    }

    @FXML
    void registerNewCarCategory() {
        String dailyRent = dailyRentalRateText.getText().trim();
        if(categoryNameText.getText().isEmpty() || dailyRent.isEmpty()){
            new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();
        } else {
            if(!categoryNameText.getText().matches("^[A-Za-z]*$")){
                new Alert(Alert.AlertType.ERROR, "Invalid Car Category Name").show();
            } else if (!validateDailyRent(dailyRent)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Daily Rental Rate Value").show();
            } else {
                String newCarCategory = categoryNameText.getText().trim().toLowerCase();

                List<CarCategoryDto> carCategoryDtos = carCategoryService.getAllCategory();
                boolean categoryExists = carCategoryDtos.stream()
                        .anyMatch(carCategoryDto1 -> carCategoryDto1.getName().trim().toLowerCase().equals(newCarCategory));

                if (categoryExists) {
                    new Alert(Alert.AlertType.WARNING, "Car Category already exists!").show();
                } else {
                    CarCategoryDto carCategoryDto = new CarCategoryDto();

                    carCategoryDto.setId(Integer.valueOf(categoryIdText.getText()));
                    carCategoryDto.setName(categoryNameText.getText());
                    carCategoryDto.setDailyRentalRate(Double.valueOf(dailyRentalRateText.getText()));

                    boolean isSaved = carCategoryService.saveCategory(carCategoryDto);

                    if (isSaved) {
                        new Alert(Alert.AlertType.INFORMATION, "Saved Car Category successfully!").show();
                        clear();
                        generateAndSetCarCategoryId();
                        loadAllCarCategories();
                    }
                }
            }
        }

    }

    private void searchCarCategory() {
        CarCategoryTableModel carCategoryTableModel = carCategoryTable.getSelectionModel().getSelectedItem();

        categoryIdText.setText(String.valueOf(Integer.valueOf(carCategoryTableModel.getId())));
        categoryNameText.setText(carCategoryTableModel.getName());
        dailyRentalRateText.setText(String.valueOf(carCategoryTableModel.getDailyRentalRate()));
    }

    private void loadAllCarCategories() {
        List<CarCategoryDto> carCategoryDtoList = carCategoryService.getAllCategory();
        ObservableList<CarCategoryTableModel> carCategoryTableModelList = FXCollections.observableArrayList();

        for (CarCategoryDto carCategoryDto : carCategoryDtoList) {
            CarCategoryTableModel carCategoryTableModel = new CarCategoryTableModel(
                    carCategoryDto.getId(),
                    carCategoryDto.getName(),
                    carCategoryDto.getDailyRentalRate()
            );

            carCategoryTableModelList.add(carCategoryTableModel);
        }

        carCategoryTable.setItems(carCategoryTableModelList);
    }

    @FXML
    void clearFields(MouseEvent event) {
        clear();
        generateAndSetCarCategoryId();
    }

    private void clear() {
        categoryIdText.setText("");
        categoryNameText.setText("");
        dailyRentalRateText.setText("");
    }

    @FXML
    void updateCarCategory(ActionEvent actionEvent) {
        if(categoryNameText.getText().isEmpty() || dailyRentalRate.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();
        }else{
            if(!categoryNameText.getText().matches("^[A-Za-z]*$")){
                new Alert(Alert.AlertType.ERROR, "Invalid Car Category Name").show();
            } else if (!dailyRentalRate.getText().trim().matches("^[0-9]+(?:\\.[0-9]+)?$")) {
                new Alert(Alert.AlertType.ERROR, "Invalid Daily Rental Rate Value").show();
            }else {
                String newCarCategory = categoryNameText.getText().trim().toLowerCase();

                List<CarCategoryDto> carCategoryDtos = carCategoryService.getAllCategory();
                boolean categoryExists = carCategoryDtos.stream()
                        .anyMatch(carCategoryDto1 -> carCategoryDto1.getName().trim().toLowerCase().equals(newCarCategory));

                if (categoryExists) {
                    new Alert(Alert.AlertType.WARNING, "Car Category already exists!").show();
                } else {
                    CarCategoryDto carCategoryDto = new CarCategoryDto();

                    carCategoryDto.setId(Integer.valueOf(categoryIdText.getText()));
                    carCategoryDto.setName(categoryNameText.getText());
                    carCategoryDto.setDailyRentalRate(Double.valueOf(dailyRentalRateText.getText()));

                    boolean isUpdated = carCategoryService.UpdateCAtegory(carCategoryDto);

                    if (isUpdated) {
                        new Alert(Alert.AlertType.INFORMATION, "Updated Car Category successfully!").show();
                        clear();
                        generateAndSetCarCategoryId();
                        loadAllCarCategories();
                    }
                }
            }
        }
    }
}
