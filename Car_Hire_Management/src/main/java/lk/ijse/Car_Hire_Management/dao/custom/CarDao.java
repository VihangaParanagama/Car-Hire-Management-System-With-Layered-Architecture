package lk.ijse.Car_Hire_Management.dao.custom;

import lk.ijse.Car_Hire_Management.dao.CrudDao;
import lk.ijse.Car_Hire_Management.entity.CarEntity;
import org.hibernate.Session;

import java.util.List;

public interface CarDao extends CrudDao<CarEntity> {
    boolean updateAvailabilityById(Integer carId, String newAvailability, Session session);

    Double getPerdayRentByCarId(Integer carId, Session session);

    Integer getLastCarId(Session session);

    List<Integer> getAvailableCarIdsByCarCategory(String CarCategory, Session session);

    String checkCarInRentById(Integer carID,Session session);

    Integer countAllCarCars(Session session);

    Long countAllCarInaRent(Session session);

}
