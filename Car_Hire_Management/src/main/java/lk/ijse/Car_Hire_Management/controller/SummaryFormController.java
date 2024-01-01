package lk.ijse.Car_Hire_Management.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lk.ijse.Car_Hire_Management.service.ServiceFactory;
import lk.ijse.Car_Hire_Management.service.custom.*;
import lk.ijse.Car_Hire_Management.service.util.ServiceType;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.ResourceBundle;

public class SummaryFormController implements Initializable {
    private UserService userService;

    private CustomerService customerService;

    private CarService carService;

    private CarCategoryService carCategoryService;

    private RentService rentService;

    @FXML
    private Label carCategoriesTotalText;

    @FXML
    private Label carInRentText;

    @FXML
    private Label carsTotalText;

    @FXML
    private Label earningOfOverdueText;

    @FXML
    private Label earningOfRentText;


    @FXML
    private Label grandTotal;

    @FXML
    private Label totalCustomersText;

    @FXML
    private Label totalOverdueRentText;

    @FXML
    private Label totalRentText;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userService = ServiceFactory.getInstance().getService(ServiceType.USER);
        customerService = ServiceFactory.getInstance().getService(ServiceType.CUSTOMER);
        carCategoryService = ServiceFactory.getInstance().getService(ServiceType.CAR_CATEGORY);
        carService = ServiceFactory.getInstance().getService(ServiceType.CAR);
        rentService = ServiceFactory.getInstance().getService(ServiceType.RENT);

        Customer();
        Cars();
        Rent();
        Total();

    }

    public void Customer() {
        Integer TotalCustomers = customerService.countAllCustomers();
        totalCustomersText.setText(String.valueOf(" :      "+TotalCustomers));
    }

    public void Cars() {
        Integer TotalCarCategories = carCategoryService.countAllCarcategories();
        carCategoriesTotalText.setText(String.valueOf(" :      "+TotalCarCategories));

        Integer TotalCars = carService.countAllCars();
        carsTotalText.setText(String.valueOf(" :      "+TotalCars));

        Long CarsInARent = carService.countAllCarInaRent();
        carInRentText.setText(String.valueOf(":      "+CarsInARent));
    }

    public void Rent() {
        Integer RentTotal = rentService.countAllRents();
        totalRentText.setText(String.valueOf(" :      "+RentTotal));

        Integer overDUeRents = rentService.countOverdueRents();
        totalOverdueRentText.setText(String.valueOf(" :      "+overDUeRents));


    }

    public void Total() {
        Double total = rentService.getTotalRentAmount();
        earningOfRentText.setText(String.valueOf("  :      Rs. "+total));

        Double overDueErnings = rentService.getTotalDifferenceAmount();
        earningOfOverdueText.setText(String.valueOf("  :      Rs. "+overDueErnings));

        Double totalEarinigs = rentService.getGrandTotalAmount();
        grandTotal.setText(String.valueOf(" :      Rs. "+totalEarinigs));

    }
}
