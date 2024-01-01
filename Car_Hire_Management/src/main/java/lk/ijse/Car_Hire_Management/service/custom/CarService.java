package lk.ijse.Car_Hire_Management.service.custom;

import lk.ijse.Car_Hire_Management.dto.CarDto;
import org.hibernate.Session;

import java.util.List;

public interface CarService extends SuperService{

    boolean saveCar(CarDto carDto);
    boolean deleteCar(Integer id);

    boolean updateCar(CarDto carDto);

    CarDto getCar(String id);

    List<CarDto> getAllCar();

    boolean updateAvailabilityById(Integer carId, String newAvailability);

    Double getPerdayRentByCarId(Integer carId);

//    Double getRefundableDepositByCarId(Integer carId);

    boolean updateAvailabilityByRentId(Integer RentId, String newAvailability);
    Integer getLastCarId();

    List<Integer> getAvailableCarIdsByCarCategory(String carCategory);

    String checkCarInRentById(Integer carId);

    Integer countAllCars();

    Long countAllCarInaRent();
}
