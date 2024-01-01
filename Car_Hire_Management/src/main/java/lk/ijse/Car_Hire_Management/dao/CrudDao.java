package lk.ijse.Car_Hire_Management.dao;

import lk.ijse.Car_Hire_Management.dao.SuperDao;
import lk.ijse.Car_Hire_Management.entity.SuperEntity;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public interface CrudDao<T extends SuperEntity> extends SuperDao {
    Boolean save(T entity, Session session);

    Boolean update(T entity, Session session);

    Boolean delete(Integer id, Session session);

    T get(Integer id, Session session);

    List <T> getAll(Session session);
}
