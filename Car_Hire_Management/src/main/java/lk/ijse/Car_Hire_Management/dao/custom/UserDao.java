package lk.ijse.Car_Hire_Management.dao.custom;

import lk.ijse.Car_Hire_Management.dao.CrudDao;
import lk.ijse.Car_Hire_Management.entity.UserEntity;
import org.hibernate.Session;

public interface UserDao extends CrudDao<UserEntity> {
    UserEntity getByUsername(String username, Session session);

    Integer getLastUserId(Session session);
}
