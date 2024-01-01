package lk.ijse.Car_Hire_Management.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.Car_Hire_Management.dto.CustomerDto;
import lk.ijse.Car_Hire_Management.dto.CustomerTableModel;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.CustomerService;
import lk.ijse.Car_Hire_Management.service.custom.RentService;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;


import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerFormController {

    private CustomerService customerService;

    private RentService rentService;

    @FXML
    private TextField cAddressText;

    @FXML
    private Label customerIdText;

    @FXML
    private TextField cMobileText;

    @FXML
    private TextField cNameText;

    @FXML
    private TextField cNicText;

    @FXML
    private TableColumn<CustomerTableModel, String> columnAddress;

    @FXML
    private TableColumn<CustomerTableModel, Integer> columnId;

    @FXML
    private TableColumn<CustomerTableModel, String> columnMobile;

    @FXML
    private TableColumn<CustomerTableModel, String> columnName;

    @FXML
    private TableColumn<CustomerTableModel, String> columnNic;

    @FXML
    private TableView<CustomerTableModel> customerTable;

    public void initialize() throws Exception {
        customerService = ServiceFactory.getInstance().getService(ServiceType.CUSTOMER);
        rentService = ServiceFactory.getInstance().getService(ServiceType.RENT);
        System.out.println("Customer Form Loaded");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));

        loadAllCustomers();
        generateAndSetCustomerId();
    }

    private void generateAndSetCustomerId() {
        try {
            Integer lastCustomerId = customerService.getLastCustomerId();
            int newCustomerId = (lastCustomerId != null) ? lastCustomerId + 1 : 1;
            customerIdText.setText(String.valueOf(newCustomerId));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void AddCustomerButton(ActionEvent event) {
        saveCustomer();
    }

    public void DeleteCustomerButton(ActionEvent actionEvent) {
        deleteCustomer();
    }

    public void UpdateCustomerButton(ActionEvent actionEvent) {
        updateCustomer();
    }

    public void customerTableMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            searchCustomer();
        }
    }

    private void saveCustomer() {
        try {
            if (customerIdText.getText().isEmpty() || cNameText.getText().isEmpty() || cAddressText.getText().isEmpty()
                    || cNicText.getText().isEmpty() || cMobileText.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();
            } else {
                    if (!cNameText.getText().matches("^([A-Za-z]+\\s?)+$")) {
                        new Alert(Alert.AlertType.ERROR, "Invalid customer name").show();
                    } else if (!cAddressText.getText().matches("^.*[a-zA-Z0-9]*.*$")) {
                        cAddressText.requestFocus();
                        new Alert(Alert.AlertType.ERROR, "Invalid address").show();
                    } else if (!cMobileText.getText().matches("^[0-9]{10}$")) {
                        new Alert(Alert.AlertType.ERROR, "Invalid contact number").show();
                    } else if (!cNicText.getText().matches("^[0-9]{8}([Vv])?$|^[0-9]{12}$")) {
                        new Alert(Alert.AlertType.WARNING, "or 12 digits without letters.").show();
                        new Alert(Alert.AlertType.WARNING, "NIC number must be 8 digits with an optional 'V' or 'v' at the end").show();
                    } else {
                        CustomerDto customerDto = new CustomerDto();
                        customerDto.setId(Integer.valueOf(customerIdText.getText()));
                        customerDto.setName(cNameText.getText());
                        customerDto.setNic(cNicText.getText());
                        customerDto.setAddress(cAddressText.getText());
                        customerDto.setMobile(cMobileText.getText());

                        boolean isSaved = customerService.saveCus(customerDto);
                        if (isSaved) {
                            new Alert(Alert.AlertType.INFORMATION, "Saved customer successfully!").show();
                            clear();
                            loadAllCustomers();
                            generateAndSetCustomerId();
                        }
                    }
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void updateCustomer() {
        try {
            if (customerIdText.getText().isEmpty() || cNameText.getText().isEmpty() || cAddressText.getText().isEmpty()
                    || cNicText.getText().isEmpty() || cMobileText.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "All fields must be filled").show();
            } else {
                if (!cNameText.getText().matches("^([A-Za-z]+\\s?)+$")) {
                    cNameText.requestFocus();
                    new Alert(Alert.AlertType.ERROR, "Invalid customer name").show();
                } else if (!cAddressText.getText().matches("^.*[a-zA-Z0-9]*.*$")) {
                    cAddressText.requestFocus();
                    new Alert(Alert.AlertType.ERROR, "Invalid address").show();
                } else if (!cMobileText.getText().matches("^[0-9]{10}$")) {
                    new Alert(Alert.AlertType.ERROR, "Invalid contact number").show();
                }else if (!cNicText.getText().matches("^[0-9]{8}([Vv])?$|^[0-9]{12}$")) {
                    new Alert(Alert.AlertType.WARNING, "or 12 digits without letters.").show();
                    new Alert(Alert.AlertType.WARNING, "NIC number must be 8 digits with an optional 'V' or 'v' at the end").show();


                } else {
                    CustomerDto customerDto = new CustomerDto();
                    customerDto.setId(Integer.valueOf(customerIdText.getText()));
                    customerDto.setName(cNameText.getText());
                    customerDto.setNic(cNicText.getText());
                    customerDto.setAddress(cAddressText.getText());
                    customerDto.setMobile(cMobileText.getText());

                    boolean isUpdated = customerService.updateCus(customerDto);

                    if (isUpdated) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer Successfully Updated").show();
                        clear();
                        loadAllCustomers();
                        generateAndSetCustomerId();
                    }
                }
            }
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).show();
        }
    }


    private void deleteCustomer() {
        try {

            String result = rentService.checkCustomerInRentById(Integer.valueOf(customerIdText.getText()));
            System.out.println("result"+result);
            if ("Yes".equals(result)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setContentText("If you delete this customer, the related rent information will also be deleted. Are You Sure?");


                ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Optional<ButtonType> decision = alert.showAndWait();
                if (decision.isPresent() && decision.get() == buttonTypeYes) {
                    String deleteMessage = customerService.deleteCustomer(Integer.valueOf(customerIdText.getText()));
                    if ("Delete successful".equals(deleteMessage)) {
                        clear();
                        generateAndSetCustomerId();
                        loadAllCustomers();
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Customer Successfully!").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Delete failed").show();
                    }
                }
            }else if ("Customer not found in Rent Table".equals(result)) {
                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setTitle("Confirmation Dialog");
                    alert1.setContentText("Are you sure you want to delete this Customer?");


                    ButtonType buttonTypeYes1 = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType buttonTypeNo1 = new ButtonType("No", ButtonBar.ButtonData.NO);

                    alert1.getButtonTypes().setAll(buttonTypeYes1, buttonTypeNo1);

                    Optional<ButtonType> decision = alert1.showAndWait();
                if (decision.isPresent() && decision.get() == buttonTypeYes1) {
                    String deleteMessage = customerService.deleteCustomer(Integer.valueOf(customerIdText.getText()));
                    if ("Delete successful".equals(deleteMessage)) {
                        clear();
                        generateAndSetCustomerId();
                        loadAllCustomers();
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Customer Successfully!").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Delete failed").show();
                    }
                }
            }else if("No".equals(result)){
                    new Alert(Alert.AlertType.ERROR, "It cannot be deleted until the customer hands over the car").show();
                }


        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).show();
        }
    }

    private void searchCustomer() {
        CustomerTableModel customerTableModel = customerTable.getSelectionModel().getSelectedItem();

        customerIdText.setText(String.valueOf(customerTableModel.getId()));
        cNicText.setText(customerTableModel.getNic());
        cNameText.setText(customerTableModel.getName());
        cMobileText.setText(customerTableModel.getMobile());
        cAddressText.setText(customerTableModel.getAddress());
    }

    private void loadAllCustomers() throws SQLException {
        List<CustomerDto> customerDtoList = customerService.getAllCus();

        ObservableList<CustomerTableModel> customerTableModelList = FXCollections.observableArrayList();

        for (CustomerDto customerDto : customerDtoList) {
            CustomerTableModel customerTableModel = new CustomerTableModel(
                    String.valueOf(customerDto.getId()),
                    customerDto.getName(),
                    customerDto.getNic(),
                    customerDto.getAddress(),
                    customerDto.getMobile()
            );

            customerTableModelList.add(customerTableModel);
        }

        customerTable.setItems(customerTableModelList);
    }

    @FXML
    void clearFields(MouseEvent event) {
        clear();
        generateAndSetCustomerId();
    }

    private void clear() {
        customerIdText.setText("");
        cNameText.setText("");
        cNicText.setText("");
        cAddressText.setText("");
        cMobileText.setText("");
    }

}
