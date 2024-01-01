package lk.ijse.Car_Hire_Management.service.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.Car_Hire_Management.dao.DaoFactory;
import lk.ijse.Car_Hire_Management.dao.custom.CarCategoryDao;
import lk.ijse.Car_Hire_Management.db.SessionFactoryConfiguration;
import lk.ijse.Car_Hire_Management.dto.CarCategoryDto;
import lk.ijse.Car_Hire_Management.entity.CarCategoryEntity;
import lk.ijse.Car_Hire_Management.entity.CarEntity;
import lk.ijse.Car_Hire_Management.service.custom.CarCategoryService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarCategoryServiceImpl implements CarCategoryService {

    CarCategoryDao carCategoryDao;

    public CarCategoryServiceImpl() {
        carCategoryDao = (CarCategoryDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_CATEGORY);
    }
    @Override
    public boolean saveCategory(CarCategoryDto carCategoryDto) {
        CarCategoryEntity carCategoryEntity = new CarCategoryEntity();

        carCategoryEntity.setId(carCategoryDto.getId());
        carCategoryEntity.setName(carCategoryDto.getName());
        carCategoryEntity.setDailyRentalRate(carCategoryDto.getDailyRentalRate());


        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Boolean save = carCategoryDao.save(carCategoryEntity,session);
            if (save) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }
    }

    @Override
    public boolean deleteCategory(CarCategoryDto id) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            CarCategoryEntity carCategoryEntity = session.get(CarCategoryEntity.class, (Serializable) id);

            Transaction transaction = session.getTransaction();
            transaction.begin();
            if (carCategoryEntity != null) {

                session.remove(carCategoryEntity);
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        } catch (Exception exception) {
            throw exception;
        }
    }

    @Override
    public boolean UpdateCAtegory(CarCategoryDto carCategoryDto) {
        CarCategoryEntity carCategoryEntity = new CarCategoryEntity();

        carCategoryEntity.setId(carCategoryDto.getId());
        carCategoryEntity.setName(carCategoryDto.getName());
        carCategoryEntity.setDailyRentalRate(carCategoryDto.getDailyRentalRate());


        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            Boolean save = carCategoryDao.update(carCategoryEntity,session);
            if (save) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
        }
    }

    @Override
    public CarCategoryDto getCategory(String name) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();

            CarCategoryEntity existingCategory = carCategoryDao.getCarCategoryByName(name, session);

            if (existingCategory == null) {
                return null;
            }

            new Alert(Alert.AlertType.WARNING, "Car Category already exists!").show();

            return new CarCategoryDto(existingCategory.getId(), existingCategory.getName(),existingCategory.getDailyRentalRate());
        }
    }





    @Override
    public List<CarCategoryDto> getAllCategory() {
        try {
            Session session = SessionFactoryConfiguration.getInstance().getSession();
            Transaction transaction = session.getTransaction();
            transaction.begin();

            List<CarCategoryEntity> carCategoryEntities = carCategoryDao.getAll(session);

            List<CarCategoryDto> carCategoryDtos = new ArrayList<>();

            for (CarCategoryEntity carCategoryEntity : carCategoryEntities) {
                carCategoryDtos.add(new CarCategoryDto(
                        carCategoryEntity.getId(),
                        carCategoryEntity.getName(),
                        carCategoryEntity.getDailyRentalRate()
                ));
            }

            transaction.commit();
            return carCategoryDtos;

        } catch (Exception exception) {
            throw exception;
        }
    }


    @Override
    public CarCategoryDto getCarCategoryByName(String categoryName) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            Transaction transaction = session.beginTransaction();

            CarCategoryDao carCategoryDao = (CarCategoryDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.CAR_CATEGORY);
            CarCategoryEntity carCategoryEntity = carCategoryDao.getCarCategoryByName(categoryName, session);

            transaction.commit();

            if (carCategoryEntity != null) {
                return new CarCategoryDto(carCategoryEntity.getId(), carCategoryEntity.getName() ,carCategoryEntity.getDailyRentalRate());
            } else {
                return null;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getLastCarCategoryId() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            return carCategoryDao.getLastCarCategoryId(session);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting the last user ID", e);
        }
    }

    @Override
    public Double getPerDayRentByCarCategory(String carCategory) {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {
            session.beginTransaction(); // Start a new transaction

            Double perDayRent = carCategoryDao.getPerDayRentByCarCategory(carCategory, session);

            session.getTransaction().commit();

            return perDayRent;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Integer countAllCarcategories() {
        try (Session session = SessionFactoryConfiguration.getInstance().getSession()) {

            return carCategoryDao.countAllCarCategories(session);
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}
