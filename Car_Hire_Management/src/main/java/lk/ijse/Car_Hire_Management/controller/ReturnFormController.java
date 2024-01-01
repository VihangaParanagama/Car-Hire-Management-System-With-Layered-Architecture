package lk.ijse.Car_Hire_Management.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Car_Hire_Management.dto.OverDueRentsTableDto;
import lk.ijse.Car_Hire_Management.dto.RentDto;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.CarService;
import lk.ijse.Car_Hire_Management.service.custom.CustomerService;
import lk.ijse.Car_Hire_Management.service.custom.RentService;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;
import lombok.SneakyThrows;


import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ReturnFormController implements Initializable {

    private RentService rentService;
    private CarService carService;

    private CustomerService customerService;

    @FXML
    private TableColumn<OverDueRentsTableDto, Double> BalanceColumn;

    @FXML
    private TableColumn<OverDueRentsTableDto, String> CarColumn;

    @FXML
    private TableColumn<OverDueRentsTableDto, Integer> CarIDColumn;

    @FXML
    private TableColumn<OverDueRentsTableDto, Integer> CustomerIDColumn;

    @FXML
    private TableColumn<OverDueRentsTableDto, String> CustomerNameColumn;

    @FXML
    private TableColumn<OverDueRentsTableDto, LocalDate> DueDateColun;

    @FXML
    private TableView<OverDueRentsTableDto> OverDueTable;

    @FXML
    private TableColumn<OverDueRentsTableDto, Integer> RentIDColumn;

    @FXML
    private ComboBox<Integer> rentIdComboBox;

    @FXML
    private Label balanceText;

    @FXML
    private Label carText;

    @FXML
    private Label customerNameText;

    @FXML
    private Label overDueChargesText;

    @FXML
    private Label grandTotalText;

    @FXML
    private Label returnDateText;

    private String selectedRentId;

    private Double Extracharges;

    private Double balance;

    private Double grandTotal;

    private Date toDate;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rentService = ServiceFactory.getInstance().getService(ServiceType.RENT);

        carService = ServiceFactory.getInstance().getService(ServiceType.CAR);

        customerService = ServiceFactory.getInstance().getService(ServiceType.CUSTOMER);

        loadAllRentIDsInaRent();

        //LoadAllOverDueRents
        RentIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        CarIDColumn.setCellValueFactory(new PropertyValueFactory<>("CarId"));
        CarColumn.setCellValueFactory(new PropertyValueFactory<>("Car"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerId"));
        CustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        DueDateColun.setCellValueFactory(new PropertyValueFactory<>("to_date"));
        BalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));




        LoadAllOverDueRents();


    }

    private void loadAllRentIDsInaRent() {
        rentIdComboBox.getItems().clear();
        ObservableList<Integer> allRentIds = FXCollections.observableArrayList();


        List<Integer> rentIds = rentService.getAllRentIdsInARent();
        allRentIds.addAll(rentIds);

        rentIdComboBox.setItems(allRentIds);

        System.out.println("All Rent IDs: " + allRentIds);
    }

    private void LoadAllOverDueRents() {
        List<RentDto> rentDtoList = rentService.getAllOverdueRents();

        ObservableList<OverDueRentsTableDto> overDueRentTableModelDtoList = FXCollections.observableArrayList();

        for (RentDto rentDto : rentDtoList) {
            OverDueRentsTableDto overDueRentsTableDto = new OverDueRentsTableDto(

                    rentDto.getId(),
                    rentDto.getCarId(),
                    rentDto.getCustomerId(),
                    rentDto.getTo_date(),
                    rentDto.getBalance(),
                    rentDto.getCustomer_name(),
                    rentDto.getCar()
            );

            overDueRentTableModelDtoList.add(overDueRentsTableDto);
        }

        OverDueTable.setItems(overDueRentTableModelDtoList);
    }

    @FXML
    void getBalannceAcordingToRentId(ActionEvent event) {
        try {
            selectedRentId = String.valueOf(rentIdComboBox.getValue());

            RentDto selectedRent = null;
            List<RentDto> rentIds = rentService.getAllRent();
            for (RentDto rentDto : rentIds) {
                if (String.valueOf(rentDto.getId()).equals(selectedRentId)) {
                    selectedRent = rentDto;
                    break;
                }
            }

            if (selectedRent != null) {
                String CustomerName = selectedRent.getCustomer_name();
                customerNameText.setText(CustomerName);

                String Car = selectedRent.getCar();
                carText.setText(Car);

                returnDateText.setText(String.valueOf(getCurrentDate()));

                balance = selectedRent.getBalance();
                balanceText.setText(String.valueOf(balance));

                toDate = selectedRent.getTo_date();

                if (hasPassedDueDate(toDate)) {

                    Double extradates = rentService.calculateExtraDates(toDate,getCurrentDate());

                    Extracharges = rentService.calculateChargesForExtraDates(extradates, Integer.valueOf(selectedRentId));
                    overDueChargesText.setText("Rs. "+ Extracharges);
                    grandTotal = rentService.calculateGrandTotal(Extracharges,balance);
                    grandTotalText.setText(String.valueOf(grandTotal));
                    System.out.println("grandTotal"+grandTotal);
                } else {
                    Double Extracharges = 0.0;
                    overDueChargesText.setText("Rs. 0");
                    grandTotal = rentService.calculateGrandTotal(Extracharges,balance);
                    grandTotalText.setText(String.valueOf(grandTotal));
                    System.out.println("grandTotal"+grandTotal);
                }


            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private LocalDate getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(currentDate.format(formatter));
    }

    private boolean hasPassedDueDate(Date toDate) {
        LocalDate dueDate = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(dueDate);
    }

    @FXML
    void returnACarButton(ActionEvent event) {
        try {
            if (selectedRentId != null) {
                Integer rentId = Integer.valueOf(selectedRentId);

                RentDto rentDto = new RentDto();
                rentDto.setId(selectedRentId);
                rentDto.setGrandTotal(Double.valueOf(grandTotalText.getText()));


                String isReturn = "Yes";
                String availability = "Yes";

                boolean isSaved = rentService.updateIsReturnByRentId(rentId, isReturn);
                boolean ispdated = carService.updateAvailabilityByRentId(rentId, availability);
                boolean isSave = rentService.updateRent(rentDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Return Successfully Added").show();
                    if (ispdated) {
                        System.out.println("************Availability Updated******");
                        if (isSave) {
                            clearFields();
                            loadAllRentIDsInaRent();
                            LoadAllOverDueRents();
                            System.out.println("Grand Total Updated");
                        }else{
                            System.out.println("Can't Update Grand Total");
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Can't Update New Availability").show();
                    }
                }
            } else {

                System.err.println("Selected rent ID is null");
            }
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
    }


    public void clearFields() {
        rentIdComboBox.setValue(null);
        balanceText.setText("");
        carText.setText("");
        customerNameText.setText("");
        overDueChargesText.setText("");
        grandTotalText.setText("");
        returnDateText.setText("");
    }


}
