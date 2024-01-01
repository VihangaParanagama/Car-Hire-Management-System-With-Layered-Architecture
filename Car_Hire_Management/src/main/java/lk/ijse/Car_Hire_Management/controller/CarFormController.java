package lk.ijse.Car_Hire_Management.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.Car_Hire_Management.dto.*;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.CarCategoryService;
import lk.ijse.Car_Hire_Management.service.custom.CarService;
import lk.ijse.Car_Hire_Management.service.custom.RentService;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class CarFormController implements Initializable {

    private CarService carService;
    private CarCategoryService carCategoryService;
    private RentService rentService;


    @FXML
    private TextField vehicleNameText;

    @FXML
    private TextField carBrandText;

    @FXML
    private ComboBox<String> carCategoryComboBox;

    @FXML
    private Label  carIdText;

    @FXML
    private TextField carModelText;

    @FXML
    private TextField carPricePerDayText;

    @FXML
    private TextField carYearText;

    @FXML
    private RadioButton availabilityNo;

    @FXML
    private RadioButton availabilityYes;

    @FXML
    private TableColumn<CarTableModelDto, Integer> carIdColumn;

    @FXML
    private TableColumn<CarTableModelDto, String> yearColumn;

    @FXML
    private TableColumn<CarTableModelDto, String> modelColumn;

    @FXML
    private TableColumn<CarTableModelDto, String> carNumberColumn;

    @FXML
    private TableColumn<CarTableModelDto, Double> pricePerDayColumn;

    @FXML
    private TableView<CarTableModelDto> carTable;

    @FXML
    private TableColumn<CarTableModelDto, String> brandColumn;

    @FXML
    private TableColumn<CarTableModelDto, String> categoryColumn;

    @FXML
    private TableColumn<CarTableModelDto, String> availabilityColumn;

    @FXML
    private TextField carNumberText;


    private ToggleGroup toggleGroup = new ToggleGroup();
    private MouseEvent event;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            carService = ServiceFactory.getInstance().getService(ServiceType.CAR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            carCategoryService = ServiceFactory.getInstance().getService(ServiceType.CAR_CATEGORY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        rentService = ServiceFactory.getInstance().getService(ServiceType.RENT);

        generateAndSetCarId();



        availabilityYes.setToggleGroup(toggleGroup);
        availabilityNo.setToggleGroup(toggleGroup);


        carIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        pricePerDayColumn.setCellValueFactory(new PropertyValueFactory<>("Price_per_day"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle_category"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        carNumberColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle_number"));



        loadAllCars();
    }

    private void generateAndSetCarId() {
        try {
            Integer lastCarId = carService.getLastCarId();
            int newCarId = (lastCarId != null) ? lastCarId + 1 : 1;

            carIdText.setText(String.valueOf(newCarId));
            loadCarCategoriesToComboBox();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void loadCarCategoriesToComboBox() {
        try {
            carCategoryComboBox.getItems().clear();
            ObservableList<String> carCategories = FXCollections.observableArrayList();

            List<CarCategoryDto> categories = carCategoryService.getAllCategory();

            for (CarCategoryDto category : categories) {
                carCategories.add(category.getName());
            }

            carCategoryComboBox.setItems(carCategories);

            System.out.println(carCategories);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }


    private void loadAllCars() throws SQLException {
        try {
            List<CarDto> carDtoList = carService.getAllCar();

            ObservableList<CarTableModelDto> carTableModelList = FXCollections.observableArrayList();

            for (CarDto carDto : carDtoList) {
                CarTableModelDto carTableModel = new CarTableModelDto(
                        carDto.getId(),
                        carDto.getBrand(),
                        carDto.getModel(),
                        carDto.getYear(),
                        carDto.getCar_number(),
                        carDto.getPrice_per_day(),
                        carDto.getAvailability(),
                        carDto.getVehicle_category()
                );

                carTableModelList.add(carTableModel);
            }

            carTable.setItems(carTableModelList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void carTableOnClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            setCars();
        }
    }

    private void setCars() {
        CarTableModelDto  carTableModelDto = carTable.getSelectionModel().getSelectedItem();

        carIdText.setText(String.valueOf(carTableModelDto.getId()));
        carBrandText.setText(carTableModelDto.getBrand());
        carModelText.setText(carTableModelDto.getModel());
        carYearText.setText(carTableModelDto.getYear());
        carNumberText.setText(carTableModelDto.getVehicle_number());
        carPricePerDayText.setText(String.valueOf(carTableModelDto.getPrice_per_day()));;
        carCategoryComboBox.setValue(carTableModelDto.getVehicle_category());

        if ("Yes".equals(carTableModelDto.getAvailability())) {
            availabilityYes.setSelected(true);
        } else {
            availabilityNo.setSelected(true);
        }
    }


    public void carCategoryOnAction(ActionEvent actionEvent) {
        String selectedCarCategory = carCategoryComboBox.getValue();
        vehicleNameText.setText(selectedCarCategory);

        if (selectedCarCategory != null) {
            System.out.println("Selected Car Category: " + selectedCarCategory);

            CarCategoryDto selectedCategoryDto = carCategoryService.getCarCategoryByName(selectedCarCategory);
            if (selectedCategoryDto != null) {
                Integer categoryId = selectedCategoryDto.getId();
                System.out.println("Selected Car Category ID: " + categoryId);


            } else {
                System.out.println("Error: Selected Car Category not found");
            }
        } else {
            System.out.println("No Car Category selected");
        }


    }

    public void addCar() throws SQLException {

        String selectedCarCategory = carCategoryComboBox.getValue();

        if (selectedCarCategory != null) {
            System.out.println("Selected Car Category: " + selectedCarCategory);


            CarCategoryDto selectedCategoryDto = carCategoryService.getCarCategoryByName(selectedCarCategory);
            if (selectedCategoryDto != null) {
                Integer categoryId = selectedCategoryDto.getId();
                System.out.println("Selected Car Category ID: " + categoryId);


                try{
                    String newCarNumber = carNumberText.getText().trim();
                    List<CarDto> carDtos = carService.getAllCar();
                    boolean carNumberExists = carDtos.stream()
                            .anyMatch(carDto -> carDto.getCar_number().trim().equals(newCarNumber));
                    if(carIdText.getText().isEmpty() || carBrandText.getText().isEmpty() || carModelText.getText().isEmpty()
                            || carYearText.getText().isEmpty() || carNumberText.getText().isEmpty()
                            || carPricePerDayText.getText().isEmpty() || vehicleNameText.getText().isEmpty()
                            || carCategoryComboBox.getValue() == null
                            || (!availabilityYes.isSelected() && !availabilityNo.isSelected())){
                        new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();
                    } else if (carNumberExists) {
                        new Alert(Alert.AlertType.WARNING, "Car Number already exists. Please enter another one.").show();
                    } else{
                        if (!carBrandText.getText().matches("^[A-Za-z0-9\\s]*$")) {
                            new Alert(Alert.AlertType.ERROR, "Invalid Car Brand").show();
                        } else if (!carModelText.getText().matches("^[A-Za-z0-9\\s]*$")) {
                            new Alert(Alert.AlertType.ERROR, "Invalid Car Model").show();
                        } else if (!carYearText.getText().matches("^\\d{4}$")) {
                            new Alert(Alert.AlertType.ERROR, "Invalid Year (should be a 4-digit number)").show();
                        } else if (!carNumberText.getText().matches("^[A-Z]{2,3}-\\d{4}$")) {
                            new Alert(Alert.AlertType.ERROR, "Invalid Car Number").show();
                        } else if (!carPricePerDayText.getText().matches("\\b\\d+(\\.\\d+)?\\b")) {
                            new Alert(Alert.AlertType.ERROR, "Invalid Price Per Day").show();
                        } else {
                            RadioButton selectedRadioBtn = (RadioButton) toggleGroup.getSelectedToggle();
                            String selectedAvailability = selectedRadioBtn.getText();

                            CarDto carDto = new CarDto();
                            carDto.setId(Integer.valueOf(carIdText.getText()));
                            carDto.setBrand(carBrandText.getText());
                            carDto.setModel(carModelText.getText());
                            carDto.setYear(carYearText.getText());
                            carDto.setVehicle_category(vehicleNameText.getText());
                            carDto.setCar_number(carNumberText.getText());
                            carDto.setPrice_per_day(Double.valueOf(carPricePerDayText.getText()));
                            carDto.setAvailability(selectedAvailability);
                            carDto.setCarCategoryId(categoryId);


                            boolean isSaved = carService.saveCar(carDto);
                            if (isSaved) {
                                loadAllCars();
                                resetFields();
                                new Alert(Alert.AlertType.INFORMATION, "Saved Car successfully!").show();

                            }

                        }
                    }

                }catch (Exception exception){
                    new Alert(Alert.AlertType.ERROR, "An error occurred Car Update: " + exception.getMessage()).show();
                    throw exception;
                }

            } else {
                System.out.println("Error: Selected Car Category not found");
            }
        } else {
            System.out.println("No Car Category selected");
        }

    }


    public void updateCar(ActionEvent actionEvent) throws SQLException {

        String result = carService.checkCarInRentById(Integer.valueOf(carIdText.getText()));
        if ("Yes".equals(result)) {
            String selectedCarCategory = carCategoryComboBox.getValue();

            String selectedCarId = carIdText.getText();

            if (selectedCarCategory != null && selectedCarId != null) {
                System.out.println("Selected Car Category: " + selectedCarCategory );


                CarCategoryDto selectedCategoryDto = carCategoryService.getCarCategoryByName(selectedCarCategory);
                if (selectedCategoryDto != null) {
                    Integer categoryId = selectedCategoryDto.getId();
                    System.out.println("Selected Car Category ID: " + categoryId+ "    \nSelected Car Id:"+selectedCarId);

                    try{
                        if(carIdText.getText().isEmpty() || carCategoryComboBox == null || carBrandText.getText().isEmpty() || carModelText.getText().isEmpty()
                                || carYearText.getText().isEmpty() || carNumberText.getText().isEmpty()
                                || carPricePerDayText.getText().isEmpty() || vehicleNameText.getText().isEmpty()
                                || carCategoryComboBox.getValue() == null
                                || (!availabilityYes.isSelected() && !availabilityNo.isSelected())){
                            new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();

                        }else{
                            if (!carBrandText.getText().matches("^[A-Za-z0-9\\s]*$")) {
                                new Alert(Alert.AlertType.ERROR, "Invalid Car Brand").show();
                            } else if (!carModelText.getText().matches("^[A-Za-z0-9\\s]*$")) {
                                new Alert(Alert.AlertType.ERROR, "Invalid Car Model").show();
                            } else if (!carYearText.getText().matches("^\\d{4}$")) {
                                new Alert(Alert.AlertType.ERROR, "Invalid Year (should be a 4-digit number)").show();
                            } else if (!carNumberText.getText().matches("^[A-Z]{0,3}-\\d{0,4}$")) {
                                new Alert(Alert.AlertType.ERROR, "Invalid Car Number").show();
                            } else if (!carPricePerDayText.getText().matches("\\b\\d+(\\.\\d+)?\\b")) {
                                new Alert(Alert.AlertType.ERROR, "Invalid Price Per Day").show();
                            } else {
                                RadioButton selectedRadioBtn = (RadioButton) toggleGroup.getSelectedToggle();
                                String selectedAvailability = selectedRadioBtn.getText();

                                CarDto carDto = new CarDto();
                                carDto.setId(Integer.valueOf(carIdText.getText()));;
                                carDto.setBrand(carBrandText.getText());
                                carDto.setModel(carModelText.getText());
                                carDto.setYear(carYearText.getText());
                                carDto.setVehicle_category(vehicleNameText.getText());
                                carDto.setCar_number(carNumberText.getText());
                                carDto.setPrice_per_day(Double.valueOf(carPricePerDayText.getText()));
                                carDto.setAvailability(selectedAvailability);
                                carDto.setCarCategoryId(categoryId);
                                carDto.setVehicle_category(vehicleNameText.getText());

                                if(carDto != null){
                                    boolean isUpdate = carService.updateCar(carDto);
                                    if (isUpdate) {
                                        carCategoryComboBox.getItems().clear();
                                        loadAllCars();
                                        resetFields();

                                        new Alert(Alert.AlertType.INFORMATION, "Updated Car successfully!").show();

                                    }else{
                                        new Alert(Alert.AlertType.WARNING, "Can't Update").show();
                                    }

                                }else{
                                    new Alert(Alert.AlertType.WARNING, "Carcategory Dto null").show();
                                }
                            }
                        }
                    }
                    catch (Exception exception){
                        throw exception;
                    }

                } else {
                    System.out.println("Error: Selected Car Category not found");
                }
            } else {
                new Alert(Alert.AlertType.WARNING,"No Car Category selected").show();
            }
        }else if("No".equals(result)){
        new Alert(Alert.AlertType.ERROR, "It cannot be update until the Car in a rent").show();
    }


    }

    public void deleteCar() {
        Integer carId = Integer.valueOf(carIdText.getText());
        try {
            String result = carService.checkCarInRentById(Integer.valueOf(carIdText.getText()));
            System.out.println("result"+result);
            if ("Yes".equals(result)) {
                    String result1 = rentService.checkCarInRentById(carId);
                    if ("Yes".equals(result1)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setContentText("If you delete this car,the related rent information will also be deleted. Are You Sure?");


                        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

                        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                        Optional<ButtonType> decision = alert.showAndWait();
                        if (decision.isPresent() && decision.get() == buttonTypeYes) {
                            boolean isDeleted = carService.deleteCar(carId);
                            if (isDeleted) {
                                resetFields();
                                carCategoryComboBox.getItems().clear();
                                generateAndSetCarId();
                                loadAllCars();
                                new Alert(Alert.AlertType.INFORMATION, "Deleted Car Successfully!").show();
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Delete failed").show();
                            }
                        }
                    }else if ("No".equals(result1)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setContentText("Are You sure ,You want to delete this car?");


                        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

                        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                        Optional<ButtonType> decision = alert.showAndWait();
                        if (decision.isPresent() && decision.get() == buttonTypeYes) {
                            boolean isDeleted = carService.deleteCar(carId);
                            if (isDeleted) {
                                resetFields();
                                carCategoryComboBox.getItems().clear();
                                generateAndSetCarId();
                                loadAllCars();
                                new Alert(Alert.AlertType.INFORMATION, "Deleted Car Successfully!").show();
                            } else {
                                new Alert(Alert.AlertType.ERROR, "Delete failed").show();
                            }
                        }
                    }


            }else if("No".equals(result)){
                new Alert(Alert.AlertType.ERROR, "It cannot be deleted until the Car in a rent").show();
            }



        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).show();
        }
    }

    public void resetFields() {
        carIdText.setText("");
        carBrandText.clear();
        carNumberText.clear();
        carModelText.clear();
        carYearText.clear();
        vehicleNameText.clear();
        carPricePerDayText.clear();
        carCategoryComboBox.getItems().clear();
        toggleGroup.selectToggle(null);
        generateAndSetCarId();
    }
}