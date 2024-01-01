package lk.ijse.Car_Hire_Management.dao.custom;

import lk.ijse.Car_Hire_Management.dao.CrudDao;
import lk.ijse.Car_Hire_Management.entity.CarCategoryEntity;
import org.hibernate.Session;

public interface CarCategoryDao extends CrudDao<CarCategoryEntity> {
    CarCategoryEntity getCarCategoryByName(String categoryName, Session session);

    Integer getLastCarCategoryId(Session session);

    Double getPerDayRentByCarCategory(String CarCategory, Session session);

    Integer countAllCarCategories(Session session);
}
