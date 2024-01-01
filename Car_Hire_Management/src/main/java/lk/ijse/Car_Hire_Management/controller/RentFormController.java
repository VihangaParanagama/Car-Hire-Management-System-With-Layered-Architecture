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
    import lk.ijse.Car_Hire_Management.entity.CustomerEntity;
    import lk.ijse.Car_Hire_Management.service.ServiceFactory;
    import lk.ijse.Car_Hire_Management.service.custom.CarCategoryService;
    import lk.ijse.Car_Hire_Management.service.custom.CarService;
    import lk.ijse.Car_Hire_Management.service.custom.CustomerService;
    import lk.ijse.Car_Hire_Management.service.custom.RentService;
    import lk.ijse.Car_Hire_Management.service.util.ServiceType;
    import lombok.SneakyThrows;


    import java.net.URL;
    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.List;
    import java.util.Optional;
    import java.util.ResourceBundle;




    public class RentFormController implements Initializable {

        private RentService rentService;
        private CarService carService;
        private CustomerService customerService;
        private CarCategoryService carCategoryService;

        @FXML
        private Label perDayRentText;

        @FXML
        private Label carNumberText;

        @FXML
        private DatePicker ToDate;

        @FXML
        private Label balanceText;

        @FXML
        private Label totalText;

        @FXML
        private TextField advancedPaymentText;

        @FXML
        private DatePicker fromDate;

        @FXML
        private Label  rentIdText;

        @FXML
        private ComboBox<Integer> carIdComboBox;

        @FXML
        private ComboBox<Integer> customerIdComboBox;

        @FXML
        private Label carText;

        @FXML
        private TableColumn<RentTableModelDto, Double> balanceColumn;

        @FXML
        private TableColumn<RentTableModelDto, String> customeNameColumn;

        @FXML
        private TableColumn<RentTableModelDto, Integer> rentIdColumn;

        @FXML
        private TableColumn<RentTableModelDto, String> rentedCarColumn;

        @FXML
        private TableColumn<RentTableModelDto, LocalDate> rentedDateColumn;

        @FXML
        private TableColumn<RentTableModelDto, LocalDate> returnDateColumn;

        @FXML
        private TableColumn<RentTableModelDto, String> returnStatusColumn;

        @FXML
        private TableColumn<RentTableModelDto, Integer> carIdColumn;

        @FXML
        private TableView<RentTableModelDto> rentTable;

        @FXML
        private Label customerNICText;

        @FXML
        private Label customerNameText;

        @FXML
        private ComboBox<String> carCategoryComboBox;

        private Double DailyRentalRate;

        private String car;


        @SneakyThrows
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            carService = ServiceFactory.getInstance().getService(ServiceType.CAR);
            rentService = ServiceFactory.getInstance().getService(ServiceType.RENT);
            customerService = ServiceFactory.getInstance().getService(ServiceType.CUSTOMER);
            carCategoryService = ServiceFactory.getInstance().getService(ServiceType.CAR_CATEGORY);

            //Load All Car Ctegories
            carCategoryComboBox.getItems().clear();
            ObservableList<String> Categories = FXCollections.observableArrayList();

            List<CarCategoryDto> carCategoriess = carCategoryService.getAllCategory();

            for (CarCategoryDto carCategoryDto : carCategoriess) {
                Categories.add(carCategoryDto.getName());
            }

            carCategoryComboBox.setItems(Categories);

            System.out.println(Categories);

            generateAndSetRentId();
            loadAllCustomersIds ();

            //LoadAllRents
            rentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            carIdColumn.setCellValueFactory(new PropertyValueFactory<>("carId"));
            rentedCarColumn.setCellValueFactory(new PropertyValueFactory<>("car"));
            customeNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
            rentedDateColumn.setCellValueFactory(new PropertyValueFactory<>("fromDate"));
            returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("toDate"));
            returnStatusColumn.setCellValueFactory(new PropertyValueFactory<>("isReturn"));


            loadAllRents();
        }

        @FXML
        void rentTableMouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                searchRent();
            }
        }


        @FXML
        void carCategoryOnAction(ActionEvent event) {
            String selectedCarCategory = carCategoryComboBox.getValue();
            System.out.println(selectedCarCategory);

            if (selectedCarCategory != null) {
                try {
                    List<Integer> availableCarIds = carService.getAvailableCarIdsByCarCategory(selectedCarCategory);

                    carIdComboBox.setItems(FXCollections.observableArrayList(availableCarIds));
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                System.out.println("Please select a car category first.");

            }
        }


        private void loadAllCustomersIds() {
            // LoadAllCustomerIDs
            customerIdComboBox.getItems().clear();
            ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();

            // Load all customer IDs
            List<CustomerDto> customerIds = customerService.getAllCus();
            for (CustomerDto customerDto : customerIds) {
                allCustomerIds.add(Integer.valueOf(customerDto.getId()));
            }


            List<Integer> inRentCustomerIds = rentService.getAllCustomerIdsInARent();

            allCustomerIds.removeAll(inRentCustomerIds);

            customerIdComboBox.setItems(allCustomerIds);

            System.out.println("Remaining Customer IDs: " + allCustomerIds);
        }


        private void generateAndSetRentId() {
            try {
                Integer lastRentId = rentService.getLastRentId();


                int newRentId = (lastRentId != null) ? lastRentId + 1 : 1;


                rentIdText.setText(String.valueOf(newRentId));

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                e.printStackTrace();
            }
        }


        public void DeleteRentButton() {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are you sure you want to delete this Rent?");

            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();

            Integer GetRentId = Integer.valueOf(rentIdText.getText());
            System.out.println("Deleted rent Id  "+GetRentId);

            if(result.isPresent() && result.get() == buttonTypeYes){
                if(GetRentId != null) {
                    boolean isDeleted = rentService.deleteRent(GetRentId);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Rent details deleted successfully!").show();
                        loadAllRents();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete rent details.").show();
                    }
                }else{
                    new Alert(Alert.AlertType.ERROR, "Didn't Select Rent Id.").show();
                }
            }
        }

        private void searchRent() {
            RentTableModelDto rentTableModelDto = rentTable.getSelectionModel().getSelectedItem();
            if(rentTableModelDto != null){
                rentIdText.setText(String.valueOf(rentTableModelDto.getId()));
                customerNameText.setText(rentTableModelDto.getCustomerName());
                carText.setText(rentTableModelDto.getCar());
                carIdComboBox.setValue(rentTableModelDto.getCarId());
            }else{
                new Alert(Alert.AlertType.WARNING, "Rent details not Found!").show();
            }


        }

        private void loadAllRents() {
            List<RentDto> rentDtoList = rentService.getAllRent();

            ObservableList<RentTableModelDto> rentTableModelDtoList = FXCollections.observableArrayList();

            for (RentDto rentDto : rentDtoList) {
                RentTableModelDto rentTableModelDto = new RentTableModelDto(
                        rentDto.getId(),
                        rentDto.getCarId(),
                        rentDto.getCar(),
                        rentDto.getCustomer_name(),
                        rentDto.getBalance(),
                        rentDto.getFrom_date(),
                        rentDto.getTo_date(),
                        rentDto.getIs_return()
                );

                rentTableModelDtoList.add(rentTableModelDto);
            }
            System.out.println(rentTableModelDtoList);
            rentTable.setItems(rentTableModelDtoList);
        }


        @FXML
        void carIdOnAction(ActionEvent event) {
            String selectedCarCategory = carCategoryComboBox.getValue();
            String selectedCarId = String.valueOf(carIdComboBox.getValue());

            CarDto selectedCar = null;
            List<CarDto> carIds = carService.getAllCar();
            for (CarDto carDto : carIds) {
                if (String.valueOf(carDto.getId()).equals(selectedCarId)) {
                    selectedCar = carDto;
                    break;
                }
            }

            if (selectedCar != null) {
                car = selectedCar.getBrand() + " " + selectedCar.getModel();
                carText.setText(car);

                String CarNumber = selectedCar.getCar_number();
                carNumberText.setText(CarNumber);

                if (perDayRentText != null) {
                    Double perDayRentCar = carService.getPerdayRentByCarId(Integer.valueOf(selectedCarId));

                    Double PricePerDayCarCategory = carCategoryService.getPerDayRentByCarCategory(selectedCarCategory);
                    if (PricePerDayCarCategory != null) {
                        DailyRentalRate = rentService.calculatePricaPerDay(perDayRentCar, PricePerDayCarCategory);

                        String perDayRentTextValue = String.format("Rs.%.2f", DailyRentalRate);
                        perDayRentText.setText(perDayRentTextValue);
                    } else {

                        System.out.println("PricePerDayCarCategory is null");
                        perDayRentText.setText("");
                    }
                } else {
                    perDayRentText.setText("");
                }
            } else {
                System.out.println("Car not found for ID: " + selectedCarId);
            }
        }



        @FXML
        public void customerIdOnAction(ActionEvent actionEvent) {
            Integer selectedCustomerId = customerIdComboBox.getValue();

            if (selectedCustomerId != null) {
                System.out.println("Selected Customer Id: " + selectedCustomerId);

                CustomerEntity selectedCustomer = customerService.getCustomerById(selectedCustomerId);
                if (selectedCustomer != null) {

                    System.out.println("Selected Customer Name: " + selectedCustomer.getName());
                    customerNameText.setText(selectedCustomer.getName());
                    customerNICText.setText(selectedCustomer.getNic());
                } else {
                    System.out.println("Error: Selected Customer not found");
                }

            }
        }
        @FXML
        void fromDateOnAction(ActionEvent event) {
            LocalDate fromdate = fromDate.getValue();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (fromdate != null && fromdate.isBefore(currentDate)) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Date");
                alert.setContentText("Selected date should not be earlier than today.");
                alert.showAndWait();


                fromDate.setValue(null);
            }
        }

        @FXML
        void toDateOnAction(ActionEvent event) {
            if(!carIdComboBox.getItems().isEmpty() && !carCategoryComboBox.getItems().isEmpty() && !customerIdComboBox.getItems().isEmpty()) {
                LocalDate startDate = fromDate.getValue();
                LocalDate endDate = ToDate.getValue();


                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                if (endDate != null && endDate.isBefore(currentDate)) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Date");
                    alert.setContentText("Selected date should not be earlier than today.");
                    alert.showAndWait();


                    ToDate.setValue(null);
                }else{
                    if (fromDate.getValue() != null && ToDate.getValue() != null ) {

                        boolean isWithinLimit = rentService.rentalDeurationLimit(startDate, endDate);

                        if (isWithinLimit) {


                            System.out.println(fromDate.getValue());
                            System.out.println(ToDate.getValue());
                            System.out.println(DailyRentalRate);

                            Double total = rentService.calculateTotal(DailyRentalRate, startDate, endDate);
                            totalText.setText(String.valueOf(total));

                        } else {

                            System.out.println("Rental duration is limited to a maximum of 30 days");
                            totalText.setText("");
                        }

                    } else {

                    }
                }

            }else{
                new Alert(Alert.AlertType.WARNING, "Please Select CarCategory , CarId and CustomerID").show();
            }

        }

        @FXML
        void calculateBalanceinAdvPayment(ActionEvent event) {
            String doubleRegex = "^(\\d*\\.\\d+|\\d+\\.\\d*|\\d+)$";
            if(advancedPaymentText.getText().matches(doubleRegex)){
                Double advancedPayment = Double.valueOf(advancedPaymentText.getText());
                Double total = Double.valueOf(totalText.getText());

                Double balance = rentService.calculateBalance(total, advancedPayment);
                balanceText.setText(String.valueOf(balance));
            }else{
                new Alert(Alert.AlertType.ERROR, "Invalid Advanced Payment value.").show();
            }

        }

        @FXML
        void AddRentButton(ActionEvent event) {
            Integer selectedCarId = carIdComboBox.getValue();

            if (selectedCarId != null && advancedPaymentText.getText() != null) {
                System.out.println("Selected Car : " + selectedCarId);

                Integer selectedCustomerId = customerIdComboBox.getValue();
                if (selectedCustomerId != null) {
                    System.out.println("Selected Customer Id : " + selectedCustomerId);

                    RentDto rentDto = new RentDto();
                    rentDto.setId(rentIdText.getText());
                    rentDto.setFrom_date(java.sql.Date.valueOf(fromDate.getValue()));
                    rentDto.setTo_date(java.sql.Date.valueOf(ToDate.getValue()));


                    String perDayRentValue = perDayRentText.getText().replace("Rs.", "").trim();
                    rentDto.setPerDay_rent(Double.valueOf(perDayRentValue));

                    String totalValue = totalText.getText().replace("Rs.", "").trim();
                    rentDto.setTotal(Double.valueOf(totalValue));

                    rentDto.setAdvanced_payment(Double.valueOf(advancedPaymentText.getText()));
                    rentDto.setBalance(Double.valueOf(balanceText.getText()));
                    rentDto.setCarId(selectedCarId);
                    rentDto.setCustomerId(selectedCustomerId);
                    rentDto.setCustomer_name(customerNameText.getText());
                    System.out.println("Model + Brand" + carText.getText());
                    rentDto.setCar(carText.getText());
                    System.out.println("Customer name" + customerNameText.getText());

                    String isReturn = "No";
                    rentDto.setIs_return(isReturn);

                    String availability = "No";

                    boolean isSaved = rentService.saveRent(rentDto);
                    boolean isUpdated = carService.updateAvailabilityById(selectedCarId, availability);

                    if (isSaved) {
                        loadAllRents();
                        clearFillds();

                        generateAndSetRentId();
                        loadAllCustomersIds();
                        new Alert(Alert.AlertType.INFORMATION, "Rent Successfully Added").show();
                        if (isUpdated) {
                            System.out.println("***Availability Updated***");
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Can't Update Availability").show();
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Can't Add Rent").show();
                    }

                } else {
                    System.out.println("Customer Id not found");
                }
            } else {

                System.out.println("Car Id not found");
            }
        }


        public void clearFillds() {
            rentIdText.setText("");
            carCategoryComboBox.getSelectionModel().clearSelection();
            carIdComboBox.getSelectionModel().clearSelection();
            perDayRentText.setText("");
            carNumberText.setText("");
            carText.setText("");
            customerIdComboBox.getSelectionModel().clearSelection();
            customerNameText.setText("");
            customerNICText.setText("");
            fromDate.setValue(null);
            ToDate.setValue(null);
            totalText.setText("");
            advancedPaymentText.clear();
            balanceText.setText("");
            generateAndSetRentId();

        }

    }
